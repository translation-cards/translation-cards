package org.mercycorps.translationcards.activity.addTranslation;

import android.support.annotation.NonNull;

import org.mercycorps.translationcards.data.Dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddNewTranslationContext implements Serializable {

    private List<NewTranslationContext> newTranslationContexts;
    private boolean isEdit;

    public AddNewTranslationContext(List<NewTranslationContext> newTranslationContexts) {
        this.newTranslationContexts = newTranslationContexts;
        isEdit = false;
    }

    public List<NewTranslationContext> getNewTranslationContexts() {
        return newTranslationContexts;
    }

    public ArrayList<Dictionary> getDictionaries() {
        ArrayList<Dictionary> dictionaries = new ArrayList<>();
        for (NewTranslationContext newTranslationContext : newTranslationContexts) {
            dictionaries.add(newTranslationContext.getDictionary());
        }
        return dictionaries;
    }

    public String getSourcePhrase() {
        return newTranslationContexts.isEmpty() ? "" : getTranslationsSourcePhrase();
    }

    private String getTranslationsSourcePhrase() {
        return newTranslationContexts.get(0).getTranslation().getLabel();
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setSourceText(String sourceText) {
        for (NewTranslationContext newTranslationContext : newTranslationContexts) {
            newTranslationContext.setSourceText(sourceText);
        }
    }
}
