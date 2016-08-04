package org.mercycorps.translationcards.activity.addTranslation;

import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;

import java.io.Serializable;

public class NewTranslation implements Serializable {
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
}
