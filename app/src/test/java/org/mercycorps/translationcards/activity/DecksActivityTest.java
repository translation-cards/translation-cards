package org.mercycorps.translationcards.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.net.Uri;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Tests for DecksActivity
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DecksActivityTest {

    private static final String DEFAULT = "Default";
    private static final int DB_ID = -1;
    private static final String EXTERNAL_ID = "";
    private static final long TIMESTAMP = new Date().getTime();
    private static final boolean LOCKED = false;
    private static final String PUBLISHER = "My Deck";
    private static final String DEFAULT_ISO_LANGUAGE = "en";
    private DecksActivity decksActivity;

    @Before
    public void setUp() throws Exception {
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        DbManager dbManagerMock = application.getDbManager();
        Deck defaultDeck = new Deck(DEFAULT, PUBLISHER, EXTERNAL_ID, DB_ID, TIMESTAMP, LOCKED, DEFAULT_ISO_LANGUAGE);
        when(dbManagerMock.getAllDecks()).thenReturn(new Deck[] {defaultDeck});
//        when(dbManagerMock.getTranslationLanguagesForDeck(DB_ID)).thenReturn("ARABIC   FARSI   PASHTO");
        when(dbManagerMock.getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{new Dictionary("Arabic"), new Dictionary("Farsi"), new Dictionary("Pashto")});
        decksActivity = Robolectric.setupActivity(DecksActivity.class);
    }

    @Test
    public void onCreate_shouldCreateDecksListView() {
        assertThat(decksActivity.findViewById(R.id.decks_list), is(notNullValue()));
    }

    @Test
    public void onCreate_shouldSetActionBarTitleToMyDecks() {
        assertThat(decksActivity.getSupportActionBar().getTitle().toString(), is("My Decks"));
    }

    @Test
    public void initFeedbackButton_shouldShowFeedbackButton() {
        Button feedbackButton = (Button) decksActivity.findViewById(R.id.feedback_button);

        assertThat(feedbackButton.getText().toString(), is("GIVE FEEDBACK"));
    }

    @Test
    public void initFeedbackButton_shouldLaunchFeedbackForm() {
        decksActivity.findViewById(R.id.feedback_button).performClick();

        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/viewform?entry.1158658650=0.3.2"));
        assertThat(shadowOf(decksActivity).getNextStartedActivity(), is(expectedIntent));
    }

    @Test
    public void initDecksList_shouldHaveDefaultDeck() {
        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);

        assertThat(decksListView.getAdapter().getCount(), is(2));
        assertThat(((Deck) (decksListView.getAdapter().getItem(0))).getLabel(), is(DEFAULT));
    }

    @Test
    public void initDecksList_shouldHaveDefaultDeckName() {
        View decksListItem = getFirstDeckInListView();

        TextView deckName = (TextView) decksListItem.findViewById(R.id.deck_name);
        assertThat(deckName.getText().toString(), is(DEFAULT));
    }

    @Test
    public void initDecksList_shouldDisplayFormattedDeckInformation() {
        View decksListItem = getFirstDeckInListView();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.getDefault());
        String date = dateFormat.format(new Date());

        TextView deckInformation = (TextView) decksListItem.findViewById(R.id.deck_information);

        assertThat(deckInformation.getText().toString(), is("My Deck, "+ date));
    }

    @Test
    public void initDecksList_shouldNameOriginTextFieldEnglish() {
        View decksListItem = getFirstDeckInListView();

        TextView originLanguage = (TextView) decksListItem.findViewById(R.id.origin_language);

        assertThat(originLanguage.getText().toString(), is("ENGLISH"));
    }

    @Test
    public void initDecksList_shouldContainCorrectDestinationLanguages() {
        View decksListItem = getFirstDeckInListView();

        TextView translationLanguages = (TextView) decksListItem.findViewById(R.id.translation_languages);

        assertEquals("ARABIC   FARSI   PASHTO", translationLanguages.getText().toString());
    }

    @Test
    public void initDecksList_shouldOpenTranslationsActivityWhenItemClicked() {
        View decksListItem = getFirstDeckInListView();

        LinearLayout deckLayout = (LinearLayout) decksListItem.findViewById(R.id.translation_card);
        deckLayout.performClick();

        Intent nextStartedActivity = shadowOf(decksActivity).getNextStartedActivity();
        assertThat(nextStartedActivity.getComponent().getClassName(), is(TranslationsActivity.class.getCanonicalName()));
    }

    @Test
    public void initDecksList_shouldPassDeckToTranslationActivityWhenListItemIsClicked() {
        View decksListItem = getFirstDeckInListView();

        LinearLayout deckLayout = (LinearLayout) decksListItem.findViewById(R.id.translation_card);
        deckLayout.performClick();

        Intent nextStartedActivity = shadowOf(decksActivity).getNextStartedActivity();
        Deck deck = (Deck) nextStartedActivity.getSerializableExtra("Deck");
        assertThat(deck.getLabel(), is(DEFAULT));
        assertThat(deck.getDbId(), is((long) DB_ID));
    }

    private View getFirstDeckInListView() {
        ListView decksListView = (ListView) decksActivity.findViewById(R.id.decks_list);
        return decksListView.getAdapter().getView(0, null, decksListView);
    }

}