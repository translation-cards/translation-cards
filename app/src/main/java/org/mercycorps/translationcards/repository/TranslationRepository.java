package org.mercycorps.translationcards.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;

import java.util.ArrayList;
import java.util.List;

import static org.mercycorps.translationcards.repository.DatabaseHelper.TranslationsTable.*;

public class TranslationRepository {

    private static final String TAG = TranslationRepository.class.getName();

    private DatabaseHelper databaseHelper;

    public TranslationRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public List<Translation> getTranslationsForDictionary(Dictionary dictionary) {
        List<Translation> translations = new ArrayList<>();
        for(int i = 0; i < dictionary.getTranslationCount(); i++) {
            translations.add(dictionary.getTranslation(i));
        }

        return translations;
    }

    public void deleteTranslation(long translationId) {
        String whereClause = String.format("%s = ?", ID);
        String[] whereArgs = new String[] {String.format("%d", translationId)};
        databaseHelper.getWritableDatabase().delete(TABLE_NAME, whereClause, whereArgs);
        databaseHelper.close();
    }

    private long addTranslation(SQLiteDatabase writableDatabase,
                               long dictionaryId, String label, boolean isAsset, String filename, int itemIndex, String translatedText) {
        Log.d(TAG, "Inserting translation...");
        ContentValues values = new ContentValues();
        values.put(DICTIONARY_ID, dictionaryId);
        values.put(LABEL, label);
        values.put(IS_ASSET, isAsset ? 1 : 0);
        values.put(FILENAME, filename);
        values.put(ITEM_INDEX, itemIndex);
        values.put(TRANSLATED_TEXT, translatedText);
        return writableDatabase.insert(TABLE_NAME, null, values);
    }

    public long addTranslation(
            long dictionaryId, String label, boolean isAsset, String filename, int itemIndex, String translatedText) {
        long translationId = addTranslation(
                databaseHelper.getWritableDatabase(), dictionaryId, label, isAsset, filename, itemIndex, translatedText);
        databaseHelper.close();
        return translationId;
    }

    public long addTranslationAtTop(
            long dictionaryId, String label, boolean isAsset, String filename, String translatedText) {
        String maxColumnName = String.format("MAX(%s)", ITEM_INDEX);
        Cursor cursor = databaseHelper.getReadableDatabase().query(
                TABLE_NAME, new String[]{maxColumnName},
                String.format("%s = ?", DICTIONARY_ID),
                new String[]{String.format("%d", dictionaryId)},
                null, null, null);
        if (!cursor.moveToFirst()) {
            return addTranslation(dictionaryId, label, isAsset, filename, 0, translatedText);
        }
        int itemIndex = cursor.getInt(cursor.getColumnIndex(maxColumnName)) + 1;
        cursor.close();
        databaseHelper.close();
        return addTranslation(dictionaryId, label, isAsset, filename, itemIndex, translatedText);
    }

    public void updateTranslation(
            long translationId, String label, boolean isAsset, String filename, String translatedText) {
        ContentValues values = new ContentValues();
        values.put(LABEL, label);
        values.put(IS_ASSET, isAsset);
        values.put(FILENAME, filename);
        values.put(TRANSLATED_TEXT, translatedText);
        String whereClause = String.format("%s = ?", ID);
        String[] whereArgs = new String[] {String.format("%d", translationId)};
        databaseHelper.getWritableDatabase().update(
                TABLE_NAME, values, whereClause, whereArgs);
        databaseHelper.close();
    }

    public Translation[] getTranslationsByDictionaryId(long dictionaryId) {
        Cursor cursor = databaseHelper.getReadableDatabase().query(TABLE_NAME, null,
                DICTIONARY_ID + " = ?", new String[]{String.valueOf(dictionaryId)},
                null, null, String.format("%s DESC", ITEM_INDEX));
        Translation[] translations = new Translation[cursor.getCount()];
        boolean hasNext = cursor.moveToFirst();
        int i=0;
        while(hasNext){
            Translation translation = new Translation(
                    cursor.getString(cursor.getColumnIndex(LABEL)),
                    cursor.getInt(cursor.getColumnIndex(IS_ASSET)) == 1,
                    cursor.getString(cursor.getColumnIndex(FILENAME)),
                    cursor.getLong(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(TRANSLATED_TEXT))
            );
            translations[i] = translation;
            i++;
            hasNext = cursor.moveToNext();
        }
        cursor.close();
        databaseHelper.close();
        return translations;
    }
}
