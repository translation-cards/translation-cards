package org.mercycorps.translationcards.activity.addTranslation;

import org.mercycorps.translationcards.data.Dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddNewTranslationContext implements Serializable {

    private List<NewTranslation> newTranslations;
    private boolean isEdit;

    public AddNewTranslationContext(List<NewTranslation> newTranslations) {
        this.newTranslations = newTranslations;
        isEdit = false;
    }

    public List<NewTranslation> getNewTranslations() {
        return newTranslations;
    }

    public ArrayList<Dictionary> getDictionaries() {
        ArrayList<Dictionary> dictionaries = new ArrayList<>();
        for (NewTranslation newTranslation : newTranslations) {
            dictionaries.add(newTranslation.getDictionary());
        }
        return dictionaries;
    }

    public String getSourcePhrase() {
        return newTranslations.isEmpty() ? "" : getTranslationsSourcePhrase();
    }

    private String getTranslationsSourcePhrase() {
        return newTranslations.get(0).getTranslation().getLabel();
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setSourceText(String sourceText) {
        for (NewTranslation newTranslation : newTranslations) {
            newTranslation.setSourceText(sourceText);
        }
    }
}
