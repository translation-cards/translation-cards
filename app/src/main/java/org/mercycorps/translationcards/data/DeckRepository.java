package org.mercycorps.translationcards.data;

import android.database.Cursor;
import org.mercycorps.translationcards.data.DbManager.DecksTable;

/**
 * Created by njimenez on 6/27/16.
 */
public class DeckRepository {

    private DbManager.DbHelper databaseHelper;

    public DeckRepository(DbManager.DbHelper databaseHelper) {

        this.databaseHelper = databaseHelper;
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
}
