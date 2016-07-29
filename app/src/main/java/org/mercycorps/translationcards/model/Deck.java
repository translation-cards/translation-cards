package org.mercycorps.translationcards.model;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.porting.JsonKeys;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
                boolean locked, Language sourceLanguage) {
        this.title = title;
        this.author = author;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
        this.locked = locked;
        this.sourceLanguage = sourceLanguage;
        dictionaries = null;
    }

    public Deck(String title, String author, String externalId, long timestamp, boolean locked,
                Language sourceLanguage) {
        this(title, author, externalId, -1, timestamp, locked, sourceLanguage);
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
            dictionaries = ((MainApplication) MainApplication.getContextFromMainApp()).getDictionaryRepository().getAllDictionariesForDeck(dbId);
        }
        return dictionaries;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSourceLanguage(Language language) {
        this.sourceLanguage = language;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public JSONObject toJSON(String exportedDeckName) throws JSONException {
        JSONObject json = new JSONObject();

        json.put(JsonKeys.DECK_LABEL, exportedDeckName);
        json.put(JsonKeys.PUBLISHER, author);
        if (externalId != null) {
            json.put(JsonKeys.EXTERNAL_ID, externalId);
        }
        json.put(JsonKeys.TIMESTAMP, timestamp);
        json.put(JsonKeys.SOURCE_LANGUAGE, getSourceLanguageIso());
        json.put(JsonKeys.LOCKED, locked);
        json.put(JsonKeys.DICTIONARIES, getDictionariesJSON());
        return json;
    }

    @NonNull
    private JSONArray getDictionariesJSON() throws JSONException {
        JSONArray dictionaryJSONArray = new JSONArray();
        for (Dictionary dictionary : getDictionaries()) {
            dictionaryJSONArray.put(dictionary.toJSON());
        }
        return dictionaryJSONArray;
    }

    public List<String> getAudioFilePaths() {
        List<String> allAudioFilePaths = new ArrayList<>();
        for (Dictionary dictionary : getDictionaries()) {
            allAudioFilePaths.addAll(dictionary.getAudioPaths());
        }
        return allAudioFilePaths;
    }
}
