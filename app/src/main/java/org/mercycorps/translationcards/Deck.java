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
    private final long dbId;
    private long creationDate;

    public Deck(String label, String publisher, long dbId, long creationDate) {
        this.label = label;
        this.publisher = publisher;
        this.dbId = dbId;
        this.creationDate = creationDate;
    }

    public Deck(String label, String publisher, long creationDate) {
        this(label, publisher, -1, creationDate);
    }

    public String getLabel() {
        return label;
    }

    public String getPublisher() {
        return publisher;
    }

    public long getDbId() {
        return dbId;
    }

    public String getCreationDate() {
        Date date = new Date(creationDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy");
        String formattedDate = dateFormat.format(date);
        return formattedDate;
    }
}
