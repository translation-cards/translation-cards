package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.model.Deck;

public class AddDeckActivityHelper<T extends AbstractTranslationCardsActivity> extends ActivityHelper<T> {

    public final String DEFAULT_DECK_NAME = "MercyCorps Deck";
    public final String DEFAULT_DECK_ISO_CODE = "en";
    public final String DEFAULT_SOURCE_LANGUAGE_NAME ="English" ;
    public final String DEFAULT_DECK_AUTHOR = "TW" ;
    public final String NEW_AUTHOR = "Some Author";

    public AddDeckActivityHelper(Class<T> instanceOfClass) {
        super(instanceOfClass);
    }

    public Activity createActivityToTest() {
        Intent intent = new Intent();
        NewDeckContext deckContext = new NewDeckContext();
        intent.putExtra(INTENT_KEY_DECK, deckContext);
        return getActivityWithIntent(intent);
    }

    public Activity createActivityToTestWithContext(NewDeckContext newDeckContext) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_DECK, newDeckContext);
        return getActivityWithIntent(intent);
    }

    public Activity createActivityToTestWithDefaultDeck() {
        Intent intent = new Intent();
        Deck deck = new Deck(DEFAULT_DECK_NAME, DEFAULT_DECK_AUTHOR, "", -1, true, DEFAULT_SOURCE_LANGUAGE_NAME);
        NewDeckContext defaultDeckContext = new NewDeckContext(deck);
        intent.putExtra(INTENT_KEY_DECK, defaultDeckContext);
        return getActivityWithIntent(intent);
    }

    public NewDeckContext getContextFromIntent(Activity activity) {
        return (NewDeckContext) activity.getIntent().getParcelableExtra(INTENT_KEY_DECK);
    }

}
