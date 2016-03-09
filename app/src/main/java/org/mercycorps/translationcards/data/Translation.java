package org.mercycorps.translationcards.data;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Contains information about a single phrase.
 */
public class Translation implements Serializable {

    public static final String DEFAULT_TRANSLATED_TEXT = "";

    private long dbId;
    private long dictionaryId;
    private String label;
    private boolean isAsset;
    private String filename;
    private int itemIndex;
    private String translatedText;


    public Translation(long dbId, long dictionaryId,
                       String label, boolean isAsset, String filename, int itemIndex,
                       String translatedText) {
        this.dbId = dbId;
        this.dictionaryId = dictionaryId;
        this.label = label;
        this.isAsset = isAsset;
        this.filename = filename;
        this.itemIndex = itemIndex;
        this.translatedText = translatedText;
    }

    public Translation(long dictionaryId, String label, boolean isAsset, String filename,
                       int itemIndex, String translatedText) {
        this(-1, dictionaryId, label, isAsset, filename, itemIndex, translatedText);
    }

    public static Translation getTranslationById(Context context, long id) {
        DbManager dbm = new DbManager(context);
        return dbm.getTranslationById(id);
    }

    public long getDbId() {
        return dbId;
    }

    public long getDictionaryId() {
        return dictionaryId;
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

    public int getItemIndex() {
        return itemIndex;
    }

    public String getTranslatedText() {
        return translatedText == null ? DEFAULT_TRANSLATED_TEXT : translatedText;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionaryId = dictionary.getDbId();
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setIsAsset(boolean isAsset) {
        this.isAsset = isAsset;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public void setToTopOfDictionary(Context context) {
        DbManager dbm = new DbManager(context);
        itemIndex = dbm.getTopTranslationIndex(dictionaryId);
    }

    public void save(Context context) {
        DbManager dbm = new DbManager(context);
        if (dbId == -1) {
            dbId = dbm.addTranslation(this);
        } else {
            dbm.updateTranslation(this);
        }
    }

    public void delete(Context context) {
        DbManager dbm = new DbManager(context);
        dbm.deleteTranslation(this);
    }
}
