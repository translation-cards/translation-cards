package org.mercycorps.translationcards;

/**
 * Contains information about a collection of phrases in one or more languages.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Deck {

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

    public long getCreationDate() {
        return creationDate;
    }
}
