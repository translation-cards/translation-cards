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
    private final String label;
    private final boolean isAsset;
    private final String filename;
    private final long dbId;
    private final String translatedText;


    public Translation(String label, boolean isAsset, String filename, long dbId, String translatedText) {
        this.label = label;
        this.isAsset = isAsset;
        this.filename = filename;
        this.dbId = dbId;
        this.translatedText = translatedText;
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

    public String getTranslatedText() {
        return translatedText == null ? DEFAULT_TRANSLATED_TEXT : translatedText;
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
}
