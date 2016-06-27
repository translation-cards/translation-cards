package org.mercycorps.translationcards.data;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.service.LanguageService;

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

    private String title;
    private String author;
    private String externalId;
    private long dbId;
    private long timestamp;
    private boolean locked;
    private Language sourceLanguage;
    // The dictionaries list is lazily initialized.
    private Dictionary[] dictionaries;

    public Deck(String title, String author, String externalId, long dbId, long timestamp,
                boolean locked, String sourceLanguageIso) {
        this.title = title;
        this.author = author;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
        this.locked = locked;
        this.sourceLanguage = new LanguageRepository().withISO(sourceLanguageIso);
        dictionaries = null;
    }

    public Deck(String title, String author, String externalId, long timestamp, boolean locked,
                String sourceLanguageIso) {
        this(title, author, externalId, -1, timestamp, locked, sourceLanguageIso);
    }

    public Deck() {

    }

    public String getTitle() {
        return title;
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
        return sourceLanguage.getIso();
    }
    public String getSourceLanguageName() {
        return sourceLanguage.getName();
    }

    public Dictionary[] getDictionaries() {
        if (dictionaries == null) {
            dictionaries = ((MainApplication) MainApplication.getContextFromMainApp()).getDbManager().getAllDictionariesForDeck(dbId);
        }
        return dictionaries;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void delete() {
        ((MainApplication) MainApplication.getContextFromMainApp()).getDeckRepository().deleteDeck(dbId);
    }

    public void setSourceLanguage(Language language){
        this.sourceLanguage = language;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

}
