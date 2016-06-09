package org.mercycorps.translationcards.data;

import org.mercycorps.translationcards.MainApplication;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Contains information about a collection of phrases in one or more languages.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Deck implements Serializable {

    private String label;
    private String author;
    private String externalId;
    private long dbId;
    private long timestamp;
    private boolean locked;
    private String sourceLanguageIso;
    // The dictionaries list is lazily initialized.
    private Dictionary[] dictionaries;

    public Deck(String label, String author, String externalId, long dbId, long timestamp,
                boolean locked, String sourceLanguageIso) {
        this.label = label;
        this.author = author;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
        this.locked = locked;
        this.sourceLanguageIso = sourceLanguageIso;
        dictionaries = null;
    }

    public Deck(String label, String author, String externalId, long timestamp, boolean locked,
                String sourceLanguageIso) {
        this(label, author, externalId, -1, timestamp, locked, sourceLanguageIso);
    }

    public Deck() {

    }

    public String getLabel() {
        return label;
    }

    public String getAuthor() {
        return author;
    }

    public String getExternalId() {
        return externalId;
    }

    public long getDbId() {
        return dbId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCreationDateString() {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        return dateFormat.format(date);
    }

    public String getDeckInformation() {
        return getAuthor() + ", " + getCreationDateString();
    }

    public boolean isLocked() {
        return locked;
    }

    public String getSourceLanguageIso() {
        return sourceLanguageIso;
    }

    public Dictionary[] getDictionaries() {
        if (dictionaries == null) {
            dictionaries = ((MainApplication) MainApplication.getContextFromMainApp()).getDbManager().getAllDictionariesForDeck(dbId);
        }
        return dictionaries;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void delete() {
        ((MainApplication) MainApplication.getContextFromMainApp()).getDbManager().deleteDeck(dbId);
    }

    public void setSourceLanguageIso(String sourceLanguageIso){
        this.sourceLanguageIso = sourceLanguageIso;

    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
