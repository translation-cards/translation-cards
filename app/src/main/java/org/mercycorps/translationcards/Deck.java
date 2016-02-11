package org.mercycorps.translationcards;

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

    private final String label;
    private final String publisher;
    private final String externalId;
    private final long dbId;
    private long timestamp;

    public Deck(String label, String publisher, String externalId, long dbId, long timestamp) {
        this.label = label;
        this.publisher = publisher;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
    }

    public Deck(String label, String publisher, String externalId, long timestamp) {
        this(label, publisher, externalId, -1, timestamp);
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
}
