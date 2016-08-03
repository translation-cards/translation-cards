package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;

import java.util.List;
import java.util.Set;

public class DeckService {

    public static final String INTENT_KEY_DECK = "Deck";

    private DeckRepository deckRepository;
    private DictionaryRepository dictionaryRepository;
    private Deck currentDeck;
    private LanguageService languageService;

    public DeckService(LanguageService languageService, List<Deck> decks, DeckRepository deckRepository, DictionaryRepository dictionaryRepository) {
        this.deckRepository = deckRepository;
        this.languageService = languageService;
        this.dictionaryRepository = dictionaryRepository;
        currentDeck = decks.get(0);
    }


    public Deck currentDeck() {
        return currentDeck;
    }

    public void save(Deck deck, Set<String> languages) {
        Long deckID = deckRepository.addDeck(
                deck.getTitle(),
                deck.getAuthor(),
                deck.getTimestamp(),
                deck.getExternalId(),
                "", deck.isLocked(),
                deck.getSourceLanguageIso());
        saveDictionaries(deckID, languages);
    }

    private void saveDictionaries(Long deckId, Set<String> languages) {
        Integer itemIndex = 0;
        for (String language : languages) {
            String isoCode = languageService.getIsoForLanguage(language);
            dictionaryRepository.addDictionary(isoCode, language, itemIndex, deckId);
            itemIndex++;
        }
    }

    public void delete(Deck deck) {
        deckRepository.deleteDeck(deck.getDbId());
    }

    public void setCurrentDeck(Deck deck) {
        currentDeck = deck;
    }
}
