package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.repository.DatabaseHelper;
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
    DatabaseHelper providesDatabaseHelper(LanguageService languageService) {
        return new DatabaseHelper(languageService);
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
