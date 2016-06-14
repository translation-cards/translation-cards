package org.mercycorps.translationcards.ui;

import android.content.Context;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
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
        sb.append(dictionaries.get(0).getLanguage().toUpperCase());
        for (int i = 1; i < dictionaries.size(); i++) {
            sb.append(delimiter);
            sb.append(dictionaries.get(i).getLanguage().toUpperCase());
        }
        return sb.toString();
    }

    public static String getLanguageDisplayName(String isoCode) {
        Context context = MainApplication.getContextFromMainApp();
        switch (isoCode) {
            case "ara":
                return context.getString(R.string.name_ar);
            case "eng":
                return context.getString(R.string.name_en);
            case "fas":
                return context.getString(R.string.name_fa);
            case "pus":
                return context.getString(R.string.name_ps);
            default:
                // Better than nothing.
                return isoCode;
        }
    }

//    public static String getIsoForLanguage(String label) {
//        String isoCode = ;
//
//        return iso;
//    }

}
