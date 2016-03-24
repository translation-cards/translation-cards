package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.refactor.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.refactor.activity.AddTranslationActivity;
import org.mercycorps.translationcards.refactor.activity.SummaryActivity;
import org.robolectric.Robolectric;

import org.robolectric.RuntimeEnvironment;


public class TestAddTranslationCardActivityHelper {
    public static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_TRANSLATED_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    public static final String DEFAULT_AUDIO_FILE = "DefaultAudioFile";
    public static final String DEFAULT_SOURCE_PHRASE = "SourcePhrase";
    public static final String DEFAULT_TRANSLATION_LABEL = "TranslationLabel";


    public static Activity createActivityToTest(Class<? extends AddTranslationActivity>instanceOfClass) {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static AppCompatActivity createCompatActivityToTest(Class<? extends AddTranslationActivity>instanceOfClass) {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithTranslationContext(Class<? extends AddTranslationActivity>instanceOfClass){
        Intent intent = new Intent();
        NewTranslationContext context = createTranslationContextWithSourcePhraseAndTranslatedText();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithTContextAndSourceText(Class<? extends AddTranslationActivity>instanceOfClass){
        Intent intent = new Intent();
        NewTranslationContext context = createTranslationContextWithSourcePhraseAndTranslatedText();
        context.setSourceText(DEFAULT_SOURCE_PHRASE);
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Dictionary createDefaultDictionary(){
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

    public static AudioPlayerManager getAudioPlayerManager() {
        return getApplication().getAudioPlayerManager();
    }

    public static NewTranslationContext fetchTranslationContext(Activity activity) {
        return (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
    }

    public static NewTranslationContext createTranslationContextWithSourcePhraseAndTranslatedText(){
        return new NewTranslationContext(createDefaultDictionary(), createTranslation());
    }

    public static Translation createTranslation() {
        Translation translation = new Translation();
        translation.setLabel(DEFAULT_TRANSLATION_LABEL);
        translation.setTranslatedText(DEFAULT_TRANSLATED_TEXT);
        translation.setAudioFileName(DEFAULT_AUDIO_FILE);
        return translation;
    }

    private static TestMainApplication getApplication() {
        return (TestMainApplication) RuntimeEnvironment.application;
    }

    public static void setText(Activity activity, int resId, String textToSet){
        TextView textView = (TextView) activity.findViewById(resId);
        textView.setText(textToSet);
    }

    public static void click(Activity activity, int resId){
        activity.findViewById(resId).performClick();
    }

    public static ImageView findImageView(Activity activity, int resId){
        return (ImageView) activity.findViewById(resId);
    }

    public static View findView(Activity activity, int resId){
        return activity.findViewById(resId);
    }

    public static TextView findTextView(Activity activity, int resId){
        return (TextView) activity.findViewById(resId);
    }

    public static LinearLayout findLinearLayout(Activity activity, int resId){
        return (LinearLayout) activity.findViewById(resId);
    }


    public static NewTranslationContext getContextFromIntent(Activity activity){
        return (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
    }

    public static DbManager getDbManager(){
       return ((TestMainApplication) TestMainApplication.getContextFromMainApp()).getDbManager();
    }
}
