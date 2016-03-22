package org.mercycorps.translationcards.refactor.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.media.MediaPlayerManager;
import org.mercycorps.translationcards.media.MediaRecorderManager;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RecordAudioActivityTest {

    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_SOURCE_TEXT = "DefaultSourceText";
    public static final String DEFAULT_AUDIO_FILE = DEFAULT_SOURCE_TEXT + ".mp4";

    @Test
    public void shouldNotBeAbleToPlayAudioWhenActivityStarts() {
        Activity activity = createActivityToTest();
        ImageView playButton = (ImageView) activity.findViewById(R.id.play_audio_button);
        assertEquals(R.drawable.play_button_disabled, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void whenThereIsNoAudioFileIsPlayButtonShouldBeDisabled() {
        Activity activity = createActivityToTest();
        ImageView playButton = (ImageView) activity.findViewById(R.id.play_audio_button);
        assertFalse(playButton.isClickable());
    }

    @Test
    public void shouldBeAbleToClickPlayButtonWhenAudioFileIsPresent() {
        Activity activity = createActivityToTest(createContext(DEFAULT_AUDIO_FILE));
        ImageView playButton = (ImageView) activity.findViewById(R.id.play_audio_button);
        assertTrue(playButton.isClickable());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsNotRecording() {
        Activity activity = createActivityToTest(createContext(DEFAULT_AUDIO_FILE));
        LinearLayout nextButton = (LinearLayout) activity.findViewById(R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsRecordingIsFinished() {
        setUpMediaRecorder();
        Activity activity = createActivityToTest(createContextWithSourceText());
        activity.findViewById(R.id.record_audio_button).performClick();
        activity.findViewById(R.id.record_audio_button).performClick();
        LinearLayout nextButton = (LinearLayout) activity.findViewById(R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableBackButtonWhenAudioFileIsPresentAndIsRecordingIsFinished(){
        setUpMediaRecorder();
        Activity activity = createActivityToTest(createContextWithSourceText());
        activity.findViewById(R.id.record_audio_button).performClick();
        activity.findViewById(R.id.record_audio_button).performClick();
        LinearLayout nextButton = (LinearLayout) activity.findViewById(R.id.record_activity_back);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisableNextButtonWhenRecordingIsHappening() {
        Activity activity = createActivityToTest();
        activity.findViewById(R.id.record_audio_button).performClick();
        LinearLayout nextButton = (LinearLayout) activity.findViewById(R.id.record_activity_next);
        assertEquals(View.GONE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisableBackButtonWhenRecordingIsHappening() {
        Activity activity = createActivityToTest();
        activity.findViewById(R.id.record_audio_button).performClick();
        LinearLayout nextButton = (LinearLayout) activity.findViewById(R.id.record_activity_back);
        assertEquals(View.GONE, nextButton.getVisibility());
    }

    @Test
    public void shouldHaveAValidFileNameAfterRecordingIsComplete(){
        setUpMediaRecorder();
        Activity activity = createActivityToTest(createContextWithSourceText());
        activity.findViewById(R.id.record_audio_button).performClick();
        activity.findViewById(R.id.record_audio_button).performClick();
        NewTranslationContext newTranslationContext = (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(newTranslationContext.getTranslation().getFilename(), DEFAULT_AUDIO_FILE);
    }

    private void setUpMediaRecorder() {
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        MediaRecorderManager mediaRecorderManager = application.getMediaRecorderManager();
        when(mediaRecorderManager.stop()).thenReturn(DEFAULT_AUDIO_FILE);
    }


    private NewTranslationContext createContext(String audioFileName){
        NewTranslationContext context = new NewTranslationContext(null);
        context.setAudioFile(audioFileName);
        return context;
    }

    private NewTranslationContext createContextWithSourceText(){
        NewTranslationContext context = new NewTranslationContext(null);
        context.setSourceText(DEFAULT_SOURCE_TEXT);
        return context;
    }


    private Activity createActivityToTest() {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(null);
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(RecordAudioActivity.class).withIntent(intent).create().get();
    }


    private Activity createActivityToTest(NewTranslationContext context) {
        Intent intent = new Intent();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(RecordAudioActivity.class).withIntent(intent).create().get();
    }

    // should play file when play is clicked



    // should be able to go back to source phrase activity

    //


}