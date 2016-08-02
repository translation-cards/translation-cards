package org.mercycorps.translationcards;

import android.app.Application;

import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.LanguageService;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @Provides
    DatabaseHelper providesDatabaseHelper(Application application) {
        return new DatabaseHelper(application.getApplicationContext());
    }

    @Provides
    TranslationRepository providesTranslationRepository(DatabaseHelper databaseHelper) {
        return new TranslationRepository(databaseHelper);
    }

    @Provides
    DictionaryRepository providesDictionaryRepository(DatabaseHelper databaseHelper, TranslationRepository translationRepository, LanguageService languageService) {
        return new DictionaryRepository(databaseHelper, translationRepository, languageService);
    }
}
