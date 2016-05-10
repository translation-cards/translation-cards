package org.mercycorps.translationcards.data;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import org.mercycorps.translationcards.MainApplication;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
        label = "";
        isAsset = false;
        filename = "";
        dbId = -1;
        translatedText = "";
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

    public void setMediaPlayerDataSource(Context context, MediaPlayer mp) throws IOException {
        if (isAsset) {
            AssetFileDescriptor fd = context.getAssets().openFd(filename);
            mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            fd.close();
        } else {
            mp.setDataSource(new FileInputStream(filename).getFD());
        }
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

    public FileDescriptor createFileDescriptor() throws IOException {
        return new FileInputStream(filename).getFD();
    }

    public void saveWithDictionary(Long dictionaryId) {
        dbId = ((MainApplication) MainApplication.getContextFromMainApp()).getDbManager().addTranslationAtTop(dictionaryId, label, isAsset, filename, translatedText);
    }
}
