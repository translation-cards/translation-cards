package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslationContext;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;
import org.robolectric.Robolectric;

/**
 * Created by karthikbalasubramanian on 4/1/16.
 */
public class TCardsFactory {
    public static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_TRANSLATED_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    public static final String DEFAULT_AUDIO_FILE = "DefaultAudioFile";
    public static final String DEFAULT_SOURCE_PHRASE = "SourcePhrase";
    public static final String DEFAULT_TRANSLATION_LABEL = "TranslationLabel";
    public static final Long INITIAL_DELAY = (long) 0;
    public static final Long PERIOD = (long) 100;
    public static final int DEFAULT_POSITION = 5;
    public static final int DEFAULT_MAX = 10;

    public static Activity createActivityToTest(Class<? extends AddTranslationActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static AppCompatActivity createCompatActivityToTest(Class<? extends AddTranslationActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithTranslationContext(Class<? extends AddTranslationActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslationContext context = createTranslationContextWithSourcePhraseAndTranslatedText();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithTContextAndSourceText(Class<? extends AddTranslationActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslationContext context = createTranslationContextWithSourcePhraseAndTranslatedText();
        context.setSourceText(DEFAULT_SOURCE_PHRASE);
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Dictionary createDefaultDictionary() {
        return new Dictionary(DEFAULT_DICTIONARY_LABEL);
    }

    public static Activity createActivityToTest(Class<? extends AddTranslationActivity> instanceOfClass, Dictionary dict) {
        NewTranslationContext context = new NewTranslationContext(dict);
        return createActivityToTest(instanceOfClass, context);
    }

    public static Activity createActivityToTest(Class<? extends AddTranslationActivity> instanceOfClass, NewTranslationContext context) {
        Intent intent = new Intent();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static NewTranslationContext fetchTranslationContext(Activity activity) {
        return (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
    }

    public static NewTranslationContext createTranslationContextWithSourcePhraseAndTranslatedText() {
        return new NewTranslationContext(createDefaultDictionary(), createTranslation());
    }

    public static Translation createTranslation() {
        Translation translation = new Translation();
        translation.setLabel(DEFAULT_TRANSLATION_LABEL);
        translation.setTranslatedText(DEFAULT_TRANSLATED_TEXT);
        translation.setAudioFileName(DEFAULT_AUDIO_FILE);
        return translation;
    }

}
