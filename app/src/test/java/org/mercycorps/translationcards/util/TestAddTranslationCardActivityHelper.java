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
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;
import org.robolectric.Robolectric;

import org.robolectric.RuntimeEnvironment;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestAddTranslationCardActivityHelper {
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


    public static Activity createActivityToTest(Class<? extends AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslation newTranslation = new NewTranslation(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithMultipleNewTranslationContexts(Class<? extends AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslation newTranslation = new NewTranslation(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        NewTranslation newArabicTranslation = new NewTranslation(new Dictionary(ARABIC_DICTIONARY_LABEL));
        newArabicTranslation.setTranslatedText(ARABIC_TRANSLATION);
        AddNewTranslationContext context = new AddNewTranslationContext(Arrays.asList(newTranslation, newArabicTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestInEditMode(Class<? extends AbstractTranslationCardsActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslation newTranslation = new NewTranslation(new Dictionary(DEFAULT_DICTIONARY_LABEL), new Translation(), IS_EDIT);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation), IS_EDIT);
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static AppCompatActivity createCompatActivityToTest(Class<? extends AddTranslationActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslation newTranslation = new NewTranslation(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithTranslationContext(Class<? extends AddTranslationActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslation newTranslation = createTranslationContextWithSourcePhraseAndTranslatedText();
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Activity createActivityToTestWithSourceAndTranslatedText(Class<? extends AddTranslationActivity> instanceOfClass) {
        Intent intent = new Intent();
        NewTranslation newTranslation = createTranslationContextWithSourcePhraseAndTranslatedText();
        newTranslation.setSourceText(DEFAULT_SOURCE_PHRASE);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(instanceOfClass).withIntent(intent).create().get();
    }

    public static Dictionary createDefaultDictionary() {
        return new Dictionary(DEFAULT_DICTIONARY_LABEL);
    }

    public static Activity createActivityToTest(Class<? extends AddTranslationActivity> instanceOfClass, Dictionary dict) {
        NewTranslation newTranslation = new NewTranslation(dict);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(newTranslation));
        return createActivityToTest(instanceOfClass, context);
    }

    public static Activity createActivityToTest(Class<? extends AddTranslationActivity> instanceOfClass, AddNewTranslationContext context) {
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

    public static AddNewTranslationContext fetchTranslationContext(Activity activity) {
        return (AddNewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
    }

    public static NewTranslation createTranslationContextWithSourcePhraseAndTranslatedText() {
        return new NewTranslation(createDefaultDictionary(), createTranslation(), false);
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


    public static AddNewTranslationContext getContextFromIntent(Activity activity) {
        return (AddNewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
    }

    public static NewTranslation getFirstNewTranslationFromContext(Activity activity) {
        return getContextFromIntent(activity).getNewTranslations().get(0);
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
        return 2131558473;
    }

    public static void clickLanguageTabAtPosition(Activity activity, Integer position) {
        ((LinearLayout) findView(activity, R.id.language_tabs_fragment).findViewById(R.id.languages_scroll_list)).getChildAt(position).performClick();
    }
}
