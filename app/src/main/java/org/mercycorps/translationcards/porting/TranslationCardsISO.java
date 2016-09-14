package org.mercycorps.translationcards.porting;

import android.support.annotation.NonNull;

import org.mercycorps.translationcards.service.LanguageCodeLoader;

import java.util.List;
import java.util.Map;

public class TranslationCardsISO {

    private static final Map<String, List<String>> languageMap = LanguageCodeLoader.createLanguagesImportUtility().getLanguageMap();;
    private static final String INVALID_ISO_CODE = "xx";
    public static final String INVALID_LANGUAGE_NAME = "INVALID";

    public static String getISOCodeForLanguage(String language) {
        if (language != null) {
            for (String isoCode : languageMap.keySet()) {
                if (languageMap.get(isoCode).contains(getTitleCaseName(language.trim()))) {
                    return isoCode;
                }
            }
        }
        return INVALID_ISO_CODE;
    }

    public static String getLanguageDisplayName(String isoCode, String sourceLanguageName) {
        String languageDisplayName = INVALID_LANGUAGE_NAME;
        if (languageMap.containsKey(isoCode)) {
            List<String> sourceLanguagesForIsoCode = languageMap.get(isoCode);
            String fallbackDisplayName = sourceLanguagesForIsoCode.get(0);
            String titleCaseSourceLanguage = getTitleCaseName(sourceLanguageName);
            languageDisplayName = sourceLanguagesForIsoCode.contains(titleCaseSourceLanguage) ? titleCaseSourceLanguage : fallbackDisplayName;
        }
        return languageDisplayName;
    }

    @NonNull
    public static String getTitleCaseName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        String titleCaseName = "";
        name = name.trim();
        String[] tokens = name.split(" ");
        for (String token : tokens) {
            String lowerCaseName = token.toLowerCase();
            titleCaseName += lowerCaseName.substring(0, 1).toUpperCase() + lowerCaseName.substring(1) + " ";
        }

        return titleCaseName.trim();
    }


}
