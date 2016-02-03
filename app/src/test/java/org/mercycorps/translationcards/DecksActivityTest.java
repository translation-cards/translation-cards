package org.mercycorps.translationcards;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
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

import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
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

        Toolbar toolbar = (Toolbar) decksActivity.findViewById(R.id.translation_cards_toolbar);
        assertThat(toolbar.getTitle().toString(), is("My Decks"));
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
        assertThat(((Deck) (decksListView.getAdapter().getItem(0))).getLabel(), is("Default"));
    }

    @Test
    public void initDecksList_shouldPopulateDefaultDeckCard() {
        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);
        View decksListItem = decksListView.getAdapter().getView(0, null, decksListView);

        TextView deckName = (TextView) decksListItem.findViewById(R.id.deck_name);
        assertThat(deckName.getText().toString(), is("Default"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String date = dateFormat.format(new Date());
        TextView deckInformation = (TextView) decksListItem.findViewById(R.id.deck_information);
        assertThat(deckInformation.getText().toString(), is("My Deck, "+ date));

        TextView originLanguage = (TextView) decksListItem.findViewById(R.id.origin_language);
        assertThat(originLanguage.getText().toString(), is("ENGLISH"));

        TextView translationLanguages = (TextView) decksListItem.findViewById(R.id.translation_languages);
        assertThat(translationLanguages.getText().toString(), is("ARABIC   FARSI   PASHTO"));
    }

    @Test
    public void initDecksList_shouldOpenTranslationsActivityWhenItemClicked() {
        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);
        View decksListItem = decksListView.getAdapter().getView(0, null, decksListView);

        TextView deckName = (TextView) decksListItem.findViewById(R.id.deck_name);
        deckName.performClick();

        Intent nextStartedActivity = shadowOf(decksActivity).getNextStartedActivity();
        Deck deck = (Deck) nextStartedActivity.getSerializableExtra("Deck");
        assertThat(deck.getLabel(), is("Default"));
        assertThat(deck.getDbId(), is((long) 1));
    }
}
