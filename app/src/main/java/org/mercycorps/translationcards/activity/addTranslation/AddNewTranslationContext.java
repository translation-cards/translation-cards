package org.mercycorps.translationcards.activity.addTranslation;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.service.TranslationService;

import java.io.Serializable;
import java.util.List;

public class AddNewTranslationContext implements Serializable {

    private List<NewTranslation> newTranslations;
    private boolean isEdit;

    public AddNewTranslationContext(List<NewTranslation> newTranslations) {
        this.newTranslations = newTranslations;
        isEdit = false;
    }

    public AddNewTranslationContext(List<NewTranslation> newTranslations, boolean isEdit) {
        this.newTranslations = newTranslations;
        this.isEdit = isEdit;
    }

    public List<NewTranslation> getNewTranslations() {
        return newTranslations;
    }

   public void save() {
       for (NewTranslation newTranslation : newTranslations) {
           getTranslationService().saveTranslationContext(newTranslation);
       }
   }

    private TranslationService getTranslationService() {
        return ((MainApplication) MainApplication.getContextFromMainApp()).getTranslationService();
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
