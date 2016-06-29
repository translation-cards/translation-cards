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

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.mercycorps.translationcards.porting.TxcImportUtility;

/**
 * Manages database operations.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class DbManager {

    private final DbHelper dbh;

    public DbManager(Context context) {
        this.dbh = new DbHelper(context);
    }

    public class DecksTable {
        public static final String TABLE_NAME = "decks";
        public static final String ID = "id";
        public static final String LABEL = "label";
        public static final String PUBLISHER = "publisher";
        public static final String CREATION_TIMESTAMP = "creationTimestamp";
        public static final String EXTERNAL_ID = "externalId";
        public static final String HASH = "hash";
        public static final String LOCKED = "locked";
        public static final String SOURCE_LANGUAGE_ISO = "srcLanguageIso";
    }

    public class DictionariesTable {
        public static final String TABLE_NAME = "dictionaries";
        public static final String ID = "id";
        public static final String DECK_ID = "deckId";
        public static final String LANGUAGE_ISO = "languageIso";
        public static final String LABEL = "label";
        public static final String ITEM_INDEX = "itemIndex";
    }

    public class TranslationsTable {
        public static final String TABLE_NAME = "translations";
        public static final String ID = "id";
        public static final String DICTIONARY_ID = "dictionaryId";
        public static final String LABEL = "label";
        public static final String IS_ASSET = "isAsset";
        public static final String FILENAME = "filename";
        public static final String ITEM_INDEX = "itemIndex";
        public static final String TRANSLATED_TEXT = "translationText";
    }

    public DbHelper getDbh(){
        return dbh;
    }

    public class DbHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "TranslationCards.db";
        private static final int DATABASE_VERSION = 3;

        // Initialization SQL.
        private static final String INIT_DECKS_SQL =
                "CREATE TABLE " + DecksTable.TABLE_NAME + "( " +
                DecksTable.ID + " INTEGER PRIMARY KEY," +
                DecksTable.LABEL + " TEXT," +
                DecksTable.PUBLISHER + " TEXT," +
                DecksTable.CREATION_TIMESTAMP + " INTEGER," +
                DecksTable.EXTERNAL_ID + " TEXT," +
                DecksTable.HASH + " TEXT," +
                DecksTable.LOCKED + " INTEGER," +
                DecksTable.SOURCE_LANGUAGE_ISO + " TEXT" +
                ")";
        private static final String INIT_DICTIONARIES_SQL =
                "CREATE TABLE " + DictionariesTable.TABLE_NAME + "( " +
                DictionariesTable.ID + " INTEGER PRIMARY KEY," +
                DictionariesTable.DECK_ID + " INTEGER," +
                DictionariesTable.LABEL + " TEXT," +
                DictionariesTable.ITEM_INDEX + " INTEGER," +
                DictionariesTable.LANGUAGE_ISO + " TEXT" +
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
                "ALTER TABLE " + TranslationsTable.TABLE_NAME + " ADD " +
                TranslationsTable.TRANSLATED_TEXT + " TEXT";
        private static final String ALTER_TABLE_ADD_DECK_FOREIGN_KEY =
                "ALTER TABLE " + DictionariesTable.TABLE_NAME + " ADD " +
                DictionariesTable.DECK_ID + " INTEGER";
        private static final String ALTER_TABLE_ADD_SOURCE_LANGUAGE_COLUMN =
                "ALTER TABLE " + DecksTable.TABLE_NAME + " ADD " +
                DecksTable.SOURCE_LANGUAGE_ISO + " TEXT";
        private static final String ALTER_TABLE_ADD_DEST_LANGUAGE_ISO_COLUMN =
                "ALTER TABLE " + DictionariesTable.TABLE_NAME + " ADD " +
                DictionariesTable.LANGUAGE_ISO + " TEXT";

        private final Context context;

        public DbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            this.context = context;
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(INIT_DECKS_SQL);
            db.execSQL(INIT_DICTIONARIES_SQL);
            db.execSQL(INIT_TRANSLATIONS_SQL);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Translation text and the decks table were added in v2 of the database.
            if (oldVersion == 1) {
                db.execSQL(ALTER_TABLE_ADD_TRANSLATED_TEXT_COLUMN);
                db.execSQL(INIT_DECKS_SQL);
                db.execSQL(ALTER_TABLE_ADD_DECK_FOREIGN_KEY);
            }
            // Deck source languages and ISO codes for dictionary languages were added in v3 of the
            // database.
            if (oldVersion < 3) {
                if (oldVersion == 2) {
                    // No need to run this if going from v1 to v3, because we've just created the
                    // whole decks table above in that case.
                    db.execSQL(ALTER_TABLE_ADD_SOURCE_LANGUAGE_COLUMN);
                }
                db.execSQL(ALTER_TABLE_ADD_DEST_LANGUAGE_ISO_COLUMN);
                // We assume that the source language of all pre-existing decks is English.
                ContentValues defaultSourceLanguageValues = new ContentValues();
                defaultSourceLanguageValues.put(DecksTable.SOURCE_LANGUAGE_ISO, "eng");
                db.update(DecksTable.TABLE_NAME, defaultSourceLanguageValues, null, null);
                // We use "xx" as the destination language ISO code for all pre-existing
                // dictionaries. This will never be found in a language lookup table and will force
                // us to default to the user-specified label. By adding a dummy value, we avoid
                // having to deal with nulls in the future, since we will consider this a required
                // field going forward.
                ContentValues defaultDestLanguageValues = new ContentValues();
                defaultDestLanguageValues.put(DictionariesTable.LANGUAGE_ISO, "xx");
                db.update(DictionariesTable.TABLE_NAME, defaultDestLanguageValues, null, null);
            }
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Do nothing.
        }
    }
}
