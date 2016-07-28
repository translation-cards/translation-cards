package org.mercycorps.translationcards;

import org.mercycorps.translationcards.activity.addTranslation.SummaryActivity;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.view.TranslationCardItem;

import dagger.Component;

@Component(dependencies = {BaseComponent.class})
public interface ActivityInjectorComponent {

    void inject(TranslationsActivity activity);
    void inject(SummaryActivity activity);
    void inject(TranslationCardItem cardItemView);
}