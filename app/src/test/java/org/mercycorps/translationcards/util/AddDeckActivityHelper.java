package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.data.Deck;

public class AddDeckActivityHelper<T extends AbstractTranslationCardsActivity> extends ActivityHelper<T> {

    public final String DEFAULT_DECK_NAME = "MercyCorps Deck";
    public final String DEFAULT_DECK_ISO_CODE = "en";
    public final String DEFAULT_SOURCE_LANGUAGE ="English" ;
    public final String DEFAULT_SOURCE_LANGUAGE_ISO = "en" ;
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
        NewDeckContext defaultDeckContext = new NewDeckContext(new Deck(DEFAULT_DECK_NAME, DEFAULT_DECK_AUTHOR, "", -1, true, DEFAULT_DECK_ISO_CODE), "", false);
        intent.putExtra(INTENT_KEY_DECK, defaultDeckContext);
        return getActivityWithIntent(intent);
    }

    public NewDeckContext getContextFromIntent(Activity activity) {
        return (NewDeckContext) activity.getIntent().getSerializableExtra(INTENT_KEY_DECK);
    }

}
