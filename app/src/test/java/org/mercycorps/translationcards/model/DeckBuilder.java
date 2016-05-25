package org.mercycorps.translationcards.model;

import org.mercycorps.translationcards.data.Deck;

import java.util.Date;

/**
 * Quickly and descriptively configure decks used for testing.
 */
public class DeckBuilder {
    private Deck deck;

    public DeckBuilder() {
        deck = new Deck("Test Name", "Test Author", "TestExternalId", new Date().getTime(), false, "TestSourceLanguageIso");
    }

    public DeckBuilder withLabel(String label) {
        deck.setLabel(label);
        return this;
    }

    public DeckBuilder withAuthor(String author) {
        deck.setAuthor(author);
        return this;
    }

    public Deck value() {
        return deck;
    }
}
