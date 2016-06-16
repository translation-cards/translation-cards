package org.mercycorps.translationcards.data;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.service.LanguageService;

import java.io.Serializable;

public class Language implements Serializable {
    private String name;
    private String iso;

    public Language(String iso, String name) {
        this.iso = iso;
        this.name = name;
    }

    public static Language withISO(String languageISO) {
        String languageName = ((MainApplication) MainApplication.getContextFromMainApp()).getLanguageService().getLanguageDisplayName(languageISO);
        return new Language(languageISO, languageName);
    }

    public static Language withName(String languageName) {
        String languageISO = ((MainApplication) MainApplication.getContextFromMainApp()).getLanguageService().getIsoForLanguage(languageName);
        return new Language(languageISO, languageName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }
}
