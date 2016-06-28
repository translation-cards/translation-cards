package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.model.Dictionary;

import java.util.List;

public class DeckService {

    private DeckRepository deckRepository;
    private List<Deck> decks;
    private Deck currentDeck;
    private LanguageService languageService;

    public DeckService(LanguageService languageService, List<Deck> decks, DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
        this.languageService = languageService;
        this.decks = decks;
        currentDeck = this.decks.get(0);
    }


    public Deck currentDeck() {
        return currentDeck;
    }

    public void save(Deck deck, String languages) {
        Long deckID = deckRepository.addDeck(
                deck.getTitle(),
                deck.getAuthor(),
                deck.getTimestamp(),
                deck.getExternalId(),
                "", deck.isLocked(),
                deck.getSourceLanguageIso());
        saveDictionaries(deckID, languages);
    }

    private void saveDictionaries(Long deckId, String languages) {
        String[] languagesList = languages.split(",");
        Dictionary dictionary;
        Integer itemIndex = 0;
        for (String language : languagesList) {
            String isoCode = languageService.getIsoForLanguage(language);
            dictionary = new Dictionary(isoCode, language, null, -1, deckId);
            dictionary.save(itemIndex);
            itemIndex ++;
        }
    }

    public void delete(Deck deck) {
        deckRepository.deleteDeck(deck.getDbId());
    }

    public void setCurrentDeck(Deck deck) {
        currentDeck = deck;
    }
}
