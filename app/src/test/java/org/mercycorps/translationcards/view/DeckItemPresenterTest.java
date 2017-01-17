package org.mercycorps.translationcards.view;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.view.DeckItemPresenter.DeckItemView;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeckItemPresenterTest {

    private DictionaryService dictionaryService;
    private DeckItemPresenter presenter;
    private DeckService deckService;
    private DeckItemView view;
    private Deck deck;

    @Before
    public void setUp() throws Exception {
        view = mock(DeckItemView.class);
        dictionaryService = mock(DictionaryService.class);
        deckService = mock(DeckService.class);
        presenter = new DeckItemPresenter(view, dictionaryService, deckService);
        deck = mock(Deck.class);
    }

    @Test
    public void shouldSetCurrentDictionaryToDefaultIndexWhenDeckIsClicked() {
        presenter.deckClicked(deck);

        verify(dictionaryService).setCurrentDictionary(0);
    }

    @Test
    public void shouldSetCurrentDeckWhenDeckIsClicked() {
        presenter.deckClicked(deck);

        verify(deckService).setCurrentDeck(deck);
    }

    @Test
    public void shouldStartTranslationsActivityWhenDeckIsClicked() {
        presenter.deckClicked(deck);

        verify(view).launchTranslationsActivity();
    }

    @Test
    public void shouldSetDeckTitle() {
        String deckTitle = "A Title";
        when(deck.getTitle()).thenReturn(deckTitle);

        presenter.setDeck(deck);

        verify(view).setDeckTitle(deckTitle);
    }

    @Test
    public void shouldSetDeckInformation() {
        String deckInformation = "Information";
        when(deck.getDeckInformation()).thenReturn(deckInformation);

        presenter.setDeck(deck);

        verify(view).setDeckInformation(deckInformation);
    }

    @Test
    public void shouldSetDeckOriginLanguage() {
        when(deck.getSourceLanguageName()).thenReturn("a source language");

        presenter.setDeck(deck);

        verify(view).setDeckSourceLanguage("A SOURCE LANGUAGE");
    }

    @Test
    public void shouldShowDeckIconWhenDeckIsLocked() {
        when(deck.isLocked()).thenReturn(true);

        presenter.setDeck(deck);

        verify(view).displayLockIcon();
        verify(view, times(0)).hideLockIcon();
    }

    @Test
    public void shouldNotShowDeckIconWhenDeckIsNotLocked() {
        when(deck.isLocked()).thenReturn(false);

        presenter.setDeck(deck);

        verify(view).hideLockIcon();
        verify(view, times(0)).displayLockIcon();
    }

    @Test
    public void shouldSetDestinationLanguages() {
        String destinationLanguages = "Destination Languages";
        when(deck.getDestinationLanguagesForDisplay()).thenReturn(destinationLanguages);

        presenter.setDeck(deck);

        verify(view).setDeckDestinationLanguages(destinationLanguages);
    }

    @Test
    public void shouldHideDeckMenuWhenDeckIsSet() throws Exception {
        presenter.setDeck(deck);

        verify(view).hideDeckMenu();
    }
}