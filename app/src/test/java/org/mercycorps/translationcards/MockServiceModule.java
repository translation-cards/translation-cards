package org.mercycorps.translationcards;

import org.mercycorps.translationcards.service.PermissionService;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockServiceModule {

    private final PermissionService permissionService = mock(PermissionService.class);

    @Provides
    PermissionService providesPermissionService() {
        return permissionService;
    }
}
