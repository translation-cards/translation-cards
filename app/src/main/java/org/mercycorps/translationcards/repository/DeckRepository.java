package org.mercycorps.translationcards.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.model.DbManager.DecksTable;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;

import java.io.File;

import static org.mercycorps.translationcards.model.DbManager.TranslationsTable.*;

/**
 * Created by njimenez on 6/27/16.
 */
public class DeckRepository {

    private DbManager.DbHelper databaseHelper;
    private DbManager dbManager;

    public DeckRepository(DbManager.DbHelper databaseHelper, DbManager dbManager) {
        this.databaseHelper = databaseHelper;
        this.dbManager = dbManager;
    }

    public Deck[] getAllDecks() {
        Cursor cursor = databaseHelper.getReadableDatabase().query(
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
        databaseHelper.close();
        return decks;
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
        return addDeck(databaseHelper.getWritableDatabase(), label, publisher, creationTimestamp, externalId,
                hash, locked, srcLanguageIso);
    }

    public void deleteDeck(long deckId) {
        Dictionary[] dictionaries = dbManager.getAllDictionariesForDeck(deckId);
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
            String whereClause = DICTIONARY_ID + " = ?";
            String[] whereArgs = new String[] {String.valueOf(dictionary.getDbId())};
            databaseHelper.getWritableDatabase().delete(TABLE_NAME, whereClause, whereArgs);
        }
        // Delete the rows in the dictionaries table.
        String whereClause = DbManager.DictionariesTable.DECK_ID + " = ?";
        String[] whereArgs = new String[] {String.valueOf(deckId)};
        databaseHelper.getWritableDatabase().delete(DbManager.DictionariesTable.TABLE_NAME, whereClause, whereArgs);
        // Delete the row from the deck table.
        whereClause = DecksTable.ID + " = ?"; // whereArgs remain the same
        databaseHelper.getWritableDatabase().delete(DecksTable.TABLE_NAME, whereClause, whereArgs);
    }

    public long hasDeckWithExternalId(String externalId) {
        // TODO(nworden): consider handling this better when there's multiple existing decks with
        // this external ID
        String[] columns = new String[] {DecksTable.ID};
        String selection = DecksTable.EXTERNAL_ID + " = ?";
        String[] selectionArgs = new String[] {externalId};
        Cursor cursor = databaseHelper.getReadableDatabase().query(
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

    public boolean hasDeckWithHash(String hash) {
        String[] columns = new String[] {DecksTable.ID};
        String selection = DecksTable.HASH + " = ?";
        String[] selectionArgs = new String[] {hash};
        Cursor cursor = databaseHelper.getReadableDatabase().query(
                DecksTable.TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
}