/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mercycorps.translationcards.model;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.porting.JsonKeys;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains information about a set of phrases for a particular language.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Dictionary implements Serializable {


    private long dbId;
    private long deckId;
    private String destLanguageIso;
    private String language;
    private Translation[] translations;


    public Dictionary(String destLanguageIso, String language, Translation[] translations, long dbId,
                      long deckId) {
        this.destLanguageIso = destLanguageIso;
        this.language = language;
        this.translations = translations;
        this.dbId = dbId;
        this.deckId = deckId;
    }

    public Dictionary(String language) {
        this.language = language;
        this.translations = new Translation[0];
        this.dbId = -1;
        this.deckId = -1;
        this.destLanguageIso = "";
    }

    public String getDestLanguageIso() {
        return destLanguageIso;
    }

    protected void setDestLanguageIso(String newIso) {
        destLanguageIso = newIso;
    }

    public String getLanguage() {
        return isNullOrEmpty(language) ? this.destLanguageIso : language;
    }

    public int getTranslationCount() {
        return translations.length;
    }

    public Translation getTranslation(int index) {
        return translations[index];
    }

    public Translation getTranslationBySourcePhrase(String sourcePhrase) {
        for (Translation translation : translations) {
            if (sourcePhrase.equals(translation.getLabel())) {
                return translation;
            }
        }
        return new Translation();
    }

    public long getDbId() {
        return dbId;
    }

    private boolean isNullOrEmpty(String value) {
        return (value == null) || value.isEmpty();
    }

    @Override
    public String toString() {
        return language;
    }

    protected JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonKeys.DICTIONARY_DEST_ISO_CODE, destLanguageIso);
        jsonObject.put(JsonKeys.CARDS, createJSONTranslationsArray());

        return jsonObject;
    }

    @NonNull
    private JSONArray createJSONTranslationsArray() throws JSONException {
        JSONArray translationJSONArray = new JSONArray();
        for (Translation translation : translations) {
            translationJSONArray.put(translation.toJSON());
        }
        return translationJSONArray;
    }

    protected Map<String, Boolean> getAudioPaths() {
        HashMap<String, Boolean> pathMap = new HashMap<>();
        for (Translation translation : translations) {
            if (translation.isAudioFilePresent()) {
                pathMap.put(translation.getFilePath(), translation.getIsAsset());
            }
        }

        return pathMap;
    }
}
