package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NewDeckContext implements Serializable{
    private Deck deck;
    private List<Dictionary> dictionaries;

    public NewDeckContext(){
        this(new Deck());
    }

    public NewDeckContext(Deck deck) {
        this.deck = deck;
        this.dictionaries = new ArrayList<>();
    }

    public void setDeckTitle(String deckTitle) {
        deck.setLabel(deckTitle);
    }

    public void addLanguage(String language) {
        dictionaries.add(new Dictionary(language));
    }

    public void save() {

    }

    public String getDeckLabel() {
        return deck.getLabel();
    }
}
