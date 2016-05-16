package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

public class AddDeckActivityHelper<T extends AbstractTranslationCardsActivity> {

    public static final String INTENT_KEY_DECK = "Deck";
    public static final String DEFAULT_DECK_NAME = "MercyCorps Deck";
    public static final String DEFAULT_DECK_ISO_CODE = "en";

    private ActivityController<T> controller;
    private Class<T> instanceOfClass;
    private Activity activity;

    public AddDeckActivityHelper(Class<T> instanceOfClass) {
        this.instanceOfClass = instanceOfClass;
    }

    public Activity createActivityToTest() {
        Intent intent = new Intent();
        NewDeckContext deckContext = new NewDeckContext();
        intent.putExtra(INTENT_KEY_DECK, deckContext);
        controller = Robolectric.buildActivity(instanceOfClass);
        activity = controller.withIntent(intent).create().get();
        return activity;
    }

    public Activity createActivityToTestWithContext(NewDeckContext newDeckContext) {
        Intent intent = new Intent();
        intent.putExtra(INTENT_KEY_DECK, newDeckContext);
        controller = Robolectric.buildActivity(instanceOfClass);
        activity = controller.withIntent(intent).create().get();
        return activity;
    }

    public Activity createActivityToTestWithDefaultDeck() {
        Intent intent = new Intent();
        NewDeckContext defaultDeckContext = new NewDeckContext(new Deck(DEFAULT_DECK_NAME, "", "", -1, true, DEFAULT_DECK_ISO_CODE), "", false);
        intent.putExtra(INTENT_KEY_DECK, defaultDeckContext);
        controller = Robolectric.buildActivity(instanceOfClass);
        activity = controller.withIntent(intent).create().get();
        return activity;
    }

    public void teardown() {
        if(activity != null) {
            activity.finish();
        }
        if(controller != null) {
            controller.pause().stop().destroy();
            controller = null;
        }
    }
}
