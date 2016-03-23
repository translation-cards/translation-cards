package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.refactor.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.refactor.activity.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.refactor.activity.EnterTranslatedPhraseActivity;
import org.robolectric.Robolectric;

import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;


public class TestActivityHelper {
    public static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_TRANSLATION_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";

    public static Activity createActivityToTest(Dictionary dict, Class<AbstractTranslationCardsActivity> instanceOfClass) {
        NewTranslationContext context = new NewTranslationContext(dict);
        return createActivityToTest(context, instanceOfClass);
    }

    public  static Activity createActivityToTest(NewTranslationContext context, Class<AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTest(Class<AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }
}
