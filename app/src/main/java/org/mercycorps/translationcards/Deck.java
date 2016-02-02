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
    private String creationDate;

    public Deck(String label, String publisher, long dbId, String creationDate) {
        this.label = label;
        this.publisher = publisher;
        this.dbId = dbId;
        this.creationDate = creationDate;
    }

    public Deck(String label, String publisher, String creationDate) {
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
        return creationDate;
    }
}
