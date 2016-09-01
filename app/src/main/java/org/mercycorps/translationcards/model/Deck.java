package org.mercycorps.translationcards.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.porting.JsonKeys;
import org.mercycorps.translationcards.porting.TranslationCardsISO;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Contains information about a collection of phrases in one or more languages.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Deck implements Parcelable {

    private String title;
    private String author;
    private String externalId;
    private long dbId;
    private long timestamp;
    private boolean locked;
    private String sourceLanguage;
    private Dictionary[] dictionaries;
    private static final String LANGUAGE_LIST_DELIMITER = "  ";

    public Deck(String title, String author, String externalId, long dbId, long timestamp,
                boolean locked, String sourceLanguage, Dictionary[] dictionaries) {
        this.title = title;
        this.author = author;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
        this.locked = locked;
        this.sourceLanguage = sourceLanguage;
        this.dictionaries = dictionaries;
    }

    public Deck(String title, String author, String externalId, long timestamp, boolean locked,
                String sourceLanguage) {
        this(title, author, externalId, -1, timestamp, locked, sourceLanguage, new Dictionary[0]);
    }

    public Deck() {
    }

    protected Deck(Parcel in) {
        title = in.readString();
        author = in.readString();
        externalId = in.readString();
        dbId = in.readLong();
        timestamp = in.readLong();
        locked = in.readInt() != 0;
        dictionaries = in.createTypedArray(Dictionary.CREATOR);
//        sourceLanguage = in.readParcelable(Language.class.getClassLoader());
        sourceLanguage = in.readString();
    }

    public static final Creator<Deck> CREATOR = new Creator<Deck>() {
        @Override
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        @Override
        public Deck[] newArray(int size) {
            return new Deck[size];
        }
    };

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
        return TranslationCardsISO.getISOCodeForLanguage(sourceLanguage);
    }

    public String getSourceLanguageName() {
        return sourceLanguage;
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

    public void setSourceLanguage(String language) {
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
        json.put(JsonKeys.SOURCE_LANGUAGE_ISO, TranslationCardsISO.getISOCodeForLanguage(sourceLanguage));
        json.put(JsonKeys.LOCKED, locked);
        json.put(JsonKeys.DICTIONARIES, getDictionariesJSON());
        return json;
    }

    @NonNull
    private JSONArray getDictionariesJSON() throws JSONException {
        JSONArray dictionaryJSONArray = new JSONArray();
        for (Dictionary dictionary : dictionaries) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(externalId);
        dest.writeLong(dbId);
        dest.writeLong(timestamp);
        dest.writeInt(locked ? 1 : 0);
        dest.writeTypedArray(dictionaries, flags);
        dest.writeString(sourceLanguage);
    }
}
