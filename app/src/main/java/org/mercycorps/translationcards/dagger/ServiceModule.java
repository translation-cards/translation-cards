package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.porting.LanguagesImportUtility;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.LanguageCodeLoader;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.PermissionService;
import org.mercycorps.translationcards.service.TranslationService;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    private static String TAG = ServiceModule.class.getName();

    @Provides
    PermissionService providesPermissionService() {
        return new PermissionService();
    }

    @PerApplication
    @Provides
    DeckService providesDeckService(DeckRepository deckRepository) {
        return new DeckService(deckRepository);
    }

    @PerApplication
    @Provides
    DictionaryService providesDictionaryService(DictionaryRepository dictionaryRepository, DeckService deckService) {
        return new DictionaryService(dictionaryRepository, deckService);
    }

    @PerApplication
    @Provides
    TranslationService providesTranslationService(TranslationRepository translationRepository, DictionaryService dictionaryService) {
        return new TranslationService(translationRepository, dictionaryService);
    }

    @Provides
    LanguagesImportUtility providesLanguagesImportUtility() {
        return LanguageCodeLoader.createLanguagesImportUtility();
    }

    @PerApplication
    @Provides
    LanguageService providesLanguageService(LanguagesImportUtility languagesImportUtility) {
        return new LanguageService(languagesImportUtility);
    }

    @Provides
    TxcImportUtility providesTxcImportUtility(LanguageService languageService, DeckRepository deckRepository, DictionaryRepository dictionaryRepository, TranslationRepository translationRepository) {
        return new TxcImportUtility(languageService, deckRepository, translationRepository, dictionaryRepository);
    }
}