package org.mercycorps.translationcards.ui;

import org.mercycorps.translationcards.model.Dictionary;

import java.util.List;

/**
 * A utility class for displaying information about languages keyed by ISO code.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class LanguageDisplayUtil {

    public static String getDestLanguageListDisplay(List<Dictionary> dictionaries) {
        if (dictionaries.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dictionaries.get(0).getLanguage().toUpperCase());
        for (int i = 1; i < dictionaries.size(); i++) {
            sb.append("  ");
            sb.append(dictionaries.get(i).getLanguage().toUpperCase());
        }
        return sb.toString();
    }
}
