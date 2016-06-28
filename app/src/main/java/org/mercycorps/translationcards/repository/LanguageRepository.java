package org.mercycorps.translationcards.repository;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.model.Language;

public class LanguageRepository {

    public Language withISO(String languageISO) {
        String languageName = ((MainApplication) MainApplication.getContextFromMainApp()).getLanguageService().getLanguageDisplayName(languageISO);
        return new Language(languageISO, languageName);
    }

    public Language withName(String languageName) {
        String languageISO = ((MainApplication) MainApplication.getContextFromMainApp()).getLanguageService().getIsoForLanguage(languageName);
        return new Language(languageISO, languageName);
    }
}
