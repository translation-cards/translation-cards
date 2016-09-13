package org.mercycorps.translationcards.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.mercycorps.translationcards.model.Dictionary;

import static org.mercycorps.translationcards.repository.DatabaseHelper.DictionariesTable.DECK_ID;
import static org.mercycorps.translationcards.repository.DatabaseHelper.DictionariesTable.ID;
import static org.mercycorps.translationcards.repository.DatabaseHelper.DictionariesTable.ITEM_INDEX;
import static org.mercycorps.translationcards.repository.DatabaseHelper.DictionariesTable.LABEL;
import static org.mercycorps.translationcards.repository.DatabaseHelper.DictionariesTable.TABLE_NAME;

/**
 * Created by njimenez on 6/28/16.
 */

public class DictionaryRepository {

    private DatabaseHelper databaseHelper;
    private TranslationRepository translationRepository;

    public DictionaryRepository(DatabaseHelper databaseHelper, TranslationRepository translationRepository) {
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
            String label = cursor.getString(cursor.getColumnIndex(LABEL));
            long dictionaryId = cursor.getLong(cursor.getColumnIndex(ID));
            Dictionary dictionary = new Dictionary(label,
                    translationRepository.getTranslationsByDictionaryId(dictionaryId), dictionaryId);
            dictionaries[i] = dictionary;
            i++;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        return dictionaries;
    }

    private long addDictionary(SQLiteDatabase writableDatabase, String languageName,
                               int itemIndex, long deckId) {
        ContentValues values = new ContentValues();
        values.put(LABEL, languageName);
        values.put(ITEM_INDEX, itemIndex);
        values.put(DECK_ID, deckId);
        return writableDatabase.insert(TABLE_NAME, null, values);
    }

    public long addDictionary(String languageName, int itemIndex, long deckId) {
        return addDictionary(databaseHelper.getWritableDatabase(), languageName, itemIndex, deckId);
    }

}
