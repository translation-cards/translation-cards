package org.mercycorps.translationcards;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.AbstractModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDrawable;

import roboguice.RoboGuice;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Test for TranslationsActivity
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationsActivityTest {

    public static final int DEFAULT_DECK_ID = 1;
    public static final String NO_VALUE = "";
    public static final long DEFAULT_LONG = -1;
    public static final String DICTIONARY_TEST_LABEL = "TestLabel";
    public static final String TRANSLATED_TEXT = "TranslatedText";
    public static final String TRANSLATION_LABEL = "TranslationLabel";
    public static final String DEFAULT_DECK_NAME = "Default";
    private TranslationsActivity translationsActivity;
    private DbManager dbManagerMock;
    private Dictionary.Translation translation;

    @Before
    public void setUp() {
        RoboGuice.setUseAnnotationDatabases(false);
        Intent intent = new Intent();
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG);
        intent.putExtra("Deck", deck);
        initializeMockDbManager();
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application,
                new TranslationsActivityTestModule());
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
        translationsActivity = Robolectric.buildActivity(TranslationsActivity.class).withIntent(intent).create().get();
    }

    private void initializeMockDbManager() {
        dbManagerMock = mock(DbManager.class);
        Dictionary[] dictionaries = new Dictionary[1];
        translation = new Dictionary.Translation(TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG,
                TRANSLATED_TEXT);
        Dictionary.Translation emptyTranslatedTextTranslation = new Dictionary.Translation(TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG,
                NO_VALUE);
        Dictionary.Translation[] translations = {translation, emptyTranslatedTextTranslation};
        dictionaries[0] = new Dictionary(DICTIONARY_TEST_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);
        when(dbManagerMock.getAllDictionariesForDeck(DEFAULT_DECK_ID)).thenReturn(dictionaries);
    }

    @Test
    public void onCreate_shouldShowDeckNameInToolbar() {
        assertThat(translationsActivity.getSupportActionBar().getTitle().toString(), is(DEFAULT_DECK_NAME));
    }

    @Test
    public void onCreate_dbmGetsCalled() {
        verify(dbManagerMock).getAllDictionariesForDeck(DEFAULT_DECK_ID);
    }

    @Test
    public void onCreate_shouldInitializeCards() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);
        View translationsListItem = translationsList.getAdapter().getView(1, null, translationsList);

        TextView originTranslationText = (TextView) translationsListItem.findViewById(R.id.origin_translation_text);
        assertThat(originTranslationText.getText().toString(), is(TRANSLATION_LABEL));

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);
        assertThat(translatedText.getText().toString(), is(TRANSLATED_TEXT));

        ImageView editCardIcon = (ImageView) translationsListItem.findViewById(R.id.edit_card_icon);
        assertThat(editCardIcon, is(notNullValue()));

        TextView editCardLabel = (TextView) translationsListItem.findViewById(R.id.edit_card_label);
        assertThat(editCardLabel.getText().toString(), is("Edit this flashcard"));

        ImageView deleteCardIcon = (ImageView) translationsListItem.findViewById(R.id.delete_card_icon);
        assertThat(deleteCardIcon, is(notNullValue()));

        TextView deleteCardLabel = (TextView) translationsListItem.findViewById(R.id.delete_card_label);
        assertThat(deleteCardLabel.getText().toString(), is("Delete this flashcard"));
    }

    @Test
    public void shouldDisplayAndFormatNoTranslationTextStringWhenTranslatedTextLeftEmpty() {
        int disabledTextColor = -7960954;
        ListView translationsList = (ListView) translationsActivity.findViewById(R.id.translations_list);
        View translationsListItem = translationsList.getAdapter().getView(2, null, translationsList);

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);
        assertThat(translatedText.getText().toString(), is("Add " + DICTIONARY_TEST_LABEL + " translation"));
        assertThat(translatedText.getTextSize(), is(18f));
        assertThat(translatedText.getCurrentTextColor(), is(disabledTextColor));
    }

    @Test
    public void onClick_shouldStartRecordingActivityWhenEditLayoutIsClicked() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);
        View translationsListItem = translationsList.getAdapter().getView(1, null, translationsList);

        translationsListItem.findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        String dictionaryLabel = nextStartedActivity.getStringExtra(RecordingActivity.INTENT_KEY_DICTIONARY_LABEL);
        assertThat(dictionaryLabel, is(DICTIONARY_TEST_LABEL));
    }

    @Test
    public void onClick_shouldShowDeleteConfirmationDialogWhenDeleteLayoutIsClicked(){
        ListView translationsList = (ListView) translationsActivity.findViewById(R.id.translations_list);

        View translationsListItem = translationsList.getAdapter().getView(1, null, translationsList);
        translationsListItem.findViewById(R.id.translation_card_delete).performClick();

        ShadowAlertDialog shadowAlertDialog = shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        assertThat(shadowAlertDialog.getMessage().toString(),
                is("Are you sure you want to delete this translation card?"));

        ShadowAlertDialog.getLatestAlertDialog().getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        verify(dbManagerMock).deleteTranslation(translation.getDbId());
        verify(dbManagerMock, times(2)).getAllDictionariesForDeck(DEFAULT_DECK_ID);
    }

    @Test
    public void onClick_shouldInflateAndDeflateTranslationListItem() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);

        View translationsListItem = translationsList.getAdapter().getView(1, null, translationsList);

        ImageView cardIndicator = (ImageView) translationsListItem.findViewById(R.id.indicator_icon);

        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.GONE));
        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.forward_arrow));

        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.VISIBLE));
        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.back_arrow));

        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.GONE));
        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.forward_arrow));
    }

    @Test
    public void initTabs_shouldShowLanguageTabWhenOnHomeScreen() {
        LinearLayout tabContainer = (LinearLayout) translationsActivity.findViewById(R.id.tabs);

        assertThat(tabContainer.getChildCount(), is(1));

        View languageTab = tabContainer.getChildAt(0);
        TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
        assertThat(languageTabText.getText().toString(), is(DICTIONARY_TEST_LABEL.toUpperCase()));
    }

    @Test
    public void setDictionary_shouldNotHaveAnyTranslationCardsWhenNoneHaveBeenCreated() {
        TextView translationCardText = (TextView) translationsActivity.findViewById(R.id.origin_translation_text);

        assertThat(translationCardText, is(nullValue()));
    }

    @Test
    public void shouldGoToDecksActivityWhenBackButtonPressed() {
        ShadowActivity shadowActivity = Shadows.shadowOf(translationsActivity);

        shadowActivity.onBackPressed();
        assertTrue(shadowActivity.isFinishing());
    }

    public class TranslationsActivityTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(DbManager.class).toInstance(dbManagerMock);
        }

    }
}