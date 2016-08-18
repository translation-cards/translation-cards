package org.mercycorps.translationcards.service;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.porting.LanguagesImportUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageService {

    private final Map<String, List<String>> languageMap;
    public static final String INVALID_ISO_CODE = "xx";
    public static final String INVALID_LANGUAGE_NAME = "INVALID";

    public LanguageService(LanguagesImportUtility langImportUtility) {
        languageMap = langImportUtility.getLanguageMap();
    }

    public String getLanguageDisplayName(String isoCode) {
        if (languageMap.containsKey(isoCode)) {
            return languageMap.get(isoCode).get(0);
        }
        return INVALID_LANGUAGE_NAME;
    }

    public String getIsoForLanguage(String label) {
        if (label != null) {
            for (String isoCode : languageMap.keySet()) {
                if (languageMap.get(isoCode).contains(getTitleCaseName(label.trim()))) {
                    return isoCode;
                }
            }
        }
        return INVALID_ISO_CODE;
    }

    public Map<String, String> getLanguageNames() {
        Map<String, String> languageNames = new HashMap<>();

        for (List<String> list : languageMap.values()) {
            languageNames.put(TextUtils.join(" / ", list), list.get(0));
        }

        return languageNames;
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

    public Language getLanguageWithName(String languageName) {
        return new Language(getIsoForLanguage(languageName), languageName);
    }

    public Language getLanguageWithIso(String languageIso) {
        return new Language(languageIso, getLanguageDisplayName(languageIso));
    }
}
