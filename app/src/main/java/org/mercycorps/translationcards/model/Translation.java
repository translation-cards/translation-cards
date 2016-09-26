package org.mercycorps.translationcards.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.porting.JsonKeys;

import java.io.File;

/**
 * Contains information about a single phrase.
 */
public class Translation implements Parcelable {

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
        this("", false, "", -1, "");
    }

    protected Translation(Parcel in) {
        label = in.readString();
        isAsset = in.readInt() != 0;
        filePath = in.readString();
        dbId = in.readLong();
        translatedText = in.readString();
    }

    public static final Creator<Translation> CREATOR = new Creator<Translation>() {
        @Override
        public Translation createFromParcel(Parcel in) {
            return new Translation(in);
        }

        @Override
        public Translation[] newArray(int size) {
            return new Translation[size];
        }
    };

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

    protected JSONObject toJSON() throws JSONException {
        JSONObject cardJson = new JSONObject();
        String name = new File(this.filePath).getName();
        cardJson.put(JsonKeys.CARD_LABEL, label);
        cardJson.put(JsonKeys.CARD_DEST_AUDIO, name);
        cardJson.put(JsonKeys.CARD_DEST_TEXT, translatedText);
        return cardJson;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(label);
        dest.writeInt(isAsset? 1 : 0);
        dest.writeString(filePath);
        dest.writeLong(dbId);
        dest.writeString(translatedText);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Translation that = (Translation) o;

        if (isAsset != that.isAsset) return false;
        if (dbId != that.dbId) return false;
        if (!label.equals(that.label)) return false;
        if (!filePath.equals(that.filePath)) return false;
        return translatedText.equals(that.translatedText);

    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + (isAsset ? 1 : 0);
        result = 31 * result + filePath.hashCode();
        result = 31 * result + (int) (dbId ^ (dbId >>> 32));
        result = 31 * result + translatedText.hashCode();
        return result;
    }
}
