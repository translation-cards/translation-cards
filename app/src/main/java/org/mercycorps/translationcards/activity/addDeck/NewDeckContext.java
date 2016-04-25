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
    private String languagesInput;

    public NewDeckContext(){
        this(new Deck(NO_VALUE, PUBLISHER, NO_VALUE, new Date().getTime(), false, ENGLISH_ISO), NO_VALUE);
    }

    public NewDeckContext(Deck deck, String languagesInput) {
        this.deck = deck;
        this.languagesInput = languagesInput;
    }

    public void setDeckTitle(String deckTitle) {
        deck.setLabel(deckTitle);
    }

    public void save() {
        Long deckId = deck.save();
        saveDictionaries(deckId);
    }

    private void saveDictionaries(Long deckId) {
        String[] languagesList = languagesInput.split(",");
        Dictionary dictionary;
        Integer itemIndex = 0;
        for (String language : languagesList) {
            dictionary = new Dictionary(language);
            dictionary.setDeckId(deckId);
            dictionary.save(itemIndex);
            itemIndex ++;
        }
    }

    public String getDeckLabel() {
        return deck.getLabel();
    }

    public String getLanguagesInput() {
        return languagesInput;
    }

    public void updateLanguagesInput(String input) {
        languagesInput = input;
    }
}
