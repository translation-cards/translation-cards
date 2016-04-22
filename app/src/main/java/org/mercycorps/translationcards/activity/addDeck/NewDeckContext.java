package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewDeckContext implements Serializable {
    private static final String NO_VALUE = "";
    private static final String ENGLISH_ISO = "en";
    private static final String PUBLISHER = "Self";
    private Deck deck;
    private List<Dictionary> dictionaries;

    public NewDeckContext(){
        this(new Deck(NO_VALUE, PUBLISHER, NO_VALUE, new Date().getTime(), false, ENGLISH_ISO), new ArrayList<Dictionary>());
    }

    public NewDeckContext(Deck deck, List<Dictionary> dictionaries) {
        this.deck = deck;
        this.dictionaries = dictionaries;
    }

    public void setDeckTitle(String deckTitle) {
        deck.setLabel(deckTitle);
    }

    public void addLanguage(String language) {
        dictionaries.add(new Dictionary(language));
    }

    public void save() {
        Long deckId = deck.save();
        Integer itemIndex = 0;
        for (Dictionary dictionary : dictionaries) {
            dictionary.setDeckId(deckId);
            dictionary.save(itemIndex);
            itemIndex ++;
        }
    }

    public String getDeckLabel() {
        return deck.getLabel();
    }
}
