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

import com.google.inject.Inject;

import org.mercycorps.translationcards.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
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

    @Inject
    public DbManager(Context context) {
        this.dbh = new DbHelper(context);
    }

    public Dictionary[] getAllDictionaries() {
        // Getting translations.
        Map<Long, List<Translation>> translations = new HashMap<>();
        String[] columns = {TranslationsTable.ID, TranslationsTable.DICTIONARY_ID,
                TranslationsTable.LABEL, TranslationsTable.IS_ASSET, TranslationsTable.FILENAME,
                TranslationsTable.TRANSLATED_TEXT};
        Cursor cursor = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, columns,
                null, null, null, null,
                String.format("%s DESC", TranslationsTable.ITEM_INDEX));
        boolean hasNext = cursor.moveToFirst();
        while (hasNext) {
            long dictionaryId = cursor.getLong(cursor.getColumnIndex(TranslationsTable.DICTIONARY_ID));
            if (!translations.containsKey(dictionaryId)) {
                translations.put(dictionaryId, new ArrayList<Translation>());
            }
            translations.get(dictionaryId).add(new Translation(
                    cursor.getLong(cursor.getColumnIndex(TranslationsTable.ID)),
                    cursor.getLong(cursor.getColumnIndex(TranslationsTable.DICTIONARY_ID)),
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.LABEL)),
                    cursor.getInt(cursor.getColumnIndex(TranslationsTable.IS_ASSET)) == 1,
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.FILENAME)),
                    cursor.getInt(cursor.getColumnIndex(TranslationsTable.ITEM_INDEX)),
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.TRANSLATED_TEXT))));
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        // Getting languages.
        columns = new String[] {DictionariesTable.ID, DictionariesTable.LABEL,
                DictionariesTable.DECK_ID};
        cursor = dbh.getReadableDatabase().query(
                DictionariesTable.TABLE_NAME, columns,
                null, null, null, null, DictionariesTable.ITEM_INDEX);
        Dictionary[] res = new Dictionary[cursor.getCount()];
        int dictionaryIndex = 0;
        cursor.moveToFirst();
        while (dictionaryIndex < res.length) {
            long dictionaryId = cursor.getLong(cursor.getColumnIndex(DictionariesTable.ID));
            String label = cursor.getString(cursor.getColumnIndex(DictionariesTable.LABEL));
            long deckId = cursor.getLong(cursor.getColumnIndex(DictionariesTable.DECK_ID));
            Translation[] languageTranslations = {};
            if (translations.containsKey(dictionaryId)) {
                languageTranslations = translations.get(dictionaryId)
                        .toArray(new Translation[] {});
            }
            res[dictionaryIndex] = new Dictionary(
                    dictionaryId, deckId, label, dictionaryIndex, languageTranslations);
            cursor.moveToNext();
            dictionaryIndex++;
        }
        return res;
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
            String label = cursor.getString(cursor.getColumnIndex(DictionariesTable.LABEL));
            long dictionaryId = cursor.getLong(cursor.getColumnIndex(DictionariesTable.ID));
            Dictionary dictionary = new Dictionary(
                    dictionaryId, deckId, label, i, getTranslationsByDictionaryId(dictionaryId));
            dictionaries[i] = dictionary;
            i++;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        return dictionaries;
    }

    // Translation modification methods.

    long addTranslation(SQLiteDatabase writableDatabase, Translation translation) {
        ContentValues values = getTranslationContentValues(translation);
        return writableDatabase.insert(TranslationsTable.TABLE_NAME, null, values);
    }

    long addTranslation(Translation translation) {
        return addTranslation(dbh.getWritableDatabase(), translation);
    }

    void updateTranslation(Translation translation) {
        ContentValues values = getTranslationContentValues(translation);
        String whereClause = TranslationsTable.ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(translation.getDbId())};
        dbh.getWritableDatabase().update(
                TranslationsTable.TABLE_NAME, values, whereClause, whereArgs);
    }

    void deleteTranslation(Translation translation) {
        if (!translation.getIsAsset()) {
            File file = new File(translation.getFilename());
            if (file.exists()) {
                // It should always exist, but check to be safe.
                file.delete();
            }
        }
        String whereClause = TranslationsTable.ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(translation.getDbId())};
        dbh.getWritableDatabase().delete(TranslationsTable.TABLE_NAME, whereClause, whereArgs);
    }

    private ContentValues getTranslationContentValues(Translation translation) {
        ContentValues values = new ContentValues();
        values.put(TranslationsTable.DICTIONARY_ID, translation.getDictionaryId());
        values.put(TranslationsTable.LABEL, translation.getLabel());
        values.put(TranslationsTable.IS_ASSET, translation.getIsAsset() ? 1 : 0);
        values.put(TranslationsTable.FILENAME, translation.getFilename());
        values.put(TranslationsTable.ITEM_INDEX, translation.getItemIndex());
        values.put(TranslationsTable.TRANSLATED_TEXT, translation.getTranslatedText());
        return values;
    }

    // Dictionary modification methods.

    long addDictionary(SQLiteDatabase writableDatabase, Dictionary dictionary) {
        ContentValues values = getDictionaryContentValues(dictionary);
        return writableDatabase.insert(DictionariesTable.TABLE_NAME, null, values);
    }

    long addDictionary(Dictionary dictionary) {
        return addDictionary(dbh.getWritableDatabase(), dictionary);
    }

    void updateDictionary(Dictionary dictionary) {
        ContentValues values = getDictionaryContentValues(dictionary);
        String whereClause = DictionariesTable.ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(dictionary.getDbId())};
        dbh.getWritableDatabase().update(
                DictionariesTable.TABLE_NAME, values, whereClause, whereArgs);
    }

    void deleteDictionary(Dictionary dictionary) {
        for (int i = 0; i < dictionary.getTranslationCount(); i++) {
            deleteTranslation(dictionary.getTranslation(i));
        }
        String whereClause = DictionariesTable.TABLE_NAME + " = ?";
        String[] whereArgs = new String[] {String.valueOf(dictionary.getDbId())};
        dbh.getWritableDatabase().delete(DictionariesTable.TABLE_NAME, whereClause, whereArgs);
    }

    private ContentValues getDictionaryContentValues(Dictionary dictionary) {
        ContentValues values = new ContentValues();
        values.put(DictionariesTable.DECK_ID, dictionary.getDeckId());
        values.put(DictionariesTable.LABEL, dictionary.getLabel());
        values.put(DictionariesTable.ITEM_INDEX, dictionary.getItemIndex());
        return values;
    }

    // Deck modification methods.

    long addDeck(SQLiteDatabase writableDatabase, Deck deck) {
        ContentValues values = getDeckContentValues(deck);
        return writableDatabase.insert(DecksTable.TABLE_NAME, null, values);
    }

    long addDeck(Deck deck) {
        return addDeck(dbh.getWritableDatabase(), deck);
    }

    void updateDeck(Deck deck) {
        ContentValues values = getDeckContentValues(deck);
        String whereClause = DecksTable.ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(deck.getDbId())};
        dbh.getWritableDatabase().update(DecksTable.TABLE_NAME, values, whereClause, whereArgs);
    }

    void deleteDeck(long deckId) {
        Dictionary[] dictionaries = getAllDictionariesForDeck(deckId);
        for (Dictionary dictionary : dictionaries) {
            deleteDictionary(dictionary);
        }
        // Delete the row from the deck table.
        String whereClause = DecksTable.ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(deckId)};
        dbh.getWritableDatabase().delete(DecksTable.TABLE_NAME, whereClause, whereArgs);
    }

    private ContentValues getDeckContentValues(Deck deck) {
        ContentValues values = new ContentValues();
        values.put(DecksTable.LABEL, deck.getLabel());
        values.put(DecksTable.PUBLISHER, deck.getPublisher());
        values.put(DecksTable.CREATION_TIMESTAMP, deck.getTimestamp());
        values.put(DecksTable.EXTERNAL_ID, deck.getExternalId());
        values.put(DecksTable.HASH, deck.getHash());
        values.put(DecksTable.LOCKED, deck.isLocked() ? 1 : 0);
        return values;
    }

    // Translation query methods.

    Translation getTranslationById(long id) {
        String whereClause = TranslationsTable.ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        Cursor cursor = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        Translation translation = null;
        if (cursor.moveToFirst()) {
            translation = getTranslationFromCursor(cursor);
        }
        cursor.close();
        return translation;
    }

    Translation[] getAllTranslationsForDictionary(long dictionaryId) {
        String whereClause = TranslationsTable.DICTIONARY_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(dictionaryId)};
        Cursor cursor = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, null, whereClause, whereArgs, null, null, null);
        Translation[] translations = new Translation[cursor.getCount()];
        int i = 0;
        boolean hasNext = cursor.moveToFirst();
        while (hasNext) {
            translations[i] = getTranslationFromCursor(cursor);
            i++;
            hasNext = cursor.moveToNext();
        }
        return translations;
    }

    private Translation getTranslationFromCursor(Cursor cursor) {
        return new Translation(
                cursor.getLong(cursor.getColumnIndex(TranslationsTable.ID)),
                cursor.getLong(cursor.getColumnIndex(TranslationsTable.DICTIONARY_ID)),
                cursor.getString(cursor.getColumnIndex(TranslationsTable.LABEL)),
                cursor.getInt(cursor.getColumnIndex(TranslationsTable.IS_ASSET)) == 1,
                cursor.getString(cursor.getColumnIndex(TranslationsTable.FILENAME)),
                cursor.getInt(cursor.getColumnIndex(TranslationsTable.ITEM_INDEX)),
                cursor.getString(cursor.getColumnIndex(TranslationsTable.TRANSLATED_TEXT)));
    }

    int getTopTranslationIndex(long dictionaryId) {
        int result = 0;
        String maxColumnName = String.format("MAX(%s)", TranslationsTable.ITEM_INDEX);
        Cursor cursor = dbh.getReadableDatabase().query(
                TranslationsTable.TABLE_NAME, new String[]{maxColumnName},
                String.format("%s = ?", TranslationsTable.DICTIONARY_ID),
                new String[]{String.format("%d", dictionaryId)},
                null, null, null);
        if (cursor.moveToFirst()) {
            result = cursor.getInt(cursor.getColumnIndex(maxColumnName)) + 1;
        }
        cursor.close();
        dbh.close();
        return result;
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
                    cursor.getLong(cursor.getColumnIndex(DecksTable.ID)),
                    cursor.getString(cursor.getColumnIndex(DecksTable.LABEL)),
                    cursor.getString(cursor.getColumnIndex(DecksTable.PUBLISHER)),
                    cursor.getString(cursor.getColumnIndex(DecksTable.EXTERNAL_ID)),
                    cursor.getLong(cursor.getColumnIndex(DecksTable.CREATION_TIMESTAMP)),
                    cursor.getString(cursor.getColumnIndex(DecksTable.HASH)),
                    cursor.getInt(cursor.getColumnIndex(DecksTable.LOCKED)) == 1);

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
                    cursor.getLong(cursor.getColumnIndex(TranslationsTable.ID)),
                    cursor.getLong(cursor.getColumnIndex(TranslationsTable.DICTIONARY_ID)),
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.LABEL)),
                    cursor.getInt(cursor.getColumnIndex(TranslationsTable.IS_ASSET)) == 1,
                    cursor.getString(cursor.getColumnIndex(TranslationsTable.FILENAME)),
                    cursor.getInt(cursor.getColumnIndex(TranslationsTable.ITEM_INDEX)),
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

    public String getTranslationLanguagesForDeck(long deckDbId) {
        Cursor cursor = dbh.getReadableDatabase().query(
                DictionariesTable.TABLE_NAME,
                new String[]{DictionariesTable.LABEL},
                DictionariesTable.DECK_ID + " = ?",
                new String[]{String.valueOf(deckDbId)}, null, null,
                String.format("%s ASC", DictionariesTable.LABEL));

        String translationLanguages = "";
        String delimiter = "   ";
        boolean hasNext = cursor.moveToFirst();
        while(hasNext) {
            String translationLanguage = cursor.getString(cursor.getColumnIndex(DictionariesTable.LABEL));
            translationLanguages += translationLanguage.toUpperCase() + delimiter;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        dbh.close();
        return translationLanguages.trim();
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
                DecksTable.PUBLISHER + " TEXT," +
                DecksTable.CREATION_TIMESTAMP + " INTEGER," +
                DecksTable.EXTERNAL_ID + " TEXT," +
                DecksTable.HASH + " TEXT," +
                DecksTable.LOCKED + " INTEGER" +
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
                "ALTER TABLE " + TranslationsTable.TABLE_NAME + " ADD " +
                TranslationsTable.TRANSLATED_TEXT + " TEXT";
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
            long creationTimestamp = (new Date()).getTime();
            Deck defaultDeck = new Deck(
                    context.getString(R.string.data_default_deck_name),
                    context.getString(R.string.data_default_deck_publisher),
                    null, creationTimestamp, null, false);
            addDeck(db, defaultDeck);
            populateIncludedData(db, defaultDeck);
        }

        private void populateIncludedData(SQLiteDatabase db, Deck defaultDeck) {
            for (int dictionaryIndex = 0; dictionaryIndex < INCLUDED_DATA.length;
                 dictionaryIndex++) {
                Dictionary dictionary = INCLUDED_DATA[dictionaryIndex];
                dictionary.setDeck(defaultDeck);
                addDictionary(db, dictionary);
                for (int translationIndex = 0;
                     translationIndex < dictionary.getTranslationCount();
                     translationIndex++) {
                    Translation translation = dictionary.getTranslation(translationIndex);
                    translation.setItemIndex(
                            dictionary.getTranslationCount() - translationIndex - 1);
                    translation.setDictionary(dictionary);
                    addTranslation(db, translation);
                }
            }
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Translation text and the decks table were added in v2 of the database.
            if (oldVersion == 1) {
                db.execSQL(ALTER_TABLE_ADD_TRANSLATED_TEXT_COLUMN);
                db.execSQL(INIT_DECKS_SQL);
                db.execSQL(ALTER_TABLE_ADD_DECK_FOREIGN_KEY);
                long creationTimestamp = (new Date()).getTime() / 1000;
                Deck defaultDeck = new Deck(
                        context.getString(R.string.data_default_deck_name),
                        context.getString(R.string.data_default_deck_publisher),
                        null, creationTimestamp, null, false);
                long defaultDeckId = addDeck(db, defaultDeck);
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
            new Dictionary(NO_VALUE_ID, NO_VALUE_ID, "Arabic", 0, new Translation[] {}),
            new Dictionary(NO_VALUE_ID, NO_VALUE_ID, "Farsi", 1, new Translation[] {}),
            new Dictionary(NO_VALUE_ID, NO_VALUE_ID, "Pashto", 2, new Translation[] {}),
    };
}
