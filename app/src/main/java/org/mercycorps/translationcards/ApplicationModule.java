package org.mercycorps.translationcards;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application providesApplication() {
        return mApplication;
    }
}
