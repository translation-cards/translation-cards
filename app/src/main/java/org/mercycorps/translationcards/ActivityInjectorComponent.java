package org.mercycorps.translationcards;

import org.mercycorps.translationcards.activity.translations.TranslationsActivity;

import dagger.Component;

@Component(dependencies = {BaseComponent.class})
public interface ActivityInjectorComponent {

    void inject(TranslationsActivity activity);
}