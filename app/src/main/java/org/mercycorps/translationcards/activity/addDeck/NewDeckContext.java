package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Language;

import java.io.Serializable;
import java.util.Date;

public class NewDeckContext implements Serializable {
    private static final String NO_VALUE = "";
    private static final String ENGLISH_ISO = "eng";

    private Deck deck;
    private String languagesInput;
    private boolean isEdit;

    public NewDeckContext(){
        this(new Deck(NO_VALUE, "", NO_VALUE, new Date().getTime(), false, ENGLISH_ISO), NO_VALUE, false);
    }

    public NewDeckContext(Deck deck, String languagesInput, boolean isEdit) {
        this.deck = deck;
        this.languagesInput = languagesInput;
        this.isEdit = isEdit;
    }

    public void setSourceLanguage(Language language) {
        deck.setSourceLanguage(language);
    }

    public void setDeckTitle(String deckTitle) {
        deck.setTitle(deckTitle);
    }

    public String getDeckTitle() {
        return deck.getTitle();
    }

    public String getLanguagesInput() {
        return languagesInput;
    }

    public void updateLanguagesInput(String input) {
        languagesInput = input;
    }

    public String getAuthor() {
        return deck.getAuthor();
    }

    public void setAuthor(String author) {
        deck.setAuthor(author);
    }

    public String getSourceLanguage(){ return deck.getSourceLanguageName(); }

    public Deck getDeck() {
        return deck;
    }
}
