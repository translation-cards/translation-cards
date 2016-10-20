package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.activity.Router;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.PermissionService;
import org.mercycorps.translationcards.service.TranslationService;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockServiceModule {

    @PerApplication
    @Provides
    PermissionService providesPermissionService() {
        return mock(PermissionService.class);
    }

    @PerApplication
    @Provides
    LanguageService providesLanguageService() {
        return mock(LanguageService.class);
    }

    @PerApplication
    @Provides
    DictionaryService providesDictionaryService() {
        return mock(DictionaryService.class);
    }

    @PerApplication
    @Provides
    TranslationService providesTranslationService() {
        return mock(TranslationService.class);
    }

    @PerApplication
    @Provides
    DeckService providesDeckService() {
        return mock(DeckService.class);
    }

    @PerApplication
    @Provides
    TxcImportUtility providesTxcImportUtility() {
        return mock(TxcImportUtility.class);
    }

    @PerApplication
    @Provides
    Router providesRouter() {
        return mock(Router.class);
    }
}
