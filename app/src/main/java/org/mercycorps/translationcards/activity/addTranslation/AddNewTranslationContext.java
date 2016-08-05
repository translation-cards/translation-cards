package org.mercycorps.translationcards.activity.addTranslation;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.service.TranslationService;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

public class AddNewTranslationContext implements Serializable {

    private List<NewTranslation> newTranslations;
    private boolean isEdit;

    @Inject TranslationService translationService;

    public AddNewTranslationContext(List<NewTranslation> newTranslations, boolean isEdit) {
        this.newTranslations = newTranslations;
        this.isEdit = isEdit;

        MainApplication application = (MainApplication) MainApplication.getContextFromMainApp();
        application.getBaseComponent().inject(this);
    }

    public AddNewTranslationContext(List<NewTranslation> newTranslations) {
        this(newTranslations, false);
    }

    public List<NewTranslation> getNewTranslations() {
        return newTranslations;
    }

   public void save() {
       for (NewTranslation newTranslation : newTranslations) {
           translationService.saveTranslationContext(newTranslation);
       }
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
