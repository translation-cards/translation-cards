package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;

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

    public Long save(Deck deck) {
        return dbManager.addDeck(
                deck.getLabel(),
                deck.getAuthor(),
                deck.getTimestamp(),
                deck.getExternalId(),
                "", deck.isLocked(),
                deck.getSourceLanguageIso());
    }

    public void delete(Deck deck) {
        dbManager.deleteDeck(deck.getDbId());
    }

    public void setCurrentDeck(Deck deck) {
        currentDeck = deck;
    }
}
