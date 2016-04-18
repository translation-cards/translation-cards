package org.mercycorps.translationcards.ui;

import android.content.Context;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

/**
 * A utility class for displaying information about languages keyed by ISO code.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class LanguageDisplayUtil {

    public static String getDestLanguageListDisplay(Deck deck, String delimiter) {
        Dictionary[] dictionaries = deck.getDictionaries();
        if (dictionaries.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dictionaries[0].getLabel().toUpperCase());
        for (int i = 1; i < dictionaries.length; i++) {
            sb.append(delimiter);
            sb.append(dictionaries[i].getLabel().toUpperCase());
        }
        return sb.toString();
    }

    public static String getLanguageDisplayName(String isoCode) {
        Context context = MainApplication.getContextFromMainApp();
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
}
