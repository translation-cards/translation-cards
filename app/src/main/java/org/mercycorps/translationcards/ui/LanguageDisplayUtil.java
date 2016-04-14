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
        return isNullOrEmpty(deck.getLabel()) ?
                getLanguageDisplayName(context, deck.getSrcLanguageIso()) : deck.getLabel();
    }

    public static String getDestLanguageDisplayName(Context context, Dictionary dictionary) {
        return isNullOrEmpty(dictionary.getLabel()) ?
                getLanguageDisplayName(context, dictionary.getDestLanguageIso()) :
                dictionary.getLabel();
    }

    public static String getDestLanguageListDisplay(Context context, Deck deck, String delimiter) {
        Dictionary[] dictionaries = deck.getDictionaries(context);
        if (dictionaries.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getDestLanguageDisplayName(context, dictionaries[0]).toUpperCase());
        for (int i = 1; i < dictionaries.length; i++) {
            sb.append(delimiter);
            sb.append(getDestLanguageDisplayName(context, dictionaries[i]).toUpperCase());
        }
        return sb.toString();
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
                // Better than nothing.
                return isoCode;
        }
    }

    private static boolean isNullOrEmpty(String val) {
        return (val == null) || val.isEmpty();
    }
}
