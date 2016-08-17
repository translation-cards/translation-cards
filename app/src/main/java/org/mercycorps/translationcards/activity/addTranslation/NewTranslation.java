package org.mercycorps.translationcards.activity.addTranslation;

import android.os.Parcel;
import android.os.Parcelable;

import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;

public class NewTranslation implements Parcelable {
    private final Dictionary dictionary;
    private final Translation translation;
    private Boolean isEdit;

    public NewTranslation(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.translation = new Translation();
        isEdit = false;
    }

    public NewTranslation(Dictionary dictionary, Translation translation, Boolean isEdit){
        this.dictionary = dictionary;
        this.translation = translation;
        this.isEdit = isEdit;
    }

    protected NewTranslation(Parcel in) {
        dictionary = in.readParcelable(Dictionary.class.getClassLoader());
        translation = in.readParcelable(Translation.class.getClassLoader());
        isEdit = in.readInt() == 1;
    }

    public static final Creator<NewTranslation> CREATOR = new Creator<NewTranslation>() {
        @Override
        public NewTranslation createFromParcel(Parcel in) {
            return new NewTranslation(in);
        }

        @Override
        public NewTranslation[] newArray(int size) {
            return new NewTranslation[size];
        }
    };

    public Dictionary getDictionary() {
        return dictionary;
    }

    public Translation getTranslation() {
        return translation;
    }

    public void setSourceText(String sourceText) {
        this.translation.setLabel(sourceText);
    }

    public void setAudioFile(String fileName){
        this.translation.setAudioFilePath(fileName);
    }


    public void setTranslatedText(String translatedText) {
        this.translation.setTranslatedText(translatedText);
    }

    public Boolean isEdit() {
        return isEdit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(dictionary, flags);
        parcel.writeParcelable(translation, flags);
        parcel.writeInt(isEdit? 1 : 0);
    }
}
