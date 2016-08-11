package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.activity.ImportActivity;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.activity.addDeck.LanguageSelectorActivity;
import org.mercycorps.translationcards.activity.addDeck.ReviewAndSaveActivity;
import org.mercycorps.translationcards.activity.addTranslation.RecordAudioActivity;
import org.mercycorps.translationcards.activity.addTranslation.SummaryActivity;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.view.TranslationCardItem;

import dagger.Component;

@PerApplication
@Component(modules = {MediaModule.class, ServiceModule.class, RepositoryModule.class})
public interface BaseComponent {
    void inject(MainApplication mainApplication);
    void inject(TranslationsActivity activity);
    void inject(SummaryActivity activity);
    void inject(RecordAudioActivity activity);
    void inject(ImportActivity importActivity);
    void inject(EnterDeckSourceLanguageActivity enterDeckSourceLanguageActivity);
    void inject(LanguageSelectorActivity languageSelectorActivity);
    void inject(MyDecksActivity myDecksActivity);
    void inject(ReviewAndSaveActivity reviewAndSaveActivity);
    void inject(TranslationCardItem cardItemView);
}
