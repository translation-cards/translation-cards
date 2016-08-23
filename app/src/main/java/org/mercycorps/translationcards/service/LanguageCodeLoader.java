package org.mercycorps.translationcards.service;

import android.util.Log;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.porting.LanguagesImportUtility;

import java.io.IOException;
import java.io.InputStream;

public class LanguageCodeLoader {

    private static final String TAG = LanguageCodeLoader.class.getName();

    public static LanguagesImportUtility createLanguagesImportUtility() {
        InputStream inputStream;
        try {
            inputStream = MainApplication.getContextFromMainApp().getAssets().open("language_codes.json");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            inputStream = null;
        }
        LanguagesImportUtility languagesImportUtility = new LanguagesImportUtility(inputStream);
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return languagesImportUtility;
    }
}
