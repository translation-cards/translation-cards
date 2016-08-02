package org.mercycorps.translationcards;

import org.mercycorps.translationcards.activity.ImportActivity;
import org.mercycorps.translationcards.activity.addDeck.ReviewAndSaveActivityTest;
import org.mercycorps.translationcards.activity.addTranslation.RecordAudioActivityTest;
import org.mercycorps.translationcards.activity.addTranslation.SummaryActivityTest;
import org.mercycorps.translationcards.view.TranslationCardItemTest;

import dagger.Component;

@PerActivity
@Component(modules = {MockMediaModule.class, MockServiceModule.class, MockRepositoryModule.class}, dependencies = ApplicationComponent.class)
public interface TestBaseComponent extends BaseComponent {
    void inject(SummaryActivityTest activity);
    void inject(RecordAudioActivityTest activity);
    void inject(TranslationCardItemTest activity);
    void inject(ImportActivity importActivity);
    void inject(ReviewAndSaveActivityTest reviewAndSaveActivity);
}
