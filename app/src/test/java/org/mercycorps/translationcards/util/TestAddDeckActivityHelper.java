package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addDeck.AddDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.Robolectric;

public class TestAddDeckActivityHelper {
    public static final String INTENT_KEY_DECK = "Deck";

    public static Activity createActivityToTest(Class<? extends AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewDeckContext deckContext = new NewDeckContext();
        intent.putExtra(INTENT_KEY_DECK, deckContext);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithDeckContext(Class<? extends AbstractTranslationCardsActivity> instanceOfClass, NewDeckContext deckContext) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_DECK, deckContext);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }
}
