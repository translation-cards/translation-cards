package org.mercycorps.translationcards.model;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.porting.JsonKeys;
import org.mercycorps.translationcards.service.LanguageService;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

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
    private static final String LANGUAGE_LIST_DELIMITER = "  ";

    @Inject LanguageService languageService;

    public Deck(String title, String author, String externalId, long dbId, long timestamp,
                boolean locked, Language sourceLanguage, Dictionary[] dictionaries) {
        this.title = title;
        this.author = author;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
        this.locked = locked;
        this.sourceLanguage = sourceLanguage;
        this.dictionaries = dictionaries;

        MainApplication application = (MainApplication) MainApplication.getContextFromMainApp();
        application.getBaseComponent().inject(this);
    }

    public Deck(String title, String author, String externalId, long timestamp, boolean locked,
                Language sourceLanguage) {
        this(title, author, externalId, -1, timestamp, locked, sourceLanguage, new Dictionary[0]);
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

    public String getDestinationLanguagesForDisplay() {
        StringBuilder builder = new StringBuilder();
        for (Dictionary dictionary : dictionaries) {
            builder.append(dictionary.getLanguage().toUpperCase()).append(LANGUAGE_LIST_DELIMITER);
        }

        return builder.toString().trim();
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
        for (Dictionary dictionary : dictionaries) {
            String destLanguageIso = dictionary.getDestLanguageIso();
            if (null == destLanguageIso || destLanguageIso.isEmpty()) {
                dictionary.setDestLanguageIso(languageService.getIsoForLanguage(dictionary.getLanguage()));
            }
            dictionaryJSONArray.put(dictionary.toJSON());
        }
        return dictionaryJSONArray;
    }

    public Map<String, Boolean> getAudioFilePaths() {
        HashMap<String, Boolean> audioFilesMap = new HashMap<>();
        for (Dictionary dictionary : dictionaries) {
            audioFilesMap.putAll(dictionary.getAudioPaths());
        }
        return audioFilesMap;
    }
}
