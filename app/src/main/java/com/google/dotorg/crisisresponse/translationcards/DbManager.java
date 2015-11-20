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

package com.google.dotorg.crisisresponse.translationcards;

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

    private final DbHelper dbh;

    public DbManager(Context context) {
        this.dbh = new DbHelper(context);
    }

    public Dictionary[] getAllDictionaries() {
        // Getting translations.
        Map<Long, List<Dictionary.Translation>> translations = new HashMap<>();
        String[] columns = {TranslationsTable.ID, TranslationsTable.DICTIONARY_ID,
                TranslationsTable.LABEL, TranslationsTable.IS_ASSET, TranslationsTable.FILENAME};
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
                    c.getString(c.getColumnIndex((TranslationsTable.FILENAME))),
                    c.getLong(c.getColumnIndex(TranslationsTable.ID))));
            go = c.moveToNext();
        }
        c.close();
        // Getting languages.
        columns = new String[] {DictionariesTable.ID, DictionariesTable.LABEL};
        c = dbh.getReadableDatabase().query(
                DictionariesTable.TABLE_NAME, columns,
                null, null, null, null, DictionariesTable.ITEM_INDEX);
        Dictionary[] res = new Dictionary[c.getCount()];
        int dictionaryIndex = 0;
        c.moveToFirst();
        while (dictionaryIndex < res.length) {
            long dictionaryId = c.getLong(c.getColumnIndex(DictionariesTable.ID));
            String label = c.getString(c.getColumnIndex(DictionariesTable.LABEL));
            Dictionary.Translation[] languageTranslations = {};
            if (translations.containsKey(dictionaryId)) {
                languageTranslations = translations.get(dictionaryId)
                        .toArray(new Dictionary.Translation[] {});
            }
            res[dictionaryIndex] = new Dictionary(label, languageTranslations, dictionaryId);
            c.moveToNext();
            dictionaryIndex++;
        }
        return res;
    }

    public long addLanguage(SQLiteDatabase writableDatabase, String label, int itemIndex) {
        ContentValues values = new ContentValues();
        values.put(DictionariesTable.LABEL, label);
        values.put(DictionariesTable.ITEM_INDEX, itemIndex);
        return writableDatabase.insert(DictionariesTable.TABLE_NAME, null, values);
    }

    public long addLanguage(String label, int itemIndex) {
        return addLanguage(dbh.getWritableDatabase(), label, itemIndex);
    }

    public long addTranslation(SQLiteDatabase writableDatabase,
            long dictionaryId, String label, boolean isAsset, String filename, int itemIndex) {
        Log.d(TAG, "Inserting translation...");
        ContentValues values = new ContentValues();
        values.put(TranslationsTable.DICTIONARY_ID, dictionaryId);
        values.put(TranslationsTable.LABEL, label);
        values.put(TranslationsTable.IS_ASSET, isAsset ? 1 : 0);
        values.put(TranslationsTable.FILENAME, filename);
        values.put(TranslationsTable.ITEM_INDEX, itemIndex);
        return writableDatabase.insert(TranslationsTable.TABLE_NAME, null, values);
    }

    public long addTranslation(
            long dictionaryId, String label, boolean isAsset, String filename, int itemIndex) {
        return addTranslation(
                dbh.getWritableDatabase(), dictionaryId, label, isAsset, filename, itemIndex);
    }

    public long addTranslationAtTop(
            long dictionaryId, String label, boolean isAsset, String filename) {
        String maxColumnName = String.format("MAX(%s)", TranslationsTable.ITEM_INDEX);
        Cursor c = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, new String[] {maxColumnName},
                String.format("%s = ?", TranslationsTable.DICTIONARY_ID),
                new String[] {String.format("%d", dictionaryId)},
                null, null, null);
        if (!c.moveToFirst()) {
            return addTranslation(dictionaryId, label, isAsset, filename, 0);
        }
        int itemIndex = c.getInt(c.getColumnIndex(maxColumnName)) + 1;
        c.close();
        return addTranslation(dictionaryId, label, isAsset, filename, itemIndex);
    }

    public void updateTranslation(
            long translationId, String label, boolean isAsset, String filename) {
        ContentValues values = new ContentValues();
        values.put(TranslationsTable.LABEL, label);
        values.put(TranslationsTable.IS_ASSET, isAsset);
        values.put(TranslationsTable.FILENAME, filename);
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

    private class DictionariesTable {
        public static final String TABLE_NAME = "dictionaries";
        public static final String ID = "id";
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
    }

    private class DbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "TranslationCards.db";
        private static final int DATABASE_VERSION = 1;

        private static final String INIT_DICTIONARIES_SQL =
                "CREATE TABLE " + DictionariesTable.TABLE_NAME + "( " +
                DictionariesTable.ID + " INTEGER PRIMARY KEY," +
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
                TranslationsTable.ITEM_INDEX + " INTEGER" +
                ")";

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(INIT_DICTIONARIES_SQL);
            db.execSQL(INIT_TRANSLATIONS_SQL);
            populateIncludedData(db);
        }

        private void populateIncludedData(SQLiteDatabase db) {
            for (int dictionaryIndex = 0; dictionaryIndex < INCLUDED_DATA.length; dictionaryIndex++) {
                Dictionary dictionary = INCLUDED_DATA[dictionaryIndex];
                long dictionaryId = addLanguage(db, dictionary.getLabel(), dictionaryIndex);
                for (int translationIndex = 0;
                     translationIndex < dictionary.getTranslationCount();
                     translationIndex++) {
                    Dictionary.Translation translation = dictionary.getTranslation(translationIndex);
                    int itemIndex = dictionary.getTranslationCount() - translationIndex - 1;
                    addTranslation(db, dictionaryId, translation.getLabel(),
                            translation.getIsAsset(), translation.getFilename(), itemIndex);
                }
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Do nothing.
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Do nothing.
        }
    }

    private final Dictionary[] INCLUDED_DATA = new Dictionary[] {
            new Dictionary("Farsi", new Dictionary.Translation[] {}),
            new Dictionary("Arabic", new Dictionary.Translation[] {}),
            new Dictionary("Pashto", new Dictionary.Translation[] {}),
    };
}
