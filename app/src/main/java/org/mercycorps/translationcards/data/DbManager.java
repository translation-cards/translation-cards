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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.porting.ImportException;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.service.LanguageService;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
    private final LanguageService languageService;

    public DbManager(Context context, LanguageService languageService) {
        this.languageService = languageService;
        this.dbh = new DbHelper(context);
    }

    public Dictionary[] getAllDictionariesForDeck(long deckId) {
        Cursor cursor = dbh.getReadableDatabase().query(
                DictionariesTable.TABLE_NAME, null,
                DictionariesTable.DECK_ID + " = ?",
                new String[]{String.valueOf(deckId)}, null, null,
                String.format("%s ASC", DictionariesTable.LABEL));

        Dictionary[] dictionaries = new Dictionary[cursor.getCount()];
        boolean hasNext = cursor.moveToFirst();
        int i = 0;
        while (hasNext) {
            String destLanguageIso = cursor.getString(cursor.getColumnIndex(
                    DictionariesTable.LANGUAGE_ISO));
            String label = cursor.getString(cursor.getColumnIndex(DictionariesTable.LABEL));
            long dictionaryId = cursor.getLong(cursor.getColumnIndex(DictionariesTable.ID));
            Dictionary dictionary = new Dictionary(destLanguageIso, label,
                    getTranslationsByDictionaryId(dictionaryId), dictionaryId, deckId);
            dictionaries[i] = dictionary;
            i++;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        return dictionaries;
    }

    public long addDeck(SQLiteDatabase writableDatabase, String label, String publisher,
                        long creationTimestamp, String externalId, String hash, boolean locked,
                        String srcLanguageIso) {
        ContentValues values = new ContentValues();
        values.put(DecksTable.LABEL, label);
        values.put(DecksTable.PUBLISHER, publisher);
        values.put(DecksTable.CREATION_TIMESTAMP, creationTimestamp);
        values.put(DecksTable.EXTERNAL_ID, externalId);
        values.put(DecksTable.HASH, hash);
        values.put(DecksTable.LOCKED, locked ? 1 : 0);
        values.put(DecksTable.SOURCE_LANGUAGE_ISO, srcLanguageIso);
        return writableDatabase.insert(DecksTable.TABLE_NAME, null, values);
    }

    public long addDeck(String label, String publisher, long creationTimestamp, String externalId,
                        String hash, boolean locked, String srcLanguageIso) {
        return addDeck(dbh.getWritableDatabase(), label, publisher, creationTimestamp, externalId,
                hash, locked, srcLanguageIso);
    }

    public void deleteDeck(long deckId) {
        Dictionary[] dictionaries = getAllDictionariesForDeck(deckId);
        for (Dictionary dictionary : dictionaries) {
            // Delete all the files.
            for (int i = 0; i < dictionary.getTranslationCount(); i++) {
                Translation translation = dictionary.getTranslation(i);
                if (translation.getIsAsset()) {
                    // Don't delete the built-in assets.
                    continue;
                }
                File file = new File(translation.getFilename());
                if (file.exists()) {
                    // It should always exist, but check to be safe.
                    file.delete();
                }
            }
            // Delete the rows in the translations table.
            String whereClause = TranslationsTable.DICTIONARY_ID + " = ?";
            String[] whereArgs = new String[] {String.valueOf(dictionary.getDbId())};
            dbh.getWritableDatabase().delete(TranslationsTable.TABLE_NAME, whereClause, whereArgs);
        }
        // Delete the rows in the dictionaries table.
        String whereClause = DictionariesTable.DECK_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(deckId)};
        dbh.getWritableDatabase().delete(DictionariesTable.TABLE_NAME, whereClause, whereArgs);
        // Delete the row from the deck table.
        whereClause = DecksTable.ID + " = ?"; // whereArgs remain the same
        dbh.getWritableDatabase().delete(DecksTable.TABLE_NAME, whereClause, whereArgs);
    }

    public long addDictionary(SQLiteDatabase writableDatabase, String destIsoCode, String label,
                              int itemIndex, long deckId) {
        ContentValues values = new ContentValues();
        values.put(DictionariesTable.LANGUAGE_ISO, destIsoCode);
        values.put(DictionariesTable.LABEL, label);
        values.put(DictionariesTable.ITEM_INDEX, itemIndex);
        values.put(DictionariesTable.DECK_ID, deckId);
        return writableDatabase.insert(DictionariesTable.TABLE_NAME, null, values);
    }

    public long addDictionary(String destIsoCode, String label, int itemIndex, long deckId) {
        return addDictionary(dbh.getWritableDatabase(), destIsoCode, label, itemIndex, deckId);
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
        long translationId = addTranslation(
                dbh.getWritableDatabase(), dictionaryId, label, isAsset, filename, itemIndex, translatedText);
        dbh.close();
        return translationId;
    }

    public long addTranslationAtTop(
            long dictionaryId, String label, boolean isAsset, String filename, String translatedText) {
        String maxColumnName = String.format("MAX(%s)", TranslationsTable.ITEM_INDEX);
        Cursor cursor = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, new String[]{maxColumnName},
                String.format("%s = ?", TranslationsTable.DICTIONARY_ID),
                new String[]{String.format("%d", dictionaryId)},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return addTranslation(dictionaryId, label, isAsset, filename, 0, translatedText);
        }
        int itemIndex = cursor.getInt(cursor.getColumnIndex(maxColumnName)) + 1;
        cursor.close();
        dbh.close();
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
        dbh.close();
    }

    public void deleteTranslation(long translationId) {

        String whereClause = String.format("%s = ?", TranslationsTable.ID);
        String[] whereArgs = new String[] {String.format("%d", translationId)};
        dbh.getWritableDatabase().delete(TranslationsTable.TABLE_NAME, whereClause, whereArgs);
        dbh.close();
    }

    public Deck[] getAllDecks() {
        Cursor cursor = dbh.getReadableDatabase().query(
                DecksTable.TABLE_NAME, null,
                null, null, null, null,
                String.format("%s DESC", DecksTable.ID));
        Deck[] decks = new Deck[cursor.getCount()];
        boolean hasNext = cursor.moveToFirst();
        int i = 0;
        while(hasNext){
            Deck deck = new Deck(
                    cursor.getString(cursor.getColumnIndex(DecksTable.LABEL)),
                    cursor.getString(cursor.getColumnIndex(DecksTable.PUBLISHER)),
                    cursor.getString(cursor.getColumnIndex(DecksTable.EXTERNAL_ID)),
                    cursor.getLong(cursor.getColumnIndex(DecksTable.ID)),
                    cursor.getLong(cursor.getColumnIndex(DecksTable.CREATION_TIMESTAMP)),
                    cursor.getInt(cursor.getColumnIndex(DecksTable.LOCKED)) == 1,
                    cursor.getString(cursor.getColumnIndex(DecksTable.SOURCE_LANGUAGE_ISO)));

            decks[i] = deck;
            hasNext = cursor.moveToNext();
            i++;
        }
        cursor.close();
        dbh.close();
        return decks;
    }

    public boolean hasDeckWithHash(String hash) {
        String[] columns = new String[] {DecksTable.ID};
        String selection = DecksTable.HASH + " = ?";
        String[] selectionArgs = new String[] {hash};
        Cursor cursor = dbh.getReadableDatabase().query(
                DecksTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }

    public long hasDeckWithExternalId(String externalId) {
        // TODO(nworden): consider handling this better when there's multiple existing decks with
        // this external ID
        String[] columns = new String[] {DecksTable.ID};
        String selection = DecksTable.EXTERNAL_ID + " = ?";
        String[] selectionArgs = new String[] {externalId};
        Cursor cursor = dbh.getReadableDatabase().query(
                DecksTable.TABLE_NAME, columns, selection, selectionArgs, null, null,
                String.format("%s DESC", DecksTable.CREATION_TIMESTAMP), "1");
        if (cursor.getCount() == 0) {
            cursor.close();
            return -1;
        }
        cursor.moveToFirst();
        long result = cursor.getLong(cursor.getColumnIndexOrThrow(DecksTable.ID));
        cursor.close();
        return result;
    }

    private Translation[] getTranslationsByDictionaryId(long dictionaryId) {
        Cursor cursor = dbh.getReadableDatabase().query(TranslationsTable.TABLE_NAME, null,
                TranslationsTable.DICTIONARY_ID + " = ?", new String[]{String.valueOf(dictionaryId)},
                null, null, String.format("%s DESC", TranslationsTable.ITEM_INDEX));
        Translation[] translations = new Translation[cursor.getCount()];
        boolean hasNext = cursor.moveToFirst();
        int i=0;
        while(hasNext){
            Translation translation = new Translation(
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.LABEL)),
                    cursor.getInt(cursor.getColumnIndex(TranslationsTable.IS_ASSET)) == 1,
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.FILENAME)),
                    cursor.getLong(cursor.getColumnIndex(TranslationsTable.ID)),
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.TRANSLATED_TEXT))
            );
            translations[i] = translation;
            i++;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        dbh.close();
        return translations;
    }

    public void saveTranslationContext(NewTranslation context) {
        Translation translation = context.getTranslation();
        if (context.isEdit()) {
            updateTranslation(translation.getDbId(), translation.getLabel(), translation.getIsAsset(), translation.getFilename(), translation.getTranslatedText());
        } else {
            addTranslationAtTop(context.getDictionary().getDbId(), translation.getLabel(), translation.getIsAsset(), translation.getFilename(), translation.getTranslatedText());
        }
    }

    private class DecksTable {
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

    private class DictionariesTable {
        public static final String TABLE_NAME = "dictionaries";
        public static final String ID = "id";
        public static final String DECK_ID = "deckId";
        public static final String LANGUAGE_ISO = "languageIso";
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
            importBundledDeck(db);
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
                importBundledDeck(db);
            }
        }

        private void importBundledDeck(SQLiteDatabase db) {
            JSONObject jsonObject;
            try {
                Context context = MainApplication.getContextFromMainApp();
                InputStream is = context.getAssets().open("card_deck.json");
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();
                jsonObject = new JSONObject(new String(buffer, "UTF-8"));

                TxcImportUtility txcImportUtility = new TxcImportUtility(languageService);
                TxcImportUtility.ImportSpec importSpec = txcImportUtility.buildImportSpec(new File(""), "", jsonObject);
                txcImportUtility.loadAssetData(db, context, importSpec);
            } catch (IOException | JSONException | ImportException e) {
                Log.d(TAG, e.getMessage());
            }
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Do nothing.
        }
    }
}
