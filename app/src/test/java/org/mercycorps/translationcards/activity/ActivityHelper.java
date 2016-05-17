package org.mercycorps.translationcards.activity;


import android.app.Activity;
import android.content.Intent;

import org.robolectric.Robolectric;
import org.robolectric.util.ActivityController;

public class ActivityHelper<T extends AbstractTranslationCardsActivity> {

    public static final String INTENT_KEY_DECK = "Deck";

    private ActivityController<T> controller;
    private Class<T> instanceOfClass;
    private Activity activity;

    public ActivityHelper(Class<T> instanceOfClass) {
        this.instanceOfClass = instanceOfClass;
    }

    protected Activity getActivityWithIntent(Intent intent) {
        controller = Robolectric.buildActivity(instanceOfClass);
        activity = controller.withIntent(intent).create().get();
        return activity;
    }

    public void teardown() {
        if(activity != null) {
            activity.finish();
            activity = null;
        }

        if(controller != null) {
            controller.pause().stop().destroy();
            controller = null;
        }
    }
}
