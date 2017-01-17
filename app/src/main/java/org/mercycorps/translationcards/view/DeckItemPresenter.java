package org.mercycorps.translationcards.view;

import android.support.annotation.NonNull;

import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;

public class DeckItemPresenter {
    private static final int DEFAULT_DICTIONARY = 0;
    private DeckItemView view;
    private DictionaryService dictionaryService;
    private DeckService deckService;

    public DeckItemPresenter(DeckItemView view, DictionaryService dictionaryService, DeckService deckService) {
        this.view = view;
        this.dictionaryService = dictionaryService;
        this.deckService = deckService;
    }

    public void deckClicked(Deck deck) {
        deckService.setCurrentDeck(deck);
        dictionaryService.setCurrentDictionary(DEFAULT_DICTIONARY);
        view.launchTranslationsActivity();
    }

    public void setDeck(Deck deck) {
        view.setDeckTitle(deck.getTitle());
        view.setDeckInformation(deck.getDeckInformation());
        view.setDeckSourceLanguage(capitalize(deck.getSourceLanguageName()));
        if (deck.isLocked()) {
            view.displayLockIcon();
        } else {
            view.hideLockIcon();
        }
        view.setDeckDestinationLanguages(deck.getDestinationLanguagesForDisplay());
        view.hideDeckMenu();

    }

    @NonNull
    private String capitalize(String sourceLanguageName) {
        if (sourceLanguageName != null) {
            return sourceLanguageName.toUpperCase();
        }
        return "";
    }

    public interface DeckItemView {
        void launchTranslationsActivity();
        void setDeckTitle(String deckTitle);
        void setDeckInformation(String deckInformation);
        void setDeckSourceLanguage(String sourceLanguage);
        void displayLockIcon();
        void hideLockIcon();
        void setDeckDestinationLanguages(String destinationLanguages);
        void hideDeckMenu();
    }
}
