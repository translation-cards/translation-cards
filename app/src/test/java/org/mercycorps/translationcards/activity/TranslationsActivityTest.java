package org.mercycorps.translationcards.activity;

import android.app.Activity;
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
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.activity.addTranslation.GetStartedActivity;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowAlertDialog;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.CONTEXT_INTENT_KEY;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findView;
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
    public static final String DICTIONARY_ARABIC_LABEL = "ARABIC";
    public static final String DICTIONARY_FARSI_LABEL = "FARSI";
    public static final String TRANSLATED_TEXT = "TranslatedText";
    public static final String TRANSLATION_LABEL = "TranslationLabel";
    public static final String DEFAULT_DECK_NAME = "Default";
    private static final String EMPTY_DECK_TITLE = "Let's make this useful";
    public static final String INTENT_KEY_DECK = "Deck";
    private static final int EMPTY_DECK_ID = 2;
    private static final String DEFUALT_ISO_CODE = "en";
    private static final String NO_ISO_CODE = "";
    private TranslationsActivity translationsActivity;
    private DbManager dbManagerMock;
    private Translation translation;
    private Deck deck;
    private Dictionary[] dictionaries;

    @Before
    public void setUp() {
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        dbManagerMock = application.getDbManager();
        Intent intent = new Intent();
        deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG, false, DEFUALT_ISO_CODE);
        intent.putExtra("Deck", deck);
        initializeMockDbManager();
        translationsActivity = Robolectric.buildActivity(TranslationsActivity.class).withIntent(intent).create().get();
    }

    private void initializeMockDbManager() {
        dictionaries = new Dictionary[3];
        translation = new Translation(TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG, TRANSLATED_TEXT);
        Translation nullTranslatedTextTranslation = new Translation(TRANSLATION_LABEL, false, "", DEFAULT_LONG, null);
        Translation[] translations = {translation, nullTranslatedTextTranslation};
        dictionaries[0] = new Dictionary(NO_ISO_CODE, DICTIONARY_TEST_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);
        dictionaries[1] = new Dictionary(NO_ISO_CODE, DICTIONARY_ARABIC_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);
        dictionaries[2] = new Dictionary(NO_ISO_CODE, DICTIONARY_FARSI_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);
        when(dbManagerMock.getAllDictionariesForDeck(DEFAULT_DECK_ID)).thenReturn(dictionaries);
    }

    @Test
    public void shouldShowWelcomeTitleWhenNoCardsArePresent() {
        Activity activity = createEmptyTranslationsActivity();
        TextView welcomeTitle = (TextView) activity.findViewById(R.id.empty_deck_title);
        assertThat(welcomeTitle.getText().toString(), is(EMPTY_DECK_TITLE));
    }

    @Test
    public void shouldNotShowWelcomeTitleWhenCardsArePresent() {
        TextView welcomeMessageTitle = findTextView(translationsActivity, R.id.empty_deck_title);
        assertEquals(View.GONE, welcomeMessageTitle.getVisibility());
    }

    @Test
    public void shouldShowWelcomeMessageWhenNoCardsArePresent() {
        Activity activity = createEmptyTranslationsActivity();
        TextView welcomeMessage = findTextView(activity, R.id.empty_deck_message);
        assertEquals("This deck doesn't have any cards.\\nGet started by creating your first card.",
                welcomeMessage.getText().toString());
    }

    @Test
    public void shouldNotDisplayCreateTranslationButtonWhenDeckIsLocked() {
        Activity activity = createLockedDeckTranslationsActivity();
        View addTranslationButton = findView(activity, R.id.add_translation_button);
        assertEquals(View.GONE, addTranslationButton.getVisibility());
    }

    @Test
    public void shouldDisplayCreateTranslationButtonWhenDeckIsUnlocked() {
        Activity activity = createEmptyTranslationsActivity();
        View addTranslationButton = findView(activity, R.id.add_translation_button);
        assertEquals(View.VISIBLE, addTranslationButton.getVisibility());
    }

    @Test
    public void shouldNotDisplayHeaderInEmptyDeck() {
        Activity activity= createEmptyTranslationsActivity();
        LinearLayout header = findLinearLayout(activity, R.id.translation_list_header);
        assertEquals(View.GONE, header.getVisibility());
    }

    @Test
    public void shouldDisplayHeaderWhenDeckIsPopulated() {
        LinearLayout header = findLinearLayout(translationsActivity, R.id.translation_list_header);
        assertEquals(View.VISIBLE, header.getVisibility());
    }

    @Test
    public void shouldNotShowWelcomeMessageWhenCardsArePresent() {
        TextView welcomeMessage = findTextView(translationsActivity, R.id.empty_deck_message);
        assertEquals(View.GONE, welcomeMessage.getVisibility());
    }

    private Activity createEmptyTranslationsActivity() {
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, EMPTY_DECK_ID, DEFAULT_LONG, false, DEFUALT_ISO_CODE);
        return createActivityWithDeck(deck);
    }

    private Activity createActivityWithDeck(Deck deck) {
        Intent intent = new Intent();
        intent.putExtra("Deck", deck);
        initializeEmptyDeckMockDbManager();
        return Robolectric.buildActivity(TranslationsActivity.class).withIntent(intent).create().get();
    }

    private Activity createLockedDeckTranslationsActivity() {
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG, true, DEFUALT_ISO_CODE);
        return createActivityWithDeck(deck);
    }

    private void initializeEmptyDeckMockDbManager() {
        Dictionary[] dictionaries = new Dictionary[1];
        dictionaries[0] = new Dictionary(NO_ISO_CODE, DICTIONARY_TEST_LABEL, new Translation[0], DEFAULT_LONG,
                EMPTY_DECK_ID);
        when(dbManagerMock.getAllDictionariesForDeck(EMPTY_DECK_ID)).thenReturn(dictionaries);
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
        View translationsListItem = listItemWithNoTranslatedTextOrAudio();

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getText().toString(), is("Add " + DICTIONARY_TEST_LABEL + " translation"));
    }

    @Test
    public void shouldHaveCorrectTextFormattingWhenTranslatedTextIsEmpty() {
        int disabledTextColor = -7960954;
        View translationsListItem = listItemWithNoTranslatedTextOrAudio();

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getTextSize(), is(18f));
        assertThat(translatedText.getCurrentTextColor(), is(disabledTextColor));
    }

    @Test
    public void shouldStartGetStartedActivityWhenAddTranslationButtonIsClicked() {
        click(translationsActivity, R.id.add_translation_button);
        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        assertEquals(GetStartedActivity.class.getCanonicalName(), nextStartedActivity.getComponent().getClassName());
    }

    @Test
    public void onClick_shouldStartEnterSourcePhraseActivityWhenEditLayoutIsClicked() {
        View translationsListItem = firstTranslationCardInListView();
        translationsListItem.findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        assertEquals(EnterSourcePhraseActivity.class.getCanonicalName(), nextStartedActivity.getComponent().getClassName());
    }

    @Test
    public void shouldPassCorrectTranslationWhenEditCardIsClicked() {
        firstTranslationCardInListView().findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        AddNewTranslationContext context = (AddNewTranslationContext) nextStartedActivity.getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(3, context.getNewTranslations().size());
        assertEquals(translation, context.getNewTranslations().get(0).getTranslation());
        assertEquals(translation, context.getNewTranslations().get(1).getTranslation());
        assertEquals(translation, context.getNewTranslations().get(2).getTranslation());
    }

    @Test
    public void shouldPassCorrectDictionariesWhenEditCardIsClicked() {
        View translationListItem = firstTranslationCardInListView();

        translationListItem.findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        AddNewTranslationContext context = (AddNewTranslationContext) nextStartedActivity.getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(dictionaries[0], context.getNewTranslations().get(0).getDictionary());
        assertEquals(dictionaries[1], context.getNewTranslations().get(1).getDictionary());
        assertEquals(dictionaries[2], context.getNewTranslations().get(2).getDictionary());
    }

    @Test
    public void shouldPassCorrectDeckIdWhenEditCardIsClicked() {
        View translationListItem = firstTranslationCardInListView();

        translationListItem.findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        Deck deck = (Deck) nextStartedActivity.getSerializableExtra(INTENT_KEY_DECK);
        assertEquals(this.deck, deck);
    }

    @Test
    public void onClick_shouldShowDeleteConfirmationDialogWhenDeleteLayoutIsClicked(){
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_card_delete).performClick();

        ShadowAlertDialog shadowAlertDialog = shadowOf(ShadowAlertDialog.getLatestAlertDialog());
        assertThat(shadowAlertDialog.getMessage().toString(), is("Are you sure you want to delete this translation card from all languages?"));}

    @Test
    public void onClick_shouldDeleteCorrectTranslationsWhenDeleteButtonIsClicked() {
        View translationsListItem = firstTranslationCardInListView();

        translationsListItem.findViewById(R.id.translation_card_delete).performClick();

        ShadowAlertDialog.getLatestAlertDialog().getButton(AlertDialog.BUTTON_POSITIVE).performClick();
        verify(dbManagerMock, times(3)).deleteTranslation(translation.getDbId());
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

        assertThat(tabContainer.getChildCount(), is(3));

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

    @Test
    public void shouldSetEditFlagInContextWhenEditButtonIsClicked() {
        firstTranslationCardInListView().findViewById(R.id.translation_card_edit).performClick();

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        AddNewTranslationContext context = (AddNewTranslationContext) nextStartedActivity.getSerializableExtra(CONTEXT_INTENT_KEY);
        assertTrue(context.isEdit());
    }

    @Test
    public void shouldNotSetEditFlagInContextWhenCreateNewTranslationButtonIsClicked(){
        click(translationsActivity, R.id.add_translation_button);

        Intent nextStartedActivity = shadowOf(translationsActivity).getNextStartedActivity();
        AddNewTranslationContext context = (AddNewTranslationContext) nextStartedActivity.getSerializableExtra(CONTEXT_INTENT_KEY);
        assertFalse(context.isEdit());
    }

    @Test
    public void shouldDisplayGrayedOutCardWhenNoAudioHasBeenRecorded(){
        View translationsListItem = listItemWithNoTranslatedTextOrAudio();
        TextView translationText = (TextView)translationsListItem.findViewById(R.id.origin_translation_text);
        assertEquals(getColor(translationsActivity, R.color.textDisabled), translationText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayNumberOfCardsWithNoAudioInNoAudioToggleText(){
        TextView noAudioText = findTextView(translationsActivity, R.id.no_audio_toggle_text);
        assertEquals("Hide 2 cards that don't have audio in this language", noAudioText.getText().toString());
    }

    private View firstTranslationCardInListView() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);
        return translationsList.getAdapter().getView(1, null, translationsList);
    }

    private View listItemWithNoTranslatedTextOrAudio() {
        ListView translationsList = (ListView) translationsActivity.findViewById(
                R.id.translations_list);
        return translationsList.getAdapter().getView(2, null, translationsList);
    }
}