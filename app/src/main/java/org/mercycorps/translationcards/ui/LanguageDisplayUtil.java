package org.mercycorps.translationcards.ui;

import android.content.Context;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.List;

/**
 * A utility class for displaying information about languages keyed by ISO code.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class LanguageDisplayUtil {

    public static String getDestLanguageListDisplay(List<Dictionary> dictionaries, String delimiter) {
        if (dictionaries.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dictionaries.get(0).getLabel().toUpperCase());
        for (int i = 1; i < dictionaries.size(); i++) {
            sb.append(delimiter);
            sb.append(dictionaries.get(i).getLabel().toUpperCase());
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
