package org.mercycorps.translationcards.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LanguageService {

    public final Map<String, List<String>> languageMap;

    public LanguageService() {
        languageMap = new HashMap<>();

        for(String language : Locale.getISOLanguages()) {
            Locale locale = new Locale(language);
            languageMap.put(locale.getISO3Language(), new ArrayList<>(Collections.singletonList(locale.getDisplayLanguage().toUpperCase())));
        }

        languageMap.get("fas").add("FARSI");
        languageMap.get("pus").add("PASHTO");
    }

    public String getLanguageDisplayName(String isoCode) {
        if(languageMap.containsKey(isoCode)) {
            return languageMap.get(isoCode).get(0);
        } else {
            return isoCode;
        }
    }

    public  String getIsoForLanguage(String label) {
        for(String isoCode : languageMap.keySet()) {
            if(languageMap.get(isoCode).contains(label.trim().toUpperCase())) {
                return isoCode;
            }
        }
        return "";
    }

}
