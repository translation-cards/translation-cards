package org.mercycorps.translationcards;

import android.app.Application;

import dagger.Component;

@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
    Application getApplication();
}
