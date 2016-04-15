package org.mercycorps.translationcards.activity.addTranslation;

import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;

import java.io.Serializable;

public class NewTranslationContext implements Serializable {
    private final Dictionary dictionary;
    private final Translation translation;
    private Boolean isEdit;

    public NewTranslationContext(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.translation = new Translation();
        isEdit = false;
    }

    public NewTranslationContext(Dictionary dictionary, Translation translation, Boolean isEdit){
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
        this.translation.setAudioFileName(fileName);
    }


    public void setTranslatedText(String translatedText) {
        this.translation.setTranslatedText(translatedText);
    }

    public Boolean isEdit() {
        return isEdit;
    }
}
