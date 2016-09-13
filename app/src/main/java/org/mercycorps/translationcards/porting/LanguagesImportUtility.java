package org.mercycorps.translationcards.porting;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to handle constructing and reading .txc files.
 *
 * @author atamrat@thoughtworks.com (Abel Tamrat)
 */
public class LanguagesImportUtility {
    private static final String TAG = LanguagesImportUtility.class.getName();
    private Map<String, List<String>> languageMap;

    public LanguagesImportUtility(InputStream inputStream) {
        loadLanguageMap(inputStream);
    }

    public Map<String, List<String>> getLanguageMap() {
        return languageMap;
    }

    private void loadLanguageMap(InputStream inputStream) {

        JSONObject jsonObject;
        Map<String, List<String>> langMap = new HashMap<>();
        if (inputStream != null) {
            try {
                jsonObject = readJSONFileFrom(inputStream);
                ImportSpec importSpec = buildImportSpec(jsonObject);
                langMap = loadAssetData(importSpec);
            } catch (IOException | JSONException | ImportException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        languageMap = langMap;
    }

    @NonNull
    private JSONObject readJSONFileFrom(InputStream inputStream) throws IOException, JSONException {
        JSONObject jsonObject;
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);
        jsonObject = new JSONObject(new String(buffer, "UTF-8"));
        return jsonObject;
    }

    private Map<String, List<String>> loadAssetData(ImportSpec importSpec) {
        Map<String, List<String>> languageMap = new HashMap<>();
        for (int i = 0; i < importSpec.languages.size(); i++) {
            ImportSpecLanguage language = importSpec.languages.get(i);
            languageMap.put(language.isoCode.substring(0, 2), language.languageNames);
        }
        return languageMap;
    }

    @NonNull
    public ImportSpec buildImportSpec(JSONObject json) throws ImportException {
        ImportSpec spec;
        try {
            spec = initializeSpecWithMetaData(json);

            JSONArray languages = json.optJSONArray(JsonKeys.LANGUAGES);
            if (languages == null) {
                return spec;
            }

            loadLanguagesIntoSpec(spec, languages);
        } catch (JSONException e) {
            throw new ImportException(ImportException.ImportProblem.INVALID_INDEX_FILE, e);
        }

        return spec;
    }

    private void loadLanguagesIntoSpec(ImportSpec spec, JSONArray languages) throws JSONException {
        for (int i = 0; i < languages.length(); i++) {
            JSONObject language = languages.getJSONObject(i);
            String IsoCode = language.getString(JsonKeys.LANGUAGE_ISO_CODE);
            JSONArray languageDisplayNames = language.optJSONArray(JsonKeys.LANGUAGE_NAMES);

            ImportSpecLanguage languageSpec = new ImportSpecLanguage(IsoCode);
            for (int j = 0; j < languageDisplayNames.length(); j++) {
                languageSpec.languageNames.add(languageDisplayNames.getString(j));
            }
            spec.languages.add(languageSpec);
        }
    }

    @NonNull
    private ImportSpec initializeSpecWithMetaData(JSONObject json) {
        ImportSpec spec;
        String externalId = json.optString(JsonKeys.EXTERNAL_LANGUAGE_LIST_VERSION_ID);
        long timestamp = json.optLong(JsonKeys.TIMESTAMP, -1);
        spec = new ImportSpec(externalId, timestamp);
        return spec;
    }


    public class ImportSpec {

        public final String externalId;
        public final long timestamp;
        public final List<ImportSpecLanguage> languages;

        public ImportSpec(String externalId, long timestamp) {
            this.externalId = externalId;
            this.timestamp = timestamp;
            languages = new ArrayList<>();
        }
    }

    private class ImportSpecLanguage {

        public final String isoCode;
        public List<String> languageNames;

        public ImportSpecLanguage(String isoCode) {
            this.isoCode = isoCode;
            this.languageNames = new ArrayList<>();
        }
    }

}
