package org.mercycorps.translationcards;

import org.mercycorps.translationcards.activity.addTranslation.RecordAudioActivityTest;
import org.mercycorps.translationcards.activity.addTranslation.SummaryActivityTest;
import org.mercycorps.translationcards.view.TranslationCardItem;
import org.mercycorps.translationcards.view.TranslationCardItemTest;

import dagger.Component;

@Component(dependencies = {TestBaseComponent.class})
public interface TestActivityInjectorComponent {

    void inject(SummaryActivityTest activity);
    void inject(RecordAudioActivityTest activity);
    void inject(TranslationCardItemTest activity);
}