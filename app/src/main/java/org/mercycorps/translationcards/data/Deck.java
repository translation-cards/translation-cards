package org.mercycorps.translationcards.data;

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

    private String label;
    private String publisher;
    private String externalId;
    private long dbId;
    private long timestamp;
    private boolean locked;

    public Deck(String label, String publisher, String externalId, long dbId, long timestamp,
                boolean locked) {
        this.label = label;
        this.publisher = publisher;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
        this.locked = locked;
    }

    public Deck(String label, String publisher, String externalId, long timestamp, boolean locked) {
        this(label, publisher, externalId, -1, timestamp, locked);
    }

    public Deck() {

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

    public long getDbId() {
        return dbId;
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

    public boolean isLocked() {
        return locked;
    }
}
