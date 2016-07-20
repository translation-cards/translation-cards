package org.mercycorps.translationcards.activity.translations;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.TranslationService;
import org.mercycorps.translationcards.view.TranslationCardItem;
import org.mockito.ArgumentCaptor;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by abeltamrat on 7/19/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class CardListAdapterTest {
    public static final String TRANSLATION_LABEL = "Test Translation";
    private CardListAdapter cardListAdapter;
    private Activity activity;
    private List<Translation> translations;
    private TranslationService mockTranslationService;
    private Dictionary defaultDictionary;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(Activity.class).create().get();
        translations = new ArrayList<>();
        translations.add(new Translation(TRANSLATION_LABEL, false, "", 0L, "Translated Text"));

        DictionaryService mockDictionaryService = mock(DictionaryService.class);
        DeckService mockDeckService = ((TestMainApplication) activity.getApplication()).getDeckService();
        mockTranslationService = ((TestMainApplication) activity.getApplication()).getTranslationService();
        defaultDictionary = new Dictionary("eng", "English", new Translation[0], 0, 0);
        when(mockDictionaryService.currentDictionary()).thenReturn(defaultDictionary);
        List<Dictionary> dictionaries = new ArrayList<>();
        dictionaries.add(defaultDictionary);
        when(mockDictionaryService.getDictionariesForCurrentDeck()).thenReturn(dictionaries);

        Deck basicDeck = new Deck("Test Deck", "", "1", 1, false, new Language("eng", "Langauge"));
        when(mockDeckService.currentDeck()).thenReturn(basicDeck);

        cardListAdapter = new CardListAdapter(
                activity.getApplicationContext(), 0, 0,
                translations,
                mockTranslationService,
                mockDictionaryService,
                mockDeckService);

    }

    @Test
    public void shouldCreateTranslationCardItemIfNoExistingView() {
        View resultingView = cardListAdapter.getView(0, null, null);

        assertEquals(TranslationCardItem.class, resultingView.getClass());
    }

    @Test
    public void shouldRecycleTranslationCardItemView() {
        TranslationCardItem translationCardItem = new TranslationCardItem(activity.getApplicationContext());
        TranslationCardItem resultingView = (TranslationCardItem) cardListAdapter.getView(0, translationCardItem, null);

        assertEquals(translations.get(0), resultingView.getTranslation());
    }

    @Test
    public void shouldSetMessageOfAlertDialog() throws Exception {
        View resultingView = cardListAdapter.getView(0, null, null);
        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        resultingView.findViewById(R.id.translation_card_delete).performClick();

        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        CharSequence actual = ((TextView) dialog.findViewById(android.R.id.message)).getText();

        String expected = RuntimeEnvironment.application.getString(R.string.delete_dialog_message);
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteWhenAlertDialogDeleteButtonClicked() {
        View resultingView = cardListAdapter.getView(0, null, null);
        resultingView.findViewById(R.id.translation_card_delete).performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick();

        verify(mockTranslationService).deleteTranslation(TRANSLATION_LABEL);
    }

    @Test
    public void shouldShowAlertDialogWhenDeleteClicked() throws Exception {
        View resultingView = cardListAdapter.getView(0, null, null);
        resultingView.findViewById(R.id.translation_card_delete).performClick();
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        assertTrue(dialog.isShowing());
    }

    @Test
    public void shouldStartNextActivityWhenEditTriggered() {
        View resultingView = cardListAdapter.getView(0, null, null);
        resultingView.findViewById(R.id.translation_card_edit).performClick();

        String activityClass = shadowOf(activity).getNextStartedActivity().getComponent().getClassName();
        assertEquals(EnterSourcePhraseActivity.class.getName(), activityClass);
    }

    @Test
    public void shouldUseCorrectKeyWhenPassingContextToIntentWhenEditTriggered() {

        View resultingView = cardListAdapter.getView(0, null, null);
        resultingView.findViewById(R.id.translation_card_edit).performClick();
        boolean hasExtra = shadowOf(shadowOf(activity).getNextStartedActivity()).hasExtra(AddTranslationActivity.CONTEXT_INTENT_KEY);
        assertTrue(hasExtra);
    }

    @Test
    public void shouldUseCorrectKeyWhenPassingDeckToIntentWhenEditTriggered() {
        View resultingView = cardListAdapter.getView(0, null, null);
        resultingView.findViewById(R.id.translation_card_edit).performClick();
        boolean hasExtra = shadowOf(shadowOf(activity).getNextStartedActivity()).hasExtra(DeckService.INTENT_KEY_DECK);
        assertTrue(hasExtra);
    }

    @Test
    public void shouldPassContextWithRightNumberOfNewTranslationsWhenEditTriggered() {

        View resultingView = cardListAdapter.getView(0, null, null);
        resultingView.findViewById(R.id.translation_card_edit).performClick();
        AddNewTranslationContext context = (AddNewTranslationContext)shadowOf(shadowOf(activity).getNextStartedActivity()).getExtras().get(AddTranslationActivity.CONTEXT_INTENT_KEY);

        assertEquals(1, context.getNewTranslations().size());
    }

    @Test
    public void shouldPassContextWithEditFlagWhenEditTriggered() {
        View resultingView = cardListAdapter.getView(0, null, null);
        resultingView.findViewById(R.id.translation_card_edit).performClick();
        AddNewTranslationContext context = (AddNewTranslationContext)shadowOf(shadowOf(activity).getNextStartedActivity()).getExtras().get(AddTranslationActivity.CONTEXT_INTENT_KEY);

        for(NewTranslation newTranslation : context.getNewTranslations()) {
            assertEquals(TranslationsActivity.IS_EDIT, (boolean)newTranslation.isEdit());
        }
    }

//    @Test
//    public void shouldPassContextWithCorrectTranslations() {
//        cardEditClickListener.onClick(view);
//
//        ArgumentCaptor<AddNewTranslationContext> argumentCaptor = ArgumentCaptor.forClass(AddNewTranslationContext.class);
//        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());
//
//        List<NewTranslation> newTranslations = getContextPassedToFirstMethodCall(argumentCaptor).getNewTranslations();
//        assertEquals(firstTranslation, newTranslations.get(0).getTranslation());
//        assertEquals(secondTranslation, newTranslations.get(1).getTranslation());
//    }
//
//    @Test
//    public void shouldPassContextWithCorrectDictionaries() {
//        cardEditClickListener.onClick(view);
//
//        ArgumentCaptor<AddNewTranslationContext> argumentCaptor = ArgumentCaptor.forClass(AddNewTranslationContext.class);
//        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());
//
//        List<NewTranslation> newTranslations = getContextPassedToFirstMethodCall(argumentCaptor).getNewTranslations();
//        assertEquals(firstDictionary, newTranslations.get(0).getDictionary());
//        assertEquals(secondDictionary, newTranslations.get(1).getDictionary());
//    }
//
//    @Test
//    public void shouldPassCorrectDeck() {
//        cardEditClickListener.onClick(view);
//
//        ArgumentCaptor<Deck> argumentCaptor = ArgumentCaptor.forClass(Deck.class);
//        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());
//
//        assertEquals(deck, getArgumentToSecondMethodCall(argumentCaptor));
//    }
}