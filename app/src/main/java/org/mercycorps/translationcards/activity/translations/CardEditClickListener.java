package org.mercycorps.translationcards.activity.translations;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;

import java.util.ArrayList;
import java.util.List;


public class CardEditClickListener implements View.OnClickListener {
    private Activity translationsActivity;
    private Translation translation;
    private Intent intent;
    private DictionaryService dictionaryService;
    private DeckService deckService;

    public CardEditClickListener(Activity translationsActivity, Translation translation, Intent intent, DictionaryService dictionaryService, DeckService deckService) {
        this.translationsActivity = translationsActivity;
        this.translation = translation;
        this.intent = intent;
        this.dictionaryService = dictionaryService;
        this.deckService = deckService;
    }

    @Override
    public void onClick(View view) {
        intent.putExtra(AddTranslationActivity.CONTEXT_INTENT_KEY, createAddNewTranslationContext());
        intent.putExtra(deckService.INTENT_KEY_DECK, deckService.currentDeck());

        translationsActivity.startActivity(intent);
    }

    private AddNewTranslationContext createAddNewTranslationContext() {
        List<NewTranslation> newTranslations = new ArrayList<>();

        for (Dictionary dictionary : dictionaryService.getDictionariesForCurrentDeck()) {
            Translation translation = dictionary.getTranslationBySourcePhrase(this.translation.getLabel());
            newTranslations.add(new NewTranslation(dictionary, translation, TranslationsActivity.IS_EDIT));
        }

        return new AddNewTranslationContext(newTranslations, TranslationsActivity.IS_EDIT);
    }
}
