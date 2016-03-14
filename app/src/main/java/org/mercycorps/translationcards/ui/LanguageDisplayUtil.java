package org.mercycorps.translationcards.ui;

import android.content.Context;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

/**
 * A utility class for displaying information about languages keyed by ISO code.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class LanguageDisplayUtil {

    public static String getSourceLanguageDisplayName(Context context, Deck deck) {
        return (deck.getLabel() == null) ?
                getLanguageDisplayName(context, deck.getSrcLanguageIso()) : deck.getLabel();
    }

    public static String getDestLanguageDisplayName(Context context, Dictionary dictionary) {
        return (dictionary.getLabel() == null) ?
                getLanguageDisplayName(context, dictionary.getDestLanguageIso()) :
                dictionary.getLabel();
    }

    private static String getLanguageDisplayName(Context context, String isoCode) {
        switch (isoCode) {
            case "ar":
                return context.getString(R.string.name_ar);
            case "en":
                return context.getString(R.string.name_en);
            case "fa":
                return context.getString(R.string.name_fa);
            case "ps":
                return context.getString(R.string.name_ps);
            default:
                return null;
        }
    }
}
