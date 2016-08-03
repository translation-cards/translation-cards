package org.mercycorps.translationcards.dagger;

import android.app.Application;

import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.LanguageService;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {
    @PerApplication
    @Provides
    DatabaseHelper providesDatabaseHelper(Application application) {
        return new DatabaseHelper(application.getApplicationContext());
    }

    @PerApplication
    @Provides
    TranslationRepository providesTranslationRepository(DatabaseHelper databaseHelper) {
        return new TranslationRepository(databaseHelper);
    }

    @PerApplication
    @Provides
    DictionaryRepository providesDictionaryRepository(DatabaseHelper databaseHelper, TranslationRepository translationRepository, LanguageService languageService) {
        return new DictionaryRepository(databaseHelper, translationRepository, languageService);
    }

    @PerApplication
    @Provides
    DeckRepository providesDeckRepository(DictionaryRepository dictionaryRepository, DatabaseHelper databaseHelper, LanguageService languageService) {
        return new DeckRepository(dictionaryRepository, databaseHelper, languageService);
    }
}
