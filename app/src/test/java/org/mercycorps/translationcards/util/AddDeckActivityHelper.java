package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.ActivityHelper;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

public class AddDeckActivityHelper<T extends AbstractTranslationCardsActivity> extends ActivityHelper<T> {

    public static final String DEFAULT_DECK_NAME = "MercyCorps Deck";
    public static final String DEFAULT_DECK_ISO_CODE = "en";

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
        NewDeckContext defaultDeckContext = new NewDeckContext(new Deck(DEFAULT_DECK_NAME, "", "", -1, true, DEFAULT_DECK_ISO_CODE), "", false);
        intent.putExtra(INTENT_KEY_DECK, defaultDeckContext);
        return getActivityWithIntent(intent);
    }

}
