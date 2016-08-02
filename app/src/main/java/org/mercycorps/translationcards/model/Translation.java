package org.mercycorps.translationcards.model;

import java.io.Serializable;

/**
 * Contains information about a single phrase.
 */
public class Translation implements Serializable {

    public static final String DEFAULT_TRANSLATED_TEXT = "";
    private String label;
    private boolean isAsset;
    private String filename;
    private long dbId;
    private String translatedText;

    public Translation(String label, boolean isAsset, String filename, long dbId, String translatedText) {
        this.label = label;
        this.isAsset = isAsset;
        this.filename = filename;
        this.dbId = dbId;
        this.translatedText = translatedText;
    }

    public Translation() {
        this("", false, "", -1, "");
    }

    public String getLabel() {
        return label;
    }

    public boolean getIsAsset() {
        return isAsset;
    }

    public String getFilename() {
        return filename;
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

    public void setAudioFileName(String audioFileName) {
        this.filename = audioFileName;
    }

    public boolean isAudioFilePresent(){
        return !(filename == null || filename.isEmpty());
    }
}
