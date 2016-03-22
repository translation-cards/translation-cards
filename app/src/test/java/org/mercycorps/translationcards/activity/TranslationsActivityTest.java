package org.mercycorps.translationcards.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import org.mercycorps.translationcards.data.Translation;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
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
    private Translation translation;

    @Before
    public void setUp() {
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        dbManagerMock = application.getDbManager();
        Intent intent = new Intent();
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG, false);
        intent.putExtra("Deck", deck);
        initializeMockDbManager();
        translationsActivity = Robolectric.buildActivity(TranslationsActivity.class).withIntent(intent).create().get();
    }

    private void initializeMockDbManager() {
        Dictionary[] dictionaries = new Dictionary[1];
        translation = new Translation(TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG,
                TRANSLATED_TEXT);
        Translation nullTranslatedTextTranslation = new Translation(
                TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG, null);
        Translation[] translations = {translation, nullTranslatedTextTranslation};
        dictionaries[0] = new Dictionary(DICTIONARY_TEST_LABEL, translations, DEFAULT_LONG,
                DEFAULT_DECK_ID);
        when(dbManagerMock.getAllDictionariesForDeck(DEFAULT_DECK_ID)).thenReturn(dictionaries);
    }

    @Test
    public void onCreate_shouldShowDeckNameInToolbar() {
        assertThat(translationsActivity.getSupportActionBar().getTitle().toString(), is(
                DEFAULT_DECK_NAME));
    }

    @Test
    public void onCreate_dbmGetsCalled() {
        verify(dbManagerMock).getAllDictionariesForDeck(DEFAULT_DECK_ID);
    }

    @Test
    public void initList_shouldHaveCorrectOriginTextWhenCardIsCreated() {
        View translationsListItem = firstTranslationCardInListView();

        TextView originTranslationText = (TextView) translationsListItem.findViewById(
                R.id.origin_translation_text);

        assertThat(originTranslationText.getText().toString(), is(TRANSLATION_LABEL));
    }

    @Test
    public void initList_shouldHaveCorrectTranslatedTextWhenCardIsCreated() {
        View translationsListItem = firstTranslationCardInListView();

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);
        assertThat(translatedText.getText().toString(), is(TRANSLATED_TEXT));
    }

    @Test
    public void initList_shouldHaveEditCardIcon() {
        View translationsListItem = firstTranslationCardInListView();

        ImageView editCardIcon = (ImageView) translationsListItem.findViewById(R.id.edit_card_icon);
        assertThat(editCardIcon, is(notNullValue()));
    }

    @Test
    public void initList_shouldHaveEditCardLabel() {
        View translationsListItem = firstTranslationCardInListView();

        TextView editCardLabel = (TextView) translationsListItem.findViewById(R.id.edit_card_label);
        assertThat(editCardLabel.getText().toString(), is("Edit this flashcard"));
    }

    @Test
    public void initList_shouldHaveDeleteCardIcon() {
        View translationsListItem = firstTranslationCardInListView();

        ImageView deleteCardIcon = (ImageView) translationsListItem.findViewById(R.id.delete_card_icon);
        assertThat(deleteCardIcon, is(notNullValue()));
    }

    @Test
    public void initList_shouldHaveDeleteCardLabel() {
        View translationsListItem = firstTranslationCardInListView();

        TextView deleteCardLabel = (TextView) translationsListItem.findViewById(R.id.delete_card_label);
        assertThat(deleteCardLabel.getText().toString(), is("Delete this flashcard"));
    }

    @Test
    public void shouldHaveCorrectHintMessageWhenTranslatedTextIsEmpty() {
        View translationsListItem = listItemWithNoTranslatedText();

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getText().toString(), is("Add " + DICTIONARY_TEST_LABEL + " translation"));
    }

    @Test
    public void shouldHaveCorrectTextFormattingWhenTranslatedTextIsEmpty() {
        int disabledTextColor = -7960954;
        View translationsListItem = listItemWithNoTranslatedText();

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getTextSize(), is(18f));
        assertThat(translatedText.getCurrentTextColor(), is(disabledTextColor));
    }

    @Test
    public void onClick_shouldStartRecordingActivityWhenEditLayoutIsClicked() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        assertThat(nextStartedActivity.getComponent().getClassName(), is(RecordingActivity.class.getCanonicalName()));
    }

    @Test
    public void shouldPassCorrectDictionaryWhenEditLayoutIsClicked() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        String dictionaryLabel = nextStartedActivity.getStringExtra(RecordingActivity.INTENT_KEY_DICTIONARY_LABEL);
        assertThat(dictionaryLabel, is(DICTIONARY_TEST_LABEL));
    }

    @Test
    public void onClick_shouldShowDeleteConfirmationDialogWhenDeleteLayoutIsClicked(){
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_card_delete).performClick();

        ShadowAlertDialog shadowAlertDialog = shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        assertThat(shadowAlertDialog.getMessage().toString(), is("Are you sure you want to delete this translation card?"));}

    @Test
    public void onClick_shouldDeleteCorrectTranslationWhenDeleteButtonIsClicked() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_card_delete).performClick();

        ShadowAlertDialog.getLatestAlertDialog().getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        verify(dbManagerMock).deleteTranslation(translation.getDbId());
        verify(dbManagerMock, times(2)).getAllDictionariesForDeck(DEFAULT_DECK_ID);
    }

    @Test
    public void onClick_shouldRefreshCurrentDictionaryWhenATranslationCardIsDeleted() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_card_delete).performClick();

        ShadowAlertDialog.getLatestAlertDialog().getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        verify(dbManagerMock, times(2)).getAllDictionariesForDeck(DEFAULT_DECK_ID);
    }

    @Test
    public void shouldShowCollapsedCardIndicatorByDefault() {
        View translationsListItem = firstTranslationCardInListView();

        ImageView cardIndicator = (ImageView) translationsListItem.findViewById(R.id.indicator_icon);

        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.expand_arrow));
    }

    @Test
    public void shouldHideTranslationCardChildByDefault() {
        View translationsListItem = firstTranslationCardInListView();

        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.GONE));
    }

    @Test
    public void shouldShowCollapsedCardIndicatorWhenTranslationCardIsExpandedThenCollapsed() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();

        ImageView cardIndicator = (ImageView) translationsListItem.findViewById(R.id.indicator_icon);
        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.expand_arrow));
    }

    @Test
    public void shouldHideTranslationCardChildWhenTranslationCardIsExpandedThenCollapsed() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();

        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.GONE));
    }

    @Test
    public void onClick_shouldShowExpandedCardIndicatorWhenTranslationCardIsExpanded() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();

        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.VISIBLE));
    }

    @Test
    public void onClick_shouldShowTranslationCardChildWhenCardIsExpanded() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_indicator_layout).performClick();

        ImageView cardIndicator = (ImageView) translationsListItem.findViewById(R.id.indicator_icon);
        assertThat(shadowOf(cardIndicator.getBackground()).getCreatedFromResId(), is(R.drawable.collapse_arrow));
    }

    @Test
    public void initTabs_shouldShowLanguageTabWhenOnHomeScreen() {
        LinearLayout tabContainer = (LinearLayout) translationsActivity.findViewById(R.id.tabs);

        assertThat(tabContainer.getChildCount(), is(1));

        TextView languageTabText = (TextView) tabContainer.getChildAt(0).findViewById(R.id.tab_label_text);
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

    private View firstTranslationCardInListView() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);
        return translationsList.getAdapter().getView(1, null, translationsList);
    }

    private View listItemWithNoTranslatedText() {
        ListView translationsList = (ListView) translationsActivity.findViewById(
                R.id.translations_list);
        return translationsList.getAdapter().getView(2, null, translationsList);
    }
}