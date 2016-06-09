package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.Arrays;
import java.util.List;

public class DeckService {

    private DbManager dbManager;
    private List<Deck> decks;
    private Deck currentDeck;

    public DeckService(DbManager dbManager) {
        this.dbManager = dbManager;
        decks = Arrays.asList(this.dbManager.getAllDecks());
        currentDeck = decks.get(0);
    }


    public Deck currentDeck() {
        return currentDeck;
    }

    public void save(Deck deck, String languages) {
        Long deckID = dbManager.addDeck(
                deck.getLabel(),
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
            dictionary = new Dictionary(language);
            dictionary.setDeckId(deckId);
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
