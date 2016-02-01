package org.mercycorps.translationcards;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.net.Uri;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DecksActivityTest {

    private DecksActivity decksActivity;

    @Before
    public void setUp() throws Exception {
        decksActivity = Robolectric.setupActivity(DecksActivity.class);
    }

    @Test
    public void onCreate_shouldCreateDecksActivity() {
        assertNotNull(decksActivity);

        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);
        assertThat(decksListView, is(notNullValue()));
    }

    @Test
    public void initFeedbackButton_shouldShowFeedbackButton() {
        Button feedbackButton = (Button) decksActivity.findViewById(R.id.feedback_button);

        assertThat(feedbackButton.getText().toString(), is("GIVE FEEDBACK"));
    }

    @Test
    public void initFeedbackButton_shouldLaunchFeedbackForm() {
        decksActivity.findViewById(R.id.feedback_button).performClick();

        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/viewform?entry.1158658650=0.2.1"));
        assertThat(shadowOf(decksActivity).getNextStartedActivity(), is(expectedIntent));
    }

    @Test
    public void initDecksList_shouldHaveDefaultDeck() {
        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);

        assertThat(decksListView.getAdapter().getCount(), is(2));
        assertThat((String) decksListView.getAdapter().getItem(0), is("Default"));
    }

    @Test
    public void initDecksList_shouldPopulateDeckNameTextView() {
        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);
        View decksListItem = decksListView.getAdapter().getView(0, null, decksListView);

        TextView deckName = (TextView) decksListItem.findViewById(R.id.deck_name);
        assertThat(deckName, is(notNullValue()));
        assertThat(deckName.getText().toString(), is("Default"));
    }

    @Test
    public void initDecksList_shouldOpenMainActivityWhenItemClicked() {
        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);
        View decksListItem = decksListView.getAdapter().getView(0, null, decksListView);

        TextView deckName = (TextView) decksListItem.findViewById(R.id.deck_name);
        deckName.performClick();

        Intent expectedIntent = new Intent(decksActivity, TranslationsActivity.class);
        String dictionaryArray [] = new String[]{"3", "2", "1"};
        expectedIntent.putExtra("DictionaryIds", dictionaryArray);
        Intent nextStartedActivity = shadowOf(decksActivity).getNextStartedActivity();
        assertThat(nextStartedActivity.getStringArrayExtra("DictionaryIds"),
                is(dictionaryArray));
        assertThat(nextStartedActivity.getComponent().getClassName(),
                is(expectedIntent.getComponent().getClassName()));
    }
}
