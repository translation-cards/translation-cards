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

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.porting.JsonKeys;
import org.mercycorps.translationcards.porting.TranslationCardsISO;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains information about a set of phrases for a particular language.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Dictionary implements Parcelable {


    private long dbId;
    private String language;
    private Translation[] translations;


    public Dictionary(String language, Translation[] translations, long dbId) {
        this.language = language;
        this.translations = translations;
        this.dbId = dbId;
    }

    public Dictionary(String language) {
        this(language, new Translation[0], -1L);
    }

    protected Dictionary(Parcel in) {
        dbId = in.readLong();
        language = in.readString();
        translations = in.createTypedArray(Translation.CREATOR);
    }

    public static final Creator<Dictionary> CREATOR = new Creator<Dictionary>() {
        @Override
        public Dictionary createFromParcel(Parcel in) {
            return new Dictionary(in);
        }

        @Override
        public Dictionary[] newArray(int size) {
            return new Dictionary[size];
        }
    };

    public String getLanguage() {
        return language;
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

    @Override
    public String toString() {
        return language;
    }

    protected JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonKeys.DICTIONARY_DEST_ISO_CODE, TranslationCardsISO.getISOCodeForLanguage(language));
        jsonObject.put(JsonKeys.DICTIONARY_DEST_NAME, language);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dbId);
        dest.writeString(language);
        dest.writeTypedArray(translations, flags);
    }
}
