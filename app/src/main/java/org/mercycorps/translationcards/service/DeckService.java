package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.List;

public class DeckService {

    private DbManager dbManager;
    private List<Deck> decks;
    private Deck currentDeck;
    private LanguageService languageService;

    public DeckService(DbManager dbManager, LanguageService languageService, List<Deck> decks) {
        this.dbManager = dbManager;
        this.languageService = languageService;
        this.decks = decks;
        currentDeck = this.decks.get(0);
    }


    public Deck currentDeck() {
        return currentDeck;
    }

    public void save(Deck deck, String languages) {
        Long deckID = dbManager.addDeck(
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
        dbManager.deleteDeck(deck.getDbId());
    }

    public void setCurrentDeck(Deck deck) {
        currentDeck = deck;
    }
}
