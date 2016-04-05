package org.mercycorps.translationcards.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslationContext;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;
import org.robolectric.Robolectric;

import org.robolectric.RuntimeEnvironment;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestAddTranslationCardActivityHelper {
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


    public static Activity createActivityToTest(Class<? extends AbstractTranslationCardsActivity> instanceOfClass) {
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

    public static Activity createActivityToTestWithSourceAndTranslatedText(Class<? extends AddTranslationActivity> instanceOfClass) {
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

    public static AudioPlayerManager getAudioPlayerManager() {
        return getApplication().getAudioPlayerManager();
    }

    public static AudioRecorderManager getAudioRecorderManager() {
        return getApplication().getAudioRecorderManager();
    }

    public static DecoratedMediaManager getDecoratedMediaManager() {
        return getApplication().getDecoratedMediaManager();
    }

    public static ScheduledExecutorService getScheduledExecutorService() {
        return getApplication().getScheduledExecutorService();
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

    private static TestMainApplication getApplication() {
        return (TestMainApplication) RuntimeEnvironment.application;
    }

    public static void setText(Activity activity, int resId, String textToSet) {
        TextView textView = (TextView) activity.findViewById(resId);
        textView.setText(textToSet);
    }

    public static void click(Activity activity, int resId) {
        activity.findViewById(resId).performClick();
    }

    public static void click(View view, int resId){
        view.findViewById(resId).performClick();
    }

    public static ImageView findImageView(Activity activity, int resId) {
        return (ImageView) activity.findViewById(resId);
    }

    public static View findView(Activity activity, int resId) {
        return activity.findViewById(resId);
    }

    public static TextView findTextView(Activity activity, int resId) {
        return (TextView) activity.findViewById(resId);
    }

    public static LinearLayout findLinearLayout(Activity activity, int resId) {
        return (LinearLayout) activity.findViewById(resId);
    }

    public static <T> T findAnyView(Activity activity, int resId) {
        return (T) activity.findViewById(resId);
    }

    public static <T> T findAnyView(View view, int resId) {
        return (T) view.findViewById(resId);
    }


    public static NewTranslationContext getContextFromIntent(Activity activity) {
        return (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
    }

    public static DbManager getDbManager() {

        return ((TestMainApplication) TestMainApplication.getContextFromMainApp()).getDbManager();
    }

    public static void setupAudioPlayerManager() throws AudioFileNotSetException {
        when(getAudioPlayerManager().getCurrentPosition()).thenReturn(DEFAULT_POSITION);
        when(getAudioPlayerManager().getMaxDuration()).thenReturn(DEFAULT_MAX);
        when(getScheduledExecutorService().scheduleAtFixedRate(any(Runnable.class), eq(INITIAL_DELAY), eq(PERIOD), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(mock(ScheduledFuture.class));
    }

    public static int getAlertDialogTitleId(){
        return 2131558473; //// TODO: 4/1/16 How did I get this?
    }
}
