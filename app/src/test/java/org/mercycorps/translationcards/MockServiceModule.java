package org.mercycorps.translationcards;

import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.PermissionService;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockServiceModule {

    private final PermissionService permissionService = mock(PermissionService.class);
    private final LanguageService languageService = mock(LanguageService.class);

    @Provides
    PermissionService providesPermissionService() {
        return permissionService;
    }

    @Provides
    LanguageService providesLanguageService() {
        return languageService;
    }
}
