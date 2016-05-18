package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.Collections;

public class MyDecksActivityHelper<T extends AbstractTranslationCardsActivity> extends ActivityHelper<T> {

    public static final String CONTEXT_INTENT_KEY = "AddNewTranslationContext";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";


    private Dictionary dictionary;

    public MyDecksActivityHelper(Class<T> instanceOfClass) {
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

    public Dictionary createDefaultDictionary() {
        return new Dictionary(DEFAULT_DICTIONARY_LABEL);
    }

}
