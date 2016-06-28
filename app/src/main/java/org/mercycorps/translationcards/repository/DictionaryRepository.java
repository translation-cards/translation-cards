package org.mercycorps.translationcards.repository;

import android.database.Cursor;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.model.Dictionary;

import static org.mercycorps.translationcards.model.DbManager.DictionariesTable.*;

/**
 * Created by njimenez on 6/28/16.
 */

public class DictionaryRepository {

    private DbManager.DbHelper databaseHelper;
    private DbManager dbManager;

    public DictionaryRepository(DbManager dbManager) {
        this.databaseHelper = dbManager.getDbh();
        this.dbManager = dbManager;
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
                    dbManager.getTranslationsByDictionaryId(dictionaryId), dictionaryId, deckId);
            dictionaries[i] = dictionary;
            i++;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        return dictionaries;
    }

}
