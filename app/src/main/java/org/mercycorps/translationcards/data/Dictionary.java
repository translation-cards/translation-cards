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

import android.content.Context;

/**
 * Contains information about a set of phrases for a particular language.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Dictionary {

    private long dbId;
    private long deckId;
    private String label;
    private int itemIndex;
    private Translation[] translations;

    Dictionary(long dbId, long deckId, String label, int itemIndex, Translation[] translations) {
        this.dbId = dbId;
        this.deckId = deckId;
        this.label = label;
        this.itemIndex = itemIndex;
        this.translations = translations;
    }

    public Dictionary(long deckId, String label, int itemIndex) {
        this(-1, deckId, label, itemIndex, new Translation[] {});
    }

    public long getDbId() {
        return dbId;
    }

    public long getDeckId() {
        return deckId;
    }

    public String getLabel() {
        return label;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public int getTranslationCount() {
        return translations.length;
    }

    public Translation getTranslation(int index) {
        return translations[index];
    }

    public void setDeck(Deck deck) {
        this.deckId = deck.getDbId();
    }

    public void save(Context context) {
        DbManager dbm = new DbManager(context);
        if (dbId == -1) {
            dbId = dbm.addDictionary(this);
        } else {
            dbm.updateDictionary(this);
        }
    }

    public void delete(Context context) {
        DbManager dbm = new DbManager(context);
        if (dbId == -1) {
            throw new IllegalArgumentException("Tried to delete unsaved Deck.");
        }
        dbm.deleteDeck(dbId);
    }
}
