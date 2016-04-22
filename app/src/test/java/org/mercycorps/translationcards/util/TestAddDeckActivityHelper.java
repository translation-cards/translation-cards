package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addDeck.AddDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslationContext;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.Robolectric;

import java.util.ArrayList;

public class TestAddDeckActivityHelper {
    public static final String INTENT_KEY_DECK = "Deck";
    public static final String DEFAULT_DECK_NAME = "MercyCorps Deck";
    public static final String DEFAULT_DECK_ISO_CODE = "en";

    public static Activity createActivityToTest(Class<? extends AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewDeckContext deckContext = new NewDeckContext();
        intent.putExtra(INTENT_KEY_DECK, deckContext);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithDefaultDeck(Class<? extends AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewDeckContext defaultDeckContext = new NewDeckContext(new Deck(DEFAULT_DECK_NAME, "", "", -1, true, DEFAULT_DECK_ISO_CODE), new ArrayList<Dictionary>());
        intent.putExtra(INTENT_KEY_DECK, defaultDeckContext);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithContext(Class<? extends AbstractTranslationCardsActivity> instanceOfClass, NewDeckContext newDeckContext) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_DECK, newDeckContext);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static NewDeckContext getContextFromIntent(Activity activity) {
        return (NewDeckContext) activity.getIntent().getSerializableExtra(AddDeckActivity.INTENT_KEY_DECK);
    }
}
