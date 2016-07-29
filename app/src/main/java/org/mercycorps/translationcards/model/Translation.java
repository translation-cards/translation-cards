package org.mercycorps.translationcards.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.porting.JsonKeys;

import java.io.File;
import java.io.Serializable;

/**
 * Contains information about a single phrase.
 */
public class Translation implements Serializable {

    public static final String DEFAULT_TRANSLATED_TEXT = "";
    private String label;
    private boolean isAsset;
    private String filePath;
    private long dbId;
    private String translatedText;

    public Translation(String label, boolean isAsset, String filePath, long dbId, String translatedText) {
        this.label = label;
        this.isAsset = isAsset;
        this.filePath = filePath;
        this.dbId = dbId;
        this.translatedText = translatedText;
    }

    public Translation() {
        label = "";
        isAsset = false;
        filePath = "";
        dbId = -1;
        translatedText = "";
    }

    public String getLabel() {
        return label;
    }

    public boolean getIsAsset() {
        return isAsset;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getDbId() {
        return dbId;
    }

    public void setIsAsset(boolean isAsset) {
        this.isAsset = isAsset;
    }

    public String getTranslatedText() {
        return translatedText == null ? DEFAULT_TRANSLATED_TEXT : translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAudioFilePath(String audioFileName) {
        this.filePath = audioFileName;
    }

    public boolean isAudioFilePresent() {
        return !(filePath == null || filePath.isEmpty());
    }

    public void saveWithDictionary(Long dictionaryId) {
        dbId = ((MainApplication) MainApplication.getContextFromMainApp()).getTranslationRepository().addTranslationAtTop(dictionaryId, label, isAsset, filePath, translatedText);
    }

    protected JSONObject toJSON() throws JSONException {
        JSONObject cardJson = new JSONObject();
        String name = new File(this.filePath).getName();
        cardJson.put(JsonKeys.CARD_LABEL, label);
        cardJson.put(JsonKeys.CARD_DEST_AUDIO, name);
        cardJson.put(JsonKeys.CARD_DEST_TEXT, translatedText);
        return cardJson;
    }
}
