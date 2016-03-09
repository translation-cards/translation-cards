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

package org.mercycorps.translationcards.data;

/**
 * Contains information about a set of phrases for a particular language.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Dictionary {

    private final String label;
    private final Translation[] translations;
    private final long dbId;
    private final long deckId;

    public Dictionary(String label, Translation[] translations, long dbId, long deckId) {
        this.label = label;
        this.translations = translations;
        this.dbId = dbId;
        this.deckId = deckId;
    }

    public String getLabel() {
        return label;
    }

    public int getTranslationCount() {
        return translations.length;
    }

    public Translation getTranslation(int index) {
        return translations[index];
    }

    public long getDbId() {
        return dbId;
    }

    public long getDeckId() {
        return deckId;
    }
}
