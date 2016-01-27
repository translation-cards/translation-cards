package org.mercycorps.translationcards;

/**
 * Contains information about a collection of phrases in one or more languages.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Deck {

    private final String label;
    private final String publisher;
    private final Dictionary[] dictionaries;
    private final long dbId;

    public Deck(String label, String publisher, Dictionary[] dictionaries, long dbId) {
        this.label = label;
        this.publisher = publisher;
        this.dictionaries = dictionaries;
        this.dbId = dbId;
    }

    public Deck(String label, String publisher, Dictionary[] dictionaries) {
        this(label, publisher, dictionaries, -1);
    }

    public String getLabel() {
        return label;
    }

    public String getPublisher() {
        return publisher;
    }

    public Dictionary[] getDictionaries() {
        return dictionaries;
    }

    public long getDbId() {
        return dbId;
    }
}
