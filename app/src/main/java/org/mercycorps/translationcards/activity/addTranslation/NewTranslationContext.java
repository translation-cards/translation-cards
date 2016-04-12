package org.mercycorps.translationcards.activity.addTranslation;

import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;

import java.io.Serializable;

public class NewTranslationContext implements Serializable {
    private final Dictionary dictionary;
    private final Translation translation;

    public NewTranslationContext(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.translation = new Translation();
    }

    public NewTranslationContext(Dictionary dictionary, Translation translation){
        this.dictionary = dictionary;
        this.translation = translation;
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
}
