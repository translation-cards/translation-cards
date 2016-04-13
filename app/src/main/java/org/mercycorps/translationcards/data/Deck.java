package org.mercycorps.translationcards.data;

import android.content.Context;
import android.os.Parcelable;

import org.mercycorps.translationcards.MainApplication;

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
    private final long timestamp;
    private final boolean locked;
    private final String srcLanguageIso;
    // The dictionaries list is lazily initialized.
    private Dictionary[] dictionaries;

    public Deck(String label, String publisher, String externalId, long dbId, long timestamp,
                boolean locked, String srcLanguageIso) {
        this.label = label;
        this.publisher = publisher;
        this.externalId = externalId;
        this.dbId = dbId;
        this.timestamp = timestamp;
        this.locked = locked;
        this.srcLanguageIso = srcLanguageIso;
        dictionaries = null;
    }

    public Deck(String label, String publisher, String externalId, long timestamp, boolean locked,
                String srcLanguageIso) {
        this(label, publisher, externalId, -1, timestamp, locked, srcLanguageIso);
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

    public String getSrcLanguageIso() {
        return srcLanguageIso;
    }

    public Dictionary[] getDictionaries(Context context) {
        if (dictionaries == null) {
            dictionaries = ((MainApplication) context.getApplicationContext()).getDbManager().getDictionariesForDeck(dbId);
        }
        return dictionaries;
    }
}
