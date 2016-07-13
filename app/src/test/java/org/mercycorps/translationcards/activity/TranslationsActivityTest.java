package org.mercycorps.translationcards.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.activity.addTranslation.GetStartedActivity;
import org.mercycorps.translationcards.activity.translations.CardListAdapter;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.TranslationService;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ActivityController;

import java.util.Arrays;

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
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findView;
import static org.mockito.Matchers.anyInt;
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
    public static final String DICTIONARY_TEST_LABEL = "Testlabel";
    public static final String DICTIONARY_ARABIC_LABEL = "ARABIC";
    public static final String DICTIONARY_FARSI_LABEL = "FARSI";
    public static final String TRANSLATED_TEXT = "TranslatedText";
    public static final String TRANSLATION_LABEL = "TranslationLabel";
    public static final String DEFAULT_DECK_NAME = "Default";
    private static final String EMPTY_DECK_TITLE = "Let's make this useful";
    private static final int EMPTY_DECK_ID = 2;
    private static final String DEFAULT_ISO_CODE = "en";
    private static final String DEFAULT_LANGUAGE_NAME = "English";
    private static final String NO_ISO_CODE = "";
    private TranslationsActivity translationsActivity;
    private Translation translation;
    private Deck deck;
    ActivityController<TranslationsActivity> controller;
    private TranslationService translationService;
    private DictionaryService dictionaryService;
    private DeckService deckService;

    @Before
    public void setUp() {
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        translationService = application.getTranslationService();
        dictionaryService = application.getDictionaryService();
        deckService = application.getDeckService();

        initializeStubsAndMocks();

        controller = Robolectric.buildActivity(TranslationsActivity.class);
        Intent intent = new Intent();
        translationsActivity = controller.withIntent(intent).create().get();
    }

    @After
    public void teardown() {
        translationsActivity.finish();
        controller.pause().stop().destroy();
    }

    private void initializeStubsAndMocks() {
        deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG, false, new Language(DEFAULT_ISO_CODE, DEFAULT_LANGUAGE_NAME));
        when(deckService.currentDeck()).thenReturn(deck);

        Dictionary[] dictionaries = new Dictionary[3];
        translation = new Translation(TRANSLATION_LABEL, false, NO_VALUE, DEFAULT_LONG, TRANSLATED_TEXT);
        Translation nullTranslatedTextTranslation = new Translation(TRANSLATION_LABEL, false, "audio.mp3", DEFAULT_LONG, null);
        Translation[] translations = {translation, nullTranslatedTextTranslation};
        dictionaries[0] = new Dictionary(NO_ISO_CODE, DICTIONARY_TEST_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);
        dictionaries[1] = new Dictionary(NO_ISO_CODE, DICTIONARY_ARABIC_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);
        dictionaries[2] = new Dictionary(NO_ISO_CODE, DICTIONARY_FARSI_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);

        when(dictionaryService.currentDictionary()).thenReturn(dictionaries[0]);
        when(dictionaryService.getDictionariesForCurrentDeck()).thenReturn(Arrays.asList(dictionaries));
        when(translationService.cardIsExpanded(anyInt())).thenReturn(false);
        when(translationService.getCurrentTranslations()).thenReturn(Arrays.asList(translations));
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
        TextView header = findTextView(activity, R.id.translation_list_header);
        assertEquals(View.GONE, header.getVisibility());
    }

    @Test
    public void shouldDisplayHeaderWhenDeckIsPopulated() {
        TextView header = findTextView(translationsActivity, R.id.translation_list_header);
        assertEquals(View.VISIBLE, header.getVisibility());
    }

    @Test
    public void shouldNotShowWelcomeMessageWhenCardsArePresent() {
        TextView welcomeMessage = findTextView(translationsActivity, R.id.empty_deck_message);
        assertEquals(View.GONE, welcomeMessage.getVisibility());
    }

    private Activity createEmptyTranslationsActivity() {
        return createActivityWithDeck(deck);
    }

    private Activity createActivityWithDeck(Deck deck) {
        Intent intent = new Intent();
        intent.putExtra("Deck", deck);
        initializeStubsAndMocksForEmptyDeck();
        controller = Robolectric.buildActivity(TranslationsActivity.class);
        return controller.withIntent(intent).create().get();
    }

    private Activity createLockedDeckTranslationsActivity() {
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG, true, new Language(DEFAULT_ISO_CODE, DEFAULT_LANGUAGE_NAME));
        when(deckService.currentDeck()).thenReturn(deck);
        return createActivityWithDeck(deck);
    }

    private void initializeStubsAndMocksForEmptyDeck() {
        Dictionary[] dictionaries = new Dictionary[1];
        dictionaries[0] = new Dictionary(NO_ISO_CODE, DICTIONARY_TEST_LABEL, new Translation[0], DEFAULT_LONG,
                EMPTY_DECK_ID);
        when(dictionaryService.currentDictionary()).thenReturn(dictionaries[0]);
        when(dictionaryService.getDictionariesForCurrentDeck()).thenReturn(Arrays.asList(dictionaries));
    }

    @Test
    public void onCreate_shouldShowDeckNameInToolbar() {
        assertThat(translationsActivity.getSupportActionBar().getTitle().toString(), is(
                DEFAULT_DECK_NAME));
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
        View translationsListItem = listItemWithNoTranslatedTextAndWithAudio();

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);

        assertThat(translatedText.getText().toString(), is("Add " + DICTIONARY_TEST_LABEL + " translation"));
    }

    @Test
    public void shouldHaveCorrectTextFormattingWhenTranslatedTextIsEmpty() {
        int disabledTextColor = -7960954;
        View translationsListItem = listItemWithNoTranslatedTextAndWithAudio();

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
    public void shouldDisplayGrayedOutSourceTranslationWhenNoAudioHasBeenRecorded(){
        View translationsListItem = firstTranslationCardInListView();
        TextView translationText = (TextView)translationsListItem.findViewById(R.id.origin_translation_text);
        assertEquals(getColor(translationsActivity, R.color.textDisabled), translationText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayNoAudioIconWhenNoAudioHasBeenRecorded() {
        View translationsListItem = firstTranslationCardInListView();

        ImageView audioIcon = (ImageView)translationsListItem.findViewById(R.id.audio_icon);

        assertThat(shadowOf(audioIcon.getBackground()).getCreatedFromResId(), is(R.drawable.no_audio_40));
    }

    @Test
    public void shouldDisplayAudioIconWhenAudioHasBeenRecorded() {
        View translationsListItem = listItemWithNoTranslatedTextAndWithAudio();

        ImageView audioIcon = (ImageView)translationsListItem.findViewById(R.id.audio_icon);

        assertThat(shadowOf(audioIcon.getBackground()).getCreatedFromResId(), is(R.drawable.audio));
    }

    @Test
    @TargetApi(19)
    public void shouldDisplayGrayedOutCardWhenNoAudioHasBeenRecorded(){
        View translationsListItem = firstTranslationCardInListView();
        LinearLayout translationCardParent = (LinearLayout)translationsListItem.findViewById(R.id.translation_card_parent);

        LayerDrawable bgDrawable= (LayerDrawable)translationCardParent.getBackground();
        GradientDrawable background = (GradientDrawable)bgDrawable.findDrawableByLayerId(R.id.card_top_background);

        assertEquals(CardListAdapter.DISABLED_OPACITY, background.getAlpha());

    }


    private View firstTranslationCardInListView() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);
        return translationsList.getAdapter().getView(1, null, translationsList);
    }

    private View listItemWithNoTranslatedTextAndWithAudio() {
        ListView translationsList = (ListView) translationsActivity.findViewById(
                R.id.translations_list);
        return translationsList.getAdapter().getView(2, null, translationsList);
    }
}