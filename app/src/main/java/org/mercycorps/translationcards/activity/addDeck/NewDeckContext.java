package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Language;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

public class NewDeckContext implements Serializable {
    private static final String NO_VALUE = "";
    private static final String ENGLISH_ISO = "en";

    private Deck deck;
    private boolean isEdit;
    private final LinkedHashSet<String> destinationLanguages;

    public NewDeckContext() {
        this(new Deck(NO_VALUE, "", NO_VALUE, new Date().getTime(), false, ENGLISH_ISO), false);
    }

    public NewDeckContext(Deck deck, boolean isEdit) {
        this.deck = deck;
        this.isEdit = isEdit;
        destinationLanguages = new LinkedHashSet<>();
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
        if (destinationLanguages.size() == 0) {
            return "";
        }
        Iterator<String> iterator = destinationLanguages.iterator();
        StringBuilder sb = new StringBuilder();
        sb.append(iterator.next());
        while (iterator.hasNext()) {
            sb.append(", ");
            sb.append(iterator.next());
        }
        return sb.toString();
    }

    public String getAuthor() {
        return deck.getAuthor();
    }

    public void setAuthor(String author) {
        deck.setAuthor(author);
    }

    public String getSourceLanguage() {
        return deck.getSourceLanguageName();
    }

    public Deck getDeck() {
        return deck;
    }

    public void addDestinationLanguage(String language) {
        destinationLanguages.add(language);
    }

    public HashSet<String> getDestinationLanguages() {
        return destinationLanguages;
    }

    public void addDestinationLanguages(List<String> languages) {
        destinationLanguages.addAll(languages);
    }
}
