package org.mercycorps.translationcards.dagger;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.mercycorps.translationcards.porting.LanguagesImportUtility;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.PermissionService;

import java.io.IOException;
import java.io.InputStream;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private static String TAG = ServiceModule.class.getName();

    @Provides
    PermissionService providesPermissionService() {
        return new PermissionService();
    }

    @Provides
    LanguageService providesLanguageService(Application application) {
        return new LanguageService(createLanguagesImportUtility(application.getApplicationContext()));
    }

    LanguagesImportUtility createLanguagesImportUtility(Context context) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open("language_codes.json");
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