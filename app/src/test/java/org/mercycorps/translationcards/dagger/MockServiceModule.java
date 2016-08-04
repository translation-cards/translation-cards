package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.PermissionService;

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
    DeckService providesDeckService() {
        return mock(DeckService.class);
    }
}
