package org.mercycorps.translationcards.data;

import android.content.Context;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains information about a collection of phrases in one or more languages.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Deck implements Serializable {

    private long dbId;
    private final String label;
    private final String publisher;
    private final String externalId;
    private final long timestamp;
    private final String hash;
    private final boolean locked;

    Deck(long dbId,
         String label, String publisher,
         String externalId, long timestamp, String hash, boolean locked) {
        this.dbId = dbId;
        this.label = label;
        this.publisher = publisher;
        this.externalId = externalId;
        this.timestamp = timestamp;
        this.hash = hash;
        this.locked = locked;
    }

    public Deck(String label, String publisher,
                String externalId, long timestamp, String hash, boolean locked) {
        this(-1, label, publisher, externalId, timestamp, hash, locked);
    }

    public long getDbId() {
        return dbId;
    }

    public String getLabel() {
        return label;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getExternalId() {
        return externalId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCreationDateString() {
        Date date = new Date(timestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }

    public String getHash() {
        return hash;
    }

    public boolean isLocked() {
        return locked;
    }

    public void save(Context context) {
        DbManager dbm = new DbManager(context);
        if (dbId == -1) {
            dbId = dbm.addDeck(this);
        } else {
            dbm.updateDeck(this);
        }
    }

    public void delete(Context context) {
        DbManager dbm = new DbManager(context);
        if (dbId == -1) {
            throw new IllegalArgumentException("Tried to delete unsaved Deck.");
        }
        dbm.deleteDeck(dbId);
    }
}
