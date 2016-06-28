package org.mercycorps.translationcards.activity.translations;

import android.content.Intent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.TranslationService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


public class CardEditClickListenerTest {

    // Dependencies
    @Mock TranslationsActivity translationsActivity;
    @Mock TranslationService translationService;
    @Mock DictionaryService dictionaryService;
    @Mock DeckService deckService;
    @Mock Intent intent;

    // Stubs
    @Mock View view;
    @Mock AddNewTranslationContext addNewTranslationContext;
    @Mock Translation firstTranslation;
    @Mock Translation secondTranslation;
    @Mock Dictionary firstDictionary;
    @Mock Dictionary secondDictionary;
    @Mock Deck deck;

    CardEditClickListener cardEditClickListener;

    @Before
    public void setup() {
        initMocks(this);

        String FIRST_TRANSLATION = "first translation";
        when(firstTranslation.getLabel()).thenReturn(FIRST_TRANSLATION);
        when(firstDictionary.getTranslationBySourcePhrase(FIRST_TRANSLATION)).thenReturn(firstTranslation);

        String SECOND_TRANSLATION = "second translation";
        when(secondTranslation.getLabel()).thenReturn(SECOND_TRANSLATION);
        when(secondDictionary.getTranslationBySourcePhrase(FIRST_TRANSLATION)).thenReturn(secondTranslation);

        when(dictionaryService.getDictionariesForCurrentDeck()).thenReturn(Arrays.asList(firstDictionary, secondDictionary));

        when(deckService.currentDeck()).thenReturn(deck);

        cardEditClickListener = new CardEditClickListener(
                translationsActivity,
                firstTranslation,
                intent,
                dictionaryService,
                deckService);
    }

    @Test
    public void shouldStartNextActivity() {
        cardEditClickListener.onClick(view);

        verify(translationsActivity).startActivity(intent);
    }

    @Test
    public void shouldUseCorrectKeyWhenPassingContextToIntent() {
        cardEditClickListener.onClick(view);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(intent, times(2)).putExtra(argumentCaptor.capture(), any(AddNewTranslationContext.class));

        assertEquals(AddTranslationActivity.CONTEXT_INTENT_KEY, getKeyPassedToFirstMethodCall(argumentCaptor));
    }

    @Test
    public void shouldUseCorrectKeyWhenPassingDeckToIntent() {
        cardEditClickListener.onClick(view);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(intent, times(2)).putExtra(argumentCaptor.capture(), any(AddNewTranslationContext.class));

        assertEquals(TranslationsActivity.INTENT_KEY_DECK, getArgumentToSecondMethodCall(argumentCaptor));
    }

    @Test
    public void shouldPassContextWithRightNumberOfNewTranslations() {
        cardEditClickListener.onClick(view);

        ArgumentCaptor<AddNewTranslationContext> argumentCaptor = ArgumentCaptor.forClass(AddNewTranslationContext.class);
        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());

        List<NewTranslation> newTranslations = getContextPassedToFirstMethodCall(argumentCaptor).getNewTranslations();
        assertEquals(2, newTranslations.size());
    }

    @Test
    public void shouldPassContextWithEditFlag() {
        cardEditClickListener.onClick(view);

        ArgumentCaptor<AddNewTranslationContext> argumentCaptor = ArgumentCaptor.forClass(AddNewTranslationContext.class);
        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());

        for(NewTranslation newTranslation : getContextPassedToFirstMethodCall(argumentCaptor).getNewTranslations()) {
            assertEquals(TranslationsActivity.IS_EDIT, (boolean)newTranslation.isEdit());
        }
    }

    @Test
    public void shouldPassContextWithCorrectTranslations() {
        cardEditClickListener.onClick(view);

        ArgumentCaptor<AddNewTranslationContext> argumentCaptor = ArgumentCaptor.forClass(AddNewTranslationContext.class);
        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());

        List<NewTranslation> newTranslations = getContextPassedToFirstMethodCall(argumentCaptor).getNewTranslations();
        assertEquals(firstTranslation, newTranslations.get(0).getTranslation());
        assertEquals(secondTranslation, newTranslations.get(1).getTranslation());
    }

    @Test
    public void shouldPassContextWithCorrectDictionaries() {
        cardEditClickListener.onClick(view);

        ArgumentCaptor<AddNewTranslationContext> argumentCaptor = ArgumentCaptor.forClass(AddNewTranslationContext.class);
        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());

        List<NewTranslation> newTranslations = getContextPassedToFirstMethodCall(argumentCaptor).getNewTranslations();
        assertEquals(firstDictionary, newTranslations.get(0).getDictionary());
        assertEquals(secondDictionary, newTranslations.get(1).getDictionary());
    }

    @Test
    public void shouldPassCorrectDeck() {
        cardEditClickListener.onClick(view);

        ArgumentCaptor<Deck> argumentCaptor = ArgumentCaptor.forClass(Deck.class);
        verify(intent, times(2)).putExtra(any(String.class), argumentCaptor.capture());

        assertEquals(deck, getArgumentToSecondMethodCall(argumentCaptor));
    }

    private String getKeyPassedToFirstMethodCall(ArgumentCaptor<String> argumentCaptor) {
        return argumentCaptor.getAllValues().get(0);
    }

    private AddNewTranslationContext getContextPassedToFirstMethodCall(ArgumentCaptor<AddNewTranslationContext> argumentCaptor) {
        return argumentCaptor.getAllValues().get(0);
    }

    private Object getArgumentToSecondMethodCall(ArgumentCaptor argumentCaptor) {
        return argumentCaptor.getAllValues().get(1);
    }
}