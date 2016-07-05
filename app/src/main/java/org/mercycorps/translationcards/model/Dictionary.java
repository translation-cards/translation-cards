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

import org.mercycorps.translationcards.MainApplication;

import java.io.Serializable;

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
        for(Translation translation : translations) {
            if(sourcePhrase.equals(translation.getLabel())) {
                return translation;
            }
        }
        Translation translation = new Translation();
        translation.setLabel(sourcePhrase);
        translation.saveWithDictionary(dbId);
        return translation;
    }

    public long getDbId() {
        return dbId;
    }

    private boolean isNullOrEmpty(String value) {
        return (value == null) || value.isEmpty();
    }

    public void save(Integer itemIndex) {
        ((MainApplication) MainApplication.getContextFromMainApp()).getDictionaryRepository().addDictionary(destLanguageIso, language, itemIndex, deckId);
    }

    public void setDeckId(long deckId) {
        this.deckId = deckId;
    }

    @Override
    public String toString() {
        return language;
    }
}
