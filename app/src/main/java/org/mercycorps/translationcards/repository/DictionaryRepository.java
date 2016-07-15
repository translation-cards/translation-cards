package org.mercycorps.translationcards.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.service.LanguageService;

import static org.mercycorps.translationcards.model.DatabaseHelper.DictionariesTable.*;

/**
 * Created by njimenez on 6/28/16.
 */

public class DictionaryRepository {

    private DatabaseHelper databaseHelper;
    private LanguageService languageService;
    private TranslationRepository translationRepository;

    public DictionaryRepository(DatabaseHelper databaseHelper, TranslationRepository translationRepository, LanguageService languageService) {
        this.translationRepository = translationRepository;
        this.databaseHelper = databaseHelper;
        this.languageService = languageService;
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
            boolean isLabelEmpty = cursor.isNull(cursor.getColumnIndex(LABEL));
            String label;
            if (isLabelEmpty) {
                label = languageService.getLanguageDisplayName(destLanguageIso);
            } else {
                label = cursor.getString(cursor.getColumnIndex(LABEL));
            }
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
