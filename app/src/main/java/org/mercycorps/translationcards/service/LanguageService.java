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
//        languageMap.put("ara", Collections.singletonList("Arabic"));
//        languageMap.put("eng", Collections.singletonList("English"));
//        languageMap.put("fas", Arrays.asList("Farsi", "Persian"));
//        languageMap.put("pus", Arrays.asList("Pashto", "Pushto"));

        Set<String> isoCodes = new TreeSet<>();
        Set<String> displayLanguages = new TreeSet<>();

//        for (Locale locale : Locale.getAvailableLocales()) {
//            String isoCode = locale.getISO3Language();
//            String displayLanguage = locale.getDisplayLanguage();
//            if(languageMap.containsKey(isoCode)) {
//                languageMap.get(isoCode).add(displayLanguage);
//            } else {
//                languageMap.put(isoCode, Arrays.asList(displayLanguage));
//            }
//        }

        for(String language : Locale.getISOLanguages()) {
            Locale locale = new Locale(language);
            languageMap.put(locale.getISO3Language(), new ArrayList<String>(Collections.singletonList(locale.getDisplayLanguage())));
        }
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
            if(languageMap.get(isoCode).contains(label.trim())) {
                return isoCode;
            }
        }

        return "";
    }

}
