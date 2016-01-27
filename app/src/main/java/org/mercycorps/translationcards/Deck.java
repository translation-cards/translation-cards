package org.mercycorps.translationcards;

/**
 * Contains information about a collection of phrases in one or more languages.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Deck {

    private final String label;
    private final String publisher;
    private final long[] dictionaryIds;
    private final long dbId;

    public Deck(String label, String publisher, long[] dictionaryIds, long dbId) {
        this.label = label;
        this.publisher = publisher;
        this.dictionaryIds = dictionaryIds;
        this.dbId = dbId;
    }

    public Deck(String label, String publisher, long[] dictionaryIds) {
        this(label, publisher, dictionaryIds, -1);
    }

    public String getLabel() {
        return label;
    }

    public String getPublisher() {
        return publisher;
    }

    public long[] getDictionaryIds() {
        return dictionaryIds;
    }

    public long getDbId() {
        return dbId;
    }
}
