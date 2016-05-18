package org.mercycorps.translationcards.activity.translations;

import android.content.Intent;
import android.view.View;

import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agarrard on 5/18/16.
 */
class CardEditClickListener implements View.OnClickListener {
    private TranslationsActivity translationsActivity;
    private Translation translationCard;

    public CardEditClickListener(TranslationsActivity translationsActivity, Translation translationCard) {
        this.translationsActivity = translationsActivity;
        this.translationCard = translationCard;
    }

    @Override
    public void onClick(View view) {
        Intent nextIntent = new Intent(translationsActivity, EnterSourcePhraseActivity.class);
        List<NewTranslation> newTranslations = new ArrayList<>();
        for (Dictionary dictionary : translationsActivity.dictionaries) {
            Translation translation = dictionary.getTranslationBySourcePhrase(translationCard.getLabel());
            newTranslations.add(new NewTranslation(dictionary, translation, TranslationsActivity.IS_EDIT));
        }
        nextIntent.putExtra(AddTranslationActivity.CONTEXT_INTENT_KEY, new AddNewTranslationContext(newTranslations, TranslationsActivity.IS_EDIT));
        nextIntent.putExtra(TranslationsActivity.INTENT_KEY_DECK, translationsActivity.deck);
        translationsActivity.startActivity(nextIntent);
    }
}
