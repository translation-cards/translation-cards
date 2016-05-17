package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;
import org.robolectric.Robolectric;

import java.util.Arrays;
import java.util.Collections;

public class AddTranslationActivityHelper<T extends AbstractTranslationCardsActivity> extends ActivityHelper<T> {

    public static final String CONTEXT_INTENT_KEY = "AddNewTranslationContext";
    public static final String DEFAULT_TRANSLATED_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    public static final String DEFAULT_AUDIO_FILE = "DefaultAudioFile";
    public static final String DEFAULT_SOURCE_PHRASE = "SourcePhrase";
    public static final String DEFAULT_TRANSLATION_LABEL = "TranslationLabel";
    public static final Long INITIAL_DELAY = (long) 0;
    public static final Long PERIOD = (long) 100;
    public static final int DEFAULT_POSITION = 5;
    public static final int DEFAULT_MAX = 10;
    private static final boolean IS_EDIT = true;
    private static final String ARABIC_DICTIONARY_LABEL = "Arabic";
    private static final String ARABIC_TRANSLATION = "Arabic Translation";

    public Dictionary dictionary;

    public AddTranslationActivityHelper(Class<T> instanceOfClass) {
        super(instanceOfClass);
        dictionary = createDefaultDictionary();
    }

    public Activity createActivityToTest() {
        Intent intent = new Intent();
        NewTranslation newTranslation = new NewTranslation(dictionary);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return getActivityWithIntent(intent);
    }

    public Activity createActivityToTestWithNewTranslationContext() {
        Intent intent = new Intent();
        NewTranslation newTranslation = createTranslationContextWithSourcePhraseAndTranslatedText();
        newTranslation.setSourceText(DEFAULT_SOURCE_PHRASE);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return getActivityWithIntent(intent);
    }

    public Activity createActivityToTestInEditMode() {
        Intent intent = new Intent();
        NewTranslation newTranslation = new NewTranslation(dictionary, new Translation(), IS_EDIT);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation), IS_EDIT);
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return getActivityWithIntent(intent);
    }

    public Activity createActivityToTestWithContext(AddNewTranslationContext context) {
        Intent intent = new Intent();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return getActivityWithIntent(intent);
    }

    public  Activity createActivityToTestWithMultipleNewTranslationContexts() {
        Intent intent = new Intent();

        NewTranslation newTranslation = new NewTranslation(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        newTranslation.setAudioFile(DEFAULT_AUDIO_FILE);

        NewTranslation newArabicTranslation = new NewTranslation(new Dictionary(ARABIC_DICTIONARY_LABEL));
        newArabicTranslation.setTranslatedText(ARABIC_TRANSLATION);

        AddNewTranslationContext context = new AddNewTranslationContext(Arrays.asList(newTranslation, newArabicTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return getActivityWithIntent(intent);
    }

    public Activity createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab() {
        Intent intent = new Intent();
        NewTranslation newTranslation = new NewTranslation(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        NewTranslation newArabicTranslation = new NewTranslation(new Dictionary(ARABIC_DICTIONARY_LABEL));
        newArabicTranslation.setAudioFile(DEFAULT_AUDIO_FILE);
        newArabicTranslation.setTranslatedText(ARABIC_TRANSLATION);
        AddNewTranslationContext context = new AddNewTranslationContext(Arrays.asList(newTranslation, newArabicTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return getActivityWithIntent(intent);
    }

    public NewTranslation createTranslationContextWithSourcePhraseAndTranslatedText() {
        return new NewTranslation(createDefaultDictionary(), createDefaultTranslation(), false);
    }

    public Translation createDefaultTranslation() {
        Translation translation = new Translation();
        translation.setLabel(DEFAULT_TRANSLATION_LABEL);
        translation.setTranslatedText(DEFAULT_TRANSLATED_TEXT);
        translation.setAudioFileName(DEFAULT_AUDIO_FILE);
        return translation;
    }

    public Dictionary createDefaultDictionary() {
        return new Dictionary(DEFAULT_DICTIONARY_LABEL);
    }



}
