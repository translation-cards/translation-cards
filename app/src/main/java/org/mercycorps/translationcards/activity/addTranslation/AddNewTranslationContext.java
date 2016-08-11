package org.mercycorps.translationcards.activity.addTranslation;

import java.io.Serializable;
import java.util.List;

public class AddNewTranslationContext implements Serializable {

    private List<NewTranslation> newTranslations;
    private boolean isEdit;

    public AddNewTranslationContext(List<NewTranslation> newTranslations, boolean isEdit) {
        this.newTranslations = newTranslations;
        this.isEdit = isEdit;
    }

    public AddNewTranslationContext(List<NewTranslation> newTranslations) {
        this(newTranslations, false);
    }

    public List<NewTranslation> getNewTranslations() {
        return newTranslations;
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
