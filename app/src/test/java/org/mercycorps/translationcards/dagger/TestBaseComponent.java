package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.activity.ImportActivity;
import org.mercycorps.translationcards.myDecks.MyDeckAdapterTest;
import org.mercycorps.translationcards.myDecks.MyDecksActivityTest;
import org.mercycorps.translationcards.activity.TranslationsActivityTest;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckSourceLanguageActivityTest;
import org.mercycorps.translationcards.addDeck.activity.LanguageSelectorActivityTest;
import org.mercycorps.translationcards.addDeck.activity.ReviewAndSaveDeckActivityTest;
import org.mercycorps.translationcards.activity.addTranslation.RecordAudioActivityTest;
import org.mercycorps.translationcards.activity.addTranslation.SummaryActivityTest;
import org.mercycorps.translationcards.activity.translations.CardListAdapterTest;
import org.mercycorps.translationcards.view.DeckItemTest;
import org.mercycorps.translationcards.view.TranslationCardItemTest;

import dagger.Component;

@PerApplication
@Component(modules = {MockMediaModule.class, MockServiceModule.class, MockRepositoryModule.class})
public interface TestBaseComponent extends BaseComponent {
    void inject(SummaryActivityTest activity);
    void inject(RecordAudioActivityTest activity);
    void inject(TranslationCardItemTest activity);
    void inject(ImportActivity importActivity);
    void inject(ReviewAndSaveDeckActivityTest reviewAndSaveActivity);
    void inject(EnterDeckSourceLanguageActivityTest enterDeckSourceLanguageActivity);
    void inject(LanguageSelectorActivityTest languageSelectorActivity);
    void inject(MyDecksActivityTest myDecksActivityTest);
    void inject(TranslationsActivityTest translationsActivityTest);
    void inject(MyDeckAdapterTest myDeckAdapterTest);
    void inject(CardListAdapterTest cardListAdapterTest);
    void inject(DeckItemTest deckItemTest);
}
