package org.mercycorps.translationcards;

import org.mercycorps.translationcards.activity.addTranslation.RecordAudioActivity;
import org.mercycorps.translationcards.activity.addTranslation.SummaryActivity;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.view.TranslationCardItem;

import dagger.Component;

@PerActivity
@Component(modules = {MediaModule.class}, dependencies = ApplicationComponent.class)
public interface BaseComponent {
    void inject(TranslationsActivity activity);
    void inject(SummaryActivity activity);
    void inject(RecordAudioActivity activity);
    void inject(TranslationCardItem cardItemView);
}
