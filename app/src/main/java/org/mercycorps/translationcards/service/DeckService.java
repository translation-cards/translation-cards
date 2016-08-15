package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;

public class DeckService {

    public static final String INTENT_KEY_DECK = "Deck";

    private Deck currentDeck;

    public DeckService(DeckRepository deckRepository) {
        currentDeck = deckRepository.getAllDecks()[0];
    }

    public Deck currentDeck() {
        return currentDeck;
    }

    public void setCurrentDeck(Deck deck) {
        currentDeck = deck;
    }
}
