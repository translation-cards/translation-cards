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

package org.mercycorps.translationcards;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages database operations.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class DbManager {

    private static final String TAG = "DbManager";

    // The value used in place of database IDs for items not yet in the database.
    private static final long NO_VALUE_ID = -1;

    private final DbHelper dbh;

    public DbManager(Context context) {
        this.dbh = new DbHelper(context);
    }

    public Dictionary[] getAllDictionaries() {
        // Getting translations.
        Map<Long, List<Dictionary.Translation>> translations = new HashMap<>();
        String[] columns = {TranslationsTable.ID, TranslationsTable.DICTIONARY_ID,
                TranslationsTable.LABEL, TranslationsTable.IS_ASSET, TranslationsTable.FILENAME,
                TranslationsTable.TRANSLATED_TEXT};
        Cursor c = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, columns,
                null, null, null, null,
                String.format("%s DESC", TranslationsTable.ITEM_INDEX));
        boolean go = c.moveToFirst();
        while (go) {
            long dictionaryId = c.getLong(c.getColumnIndex(TranslationsTable.DICTIONARY_ID));
            if (!translations.containsKey(dictionaryId)) {
                translations.put(dictionaryId, new ArrayList<Dictionary.Translation>());
            }
            translations.get(dictionaryId).add(new Dictionary.Translation(
                    c.getString(c.getColumnIndex(TranslationsTable.LABEL)),
                    c.getInt(c.getColumnIndex(TranslationsTable.IS_ASSET)) == 1,
                    c.getString(c.getColumnIndex(TranslationsTable.FILENAME)),
                    c.getLong(c.getColumnIndex(TranslationsTable.ID)),
                    c.getString(c.getColumnIndex(TranslationsTable.TRANSLATED_TEXT))));
            go = c.moveToNext();
        }
        c.close();
        // Getting languages.
        columns = new String[] {DictionariesTable.ID, DictionariesTable.LABEL,
                DictionariesTable.DECK_ID};
        c = dbh.getReadableDatabase().query(
                DictionariesTable.TABLE_NAME, columns,
                null, null, null, null, DictionariesTable.ITEM_INDEX);
        Dictionary[] res = new Dictionary[c.getCount()];
        int dictionaryIndex = 0;
        c.moveToFirst();
        while (dictionaryIndex < res.length) {
            long dictionaryId = c.getLong(c.getColumnIndex(DictionariesTable.ID));
            String label = c.getString(c.getColumnIndex(DictionariesTable.LABEL));
            long deckId = c.getLong(c.getColumnIndex(DictionariesTable.DECK_ID));
            Dictionary.Translation[] languageTranslations = {};
            if (translations.containsKey(dictionaryId)) {
                languageTranslations = translations.get(dictionaryId)
                        .toArray(new Dictionary.Translation[] {});
            }
            res[dictionaryIndex] = new Dictionary(
                    label, languageTranslations, dictionaryId, deckId);
            c.moveToNext();
            dictionaryIndex++;
        }
        return res;
    }

    public long addDeck(SQLiteDatabase writableDatabase, String label, String publisher) {
        ContentValues values = new ContentValues();
        values.put(DecksTable.LABEL, label);
        values.put(DecksTable.PUBLISHER, publisher);
        return writableDatabase.insert(DecksTable.TABLE_NAME, null, values);
    }

    public long addDictionary(SQLiteDatabase writableDatabase, String label, int itemIndex,
                              long deckId) {
        ContentValues values = new ContentValues();
        values.put(DictionariesTable.LABEL, label);
        values.put(DictionariesTable.ITEM_INDEX, itemIndex);
        values.put(DictionariesTable.DECK_ID, deckId);
        return writableDatabase.insert(DictionariesTable.TABLE_NAME, null, values);
    }

    public long addDictionary(String label, int itemIndex, long deckId) {
        return addDictionary(dbh.getWritableDatabase(), label, itemIndex, deckId);
    }

    public long addTranslation(SQLiteDatabase writableDatabase,
            long dictionaryId, String label, boolean isAsset, String filename, int itemIndex, String translatedText) {
        Log.d(TAG, "Inserting translation...");
        ContentValues values = new ContentValues();
        values.put(TranslationsTable.DICTIONARY_ID, dictionaryId);
        values.put(TranslationsTable.LABEL, label);
        values.put(TranslationsTable.IS_ASSET, isAsset ? 1 : 0);
        values.put(TranslationsTable.FILENAME, filename);
        values.put(TranslationsTable.ITEM_INDEX, itemIndex);
        values.put(TranslationsTable.TRANSLATED_TEXT, translatedText);
        return writableDatabase.insert(TranslationsTable.TABLE_NAME, null, values);
    }

    public long addTranslation(
            long dictionaryId, String label, boolean isAsset, String filename, int itemIndex, String translatedText) {
        return addTranslation(
                dbh.getWritableDatabase(), dictionaryId, label, isAsset, filename, itemIndex, translatedText);
    }

    public long addTranslationAtTop(
            long dictionaryId, String label, boolean isAsset, String filename, String translatedText) {
        String maxColumnName = String.format("MAX(%s)", TranslationsTable.ITEM_INDEX);
        Cursor c = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, new String[] {maxColumnName},
                String.format("%s = ?", TranslationsTable.DICTIONARY_ID),
                new String[] {String.format("%d", dictionaryId)},
                null, null, null);
        if (!c.moveToFirst()) {
            return addTranslation(dictionaryId, label, isAsset, filename, 0, translatedText);
        }
        int itemIndex = c.getInt(c.getColumnIndex(maxColumnName)) + 1;
        c.close();
        return addTranslation(dictionaryId, label, isAsset, filename, itemIndex, translatedText);
    }

    public void updateTranslation(
            long translationId, String label, boolean isAsset, String filename, String translatedText) {
        ContentValues values = new ContentValues();
        values.put(TranslationsTable.LABEL, label);
        values.put(TranslationsTable.IS_ASSET, isAsset);
        values.put(TranslationsTable.FILENAME, filename);
        values.put(TranslationsTable.TRANSLATED_TEXT, translatedText);
        String whereClause = String.format("%s = ?", TranslationsTable.ID);
        String[] whereArgs = new String[] {String.format("%d", translationId)};
        dbh.getWritableDatabase().update(
                TranslationsTable.TABLE_NAME, values, whereClause, whereArgs);
    }

    public void deleteTranslation(long translationId) {
        String whereClause = String.format("%s = ?", TranslationsTable.ID);
        String[] whereArgs = new String[] {String.format("%d", translationId)};
        dbh.getWritableDatabase().delete(TranslationsTable.TABLE_NAME, whereClause, whereArgs);
    }

    private class DecksTable {
        public static final String TABLE_NAME = "decks";
        public static final String ID = "id";
        public static final String LABEL = "label";
        public static final String PUBLISHER = "publisher";
    }

    private class DictionariesTable {
        public static final String TABLE_NAME = "dictionaries";
        public static final String ID = "id";
        public static final String DECK_ID = "deckId";
        public static final String LABEL = "label";
        public static final String ITEM_INDEX = "itemIndex";
    }

    private class TranslationsTable {
        public static final String TABLE_NAME = "translations";
        public static final String ID = "id";
        public static final String DICTIONARY_ID = "dictionaryId";
        public static final String LABEL = "label";
        public static final String IS_ASSET = "isAsset";
        public static final String FILENAME = "filename";
        public static final String ITEM_INDEX = "itemIndex";
        public static final String TRANSLATED_TEXT = "translationText";
    }

    private class DbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "TranslationCards.db";
        private static final int DATABASE_VERSION = 2;

        // Initialization SQL.
        private static final String INIT_DECKS_SQL =
                "CREATE TABLE " + DecksTable.TABLE_NAME + "( " +
                DecksTable.ID + " INTEGER PRIMARY KEY," +
                DecksTable.LABEL + " TEXT," +
                DecksTable.PUBLISHER + " TEXT" +
                ")";
        private static final String INIT_DICTIONARIES_SQL =
                "CREATE TABLE " + DictionariesTable.TABLE_NAME + "( " +
                DictionariesTable.ID + " INTEGER PRIMARY KEY," +
                DictionariesTable.DECK_ID + " INTEGER," +
                DictionariesTable.LABEL + " TEXT," +
                DictionariesTable.ITEM_INDEX + " INTEGER" +
                ")";
        private static final String INIT_TRANSLATIONS_SQL =
                "CREATE TABLE " + TranslationsTable.TABLE_NAME + " (" +
                TranslationsTable.ID + " INTEGER PRIMARY KEY," +
                TranslationsTable.DICTIONARY_ID + " INTEGER," +
                TranslationsTable.LABEL + " TEXT," +
                TranslationsTable.IS_ASSET + " INTEGER," +
                TranslationsTable.FILENAME + " TEXT," +
                TranslationsTable.ITEM_INDEX + " INTEGER," +
                TranslationsTable.TRANSLATED_TEXT + " TEXT" +
                ")";

        // Update SQL.
        private static final String ALTER_TABLE_ADD_TRANSLATED_TEXT_COLUMN =
                "ALTER TABLE" + TranslationsTable.TABLE_NAME + "ADD " +
                TranslationsTable.TRANSLATED_TEXT +" TEXT";
        private static final String ALTER_TABLE_ADD_DECK_FOREIGN_KEY =
                "ALTER TABLE " + DictionariesTable.TABLE_NAME + " ADD " +
                DictionariesTable.DECK_ID + " INTEGER";

        private final Context context;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(INIT_DECKS_SQL);
            db.execSQL(INIT_DICTIONARIES_SQL);
            db.execSQL(INIT_TRANSLATIONS_SQL);
            long defaultDeckId = addDeck(
                    db, context.getString(R.string.data_default_deck_name), "");
            populateIncludedData(db, defaultDeckId);
        }

        private void populateIncludedData(SQLiteDatabase db, long defaultDeckId) {
            for (int dictionaryIndex = 0; dictionaryIndex < INCLUDED_DATA.length;
                 dictionaryIndex++) {
                Dictionary dictionary = INCLUDED_DATA[dictionaryIndex];
                long dictionaryId = addDictionary(db, dictionary.getLabel(), dictionaryIndex,
                        defaultDeckId);
                for (int translationIndex = 0;
                     translationIndex < dictionary.getTranslationCount();
                     translationIndex++) {
                    Dictionary.Translation translation =
                            dictionary.getTranslation(translationIndex);
                    int itemIndex = dictionary.getTranslationCount() - translationIndex - 1;
                    addTranslation(db, dictionaryId, translation.getLabel(),
                            translation.getIsAsset(), translation.getFilename(), itemIndex,
                            translation.getTranslatedText());
                }
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Translation text and the decks table were added in v2 of the database.
            if (oldVersion == 1) {
                db.execSQL(ALTER_TABLE_ADD_TRANSLATED_TEXT_COLUMN);
                db.execSQL(INIT_DECKS_SQL);
                db.execSQL(ALTER_TABLE_ADD_DECK_FOREIGN_KEY);
                long defaultDeckId = addDeck(
                        db, context.getString(R.string.data_default_deck_name), "");
                ContentValues defaultDeckUpdateValues = new ContentValues();
                defaultDeckUpdateValues.put(DictionariesTable.DECK_ID, defaultDeckId);
                db.update(DictionariesTable.TABLE_NAME, defaultDeckUpdateValues, null, null);
            }
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Do nothing.
        }
    }

    private final Dictionary[] INCLUDED_DATA = new Dictionary[] {
            new Dictionary("Arabic", new Dictionary.Translation[] {}, NO_VALUE_ID, NO_VALUE_ID),
            new Dictionary("Farsi", new Dictionary.Translation[] {}, NO_VALUE_ID, NO_VALUE_ID),
            new Dictionary("Pashto", new Dictionary.Translation[] {}, NO_VALUE_ID, NO_VALUE_ID),
    };
}
