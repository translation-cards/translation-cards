package org.mercycorps.translationcards.service;


import android.support.annotation.NonNull;

import org.mercycorps.translationcards.porting.LanguagesImportUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LanguageService {

    private final Map<String, List<String>> languageMap;
    public static final String INVALID_ISO_CODE = "xx";
    public static final String INVALID_LANGUAGE = "INVALID";

    public LanguageService(LanguagesImportUtility langImportUtility) {
        languageMap = langImportUtility.getLanguageMap();
    }

    public String getLanguageDisplayName(String isoCode) {
        if(languageMap.containsKey(isoCode)) {
            return languageMap.get(isoCode).get(0);
        } else {
            return INVALID_LANGUAGE;
        }
    }

    public String getIsoForLanguage(String label) {
        for(String isoCode : languageMap.keySet()) {
            if(languageMap.get(isoCode).contains(getTitleCaseName(label.trim()))) {
                return isoCode;
            }
        }
        return INVALID_LANGUAGE;
    }

    public List<String> getLanguageNames() {
        List<String> languageNames = new ArrayList<>();

        for(List<String> list : languageMap.values()) {
            for(String name : list) {
                languageNames.add(getTitleCaseName(name));
            }
        }

        Collections.sort(languageNames);

        return languageNames;
    }

    @NonNull
    public static String getTitleCaseName(String name) {
        if(name == null|| name.isEmpty()){
            return "";
        }
        String titleCaseName = "";
        String[] tokens = name.split(" ");
        for(String token : tokens) {
            String lowerCaseName = token.toLowerCase();
            titleCaseName += lowerCaseName.substring(0, 1).toUpperCase() + lowerCaseName.substring(1) + " ";
        }

        return titleCaseName.trim();
    }
}
