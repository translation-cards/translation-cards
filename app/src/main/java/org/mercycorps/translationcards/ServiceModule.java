package org.mercycorps.translationcards;

import org.mercycorps.translationcards.service.PermissionService;

import dagger.Module;
import dagger.Provides;

@Module
public class ServiceModule {

    @Provides
    PermissionService providesPermissionService() {
        return new PermissionService();
    }
}