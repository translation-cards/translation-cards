package org.mercycorps.translationcards.activity.addDeck;

import android.os.Parcel;
import android.os.Parcelable;

import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Language;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class NewDeckContext implements Parcelable {
    private static final String ENGLISH_NAME = "English";
    private static final String NO_VALUE = "";
    private static final String ENGLISH_ISO = "en";

    private Deck deck;
    private final LinkedHashSet<String> destinationLanguages;

    public NewDeckContext() {
        this(new Deck(NO_VALUE, "", NO_VALUE, new Date().getTime(), false, new Language(ENGLISH_ISO, ENGLISH_NAME)));
    }

    public NewDeckContext(Deck deck) {
        this.deck = deck;
        destinationLanguages = new LinkedHashSet<>();
    }

    protected NewDeckContext(Parcel in) {
        deck = in.readParcelable(Deck.class.getClassLoader());
        destinationLanguages = new LinkedHashSet<>();
        String[] languagesArray = in.createStringArray();
        destinationLanguages.addAll(Arrays.asList(languagesArray));
    }

    public static final Creator<NewDeckContext> CREATOR = new Creator<NewDeckContext>() {
        @Override
        public NewDeckContext createFromParcel(Parcel in) {
            return new NewDeckContext(in);
        }

        @Override
        public NewDeckContext[] newArray(int size) {
            return new NewDeckContext[size];
        }
    };

    public void setSourceLanguage(Language language) {
        deck.setSourceLanguage(language);
    }

    public void setDeckTitle(String deckTitle) {
        deck.setTitle(deckTitle);
    }

    public String getDeckTitle() {
        return deck.getTitle();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(deck, flags);
        parcel.writeStringArray(destinationLanguages.toArray(new String[destinationLanguages.size()]));
    }
}
