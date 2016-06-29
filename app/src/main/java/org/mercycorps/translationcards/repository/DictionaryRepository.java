package org.mercycorps.translationcards.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.model.Dictionary;

import static org.mercycorps.translationcards.model.DbManager.DictionariesTable.*;

/**
 * Created by njimenez on 6/28/16.
 */

public class DictionaryRepository {

    private DbManager.DbHelper databaseHelper;
    private TranslationRepository translationRepository;

    public DictionaryRepository(DbManager.DbHelper databaseHelper, TranslationRepository translationRepository) {
        this.translationRepository = translationRepository;
        this.databaseHelper = databaseHelper;
    }

    public Dictionary[] getAllDictionariesForDeck(long deckId) {
        Cursor cursor = databaseHelper.getReadableDatabase().query(
                TABLE_NAME, null,
                DECK_ID + " = ?",
                new String[]{String.valueOf(deckId)}, null, null,
                String.format("%s ASC", LABEL));

        Dictionary[] dictionaries = new Dictionary[cursor.getCount()];
        boolean hasNext = cursor.moveToFirst();
        int i = 0;
        while (hasNext) {
            String destLanguageIso = cursor.getString(cursor.getColumnIndex(
                    LANGUAGE_ISO));
            String label = cursor.getString(cursor.getColumnIndex(LABEL));
            long dictionaryId = cursor.getLong(cursor.getColumnIndex(ID));
            Dictionary dictionary = new Dictionary(destLanguageIso, label,
                    translationRepository.getTranslationsByDictionaryId(dictionaryId), dictionaryId, deckId);
            dictionaries[i] = dictionary;
            i++;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        return dictionaries;
    }

    public long addDictionary(SQLiteDatabase writableDatabase, String destIsoCode, String label,
                              int itemIndex, long deckId) {
        ContentValues values = new ContentValues();
        values.put(LANGUAGE_ISO, destIsoCode);
        values.put(LABEL, label);
        values.put(ITEM_INDEX, itemIndex);
        values.put(DECK_ID, deckId);
        return writableDatabase.insert(TABLE_NAME, null, values);
    }

    public long addDictionary(String destIsoCode, String label, int itemIndex, long deckId) {
        return addDictionary(databaseHelper.getWritableDatabase(), destIsoCode, label, itemIndex, deckId);
    }

}
