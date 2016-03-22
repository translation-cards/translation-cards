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
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.MediaRecorderManager;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
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
    public void whenThereIsNoAudioFilePlayButtonShouldBeDisabled() {
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
        assertTrue(newTranslationContext.getTranslation().getFilename().startsWith("TranslationCards"));
    }

    @Test
    public void shouldPlayAudioFileWhenPlayButtonIsClicked() throws IOException {
        FileDescriptor descriptor = new FileDescriptor();
        NewTranslationContext context = createContext(createTranslation());
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        AudioPlayerManager audioPlayerManager = application.getAudioPlayerManager();
        Activity activity = createActivityToTest(context);
        activity.findViewById(R.id.play_audio_button).performClick();
        verify(audioPlayerManager).play(DEFAULT_AUDIO_FILE);
    }

    private Translation createTranslation() throws IOException {
        Translation translation = spy(new Translation());
        translation.setAudioFileName(DEFAULT_AUDIO_FILE);
        return translation;
    }

    @Test
    public void shouldStartEnterSourcePhraseActivityWhenBackButtonIsClicked() {
        Activity activity = createActivityToTest(createContextWithSourceText());
        activity.findViewById(R.id.record_activity_back).performClick();
        assertEquals(EnterSourcePhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartEnterTranslatedPhraseActivityWhenNextButtonIsClicked() {
        Activity activity = createActivityToTest(createContextWithSourceText());
        activity.findViewById(R.id.record_activity_next).performClick();
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    private File createFile() {
        return new File(DEFAULT_AUDIO_FILE);
    }

    private FileDescriptor createFileDescriptor(NewTranslationContext context) throws IOException {
        return new FileInputStream(context.getTranslation().getFilename()).getFD();
    }

    private void setUpMediaRecorder() {
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        MediaRecorderManager mediaRecorderManager = application.getMediaRecorderManager();
    }

    private void setupFileHelper(NewTranslationContext context) throws IOException {
        TestMainApplication application = (TestMainApplication) RuntimeEnvironment.application;
        when(application.getFileHelper().createFileDescriptor(any(String.class))).thenReturn(createFileDescriptor(context));
    }


    private NewTranslationContext createContext(String audioFileName){
        NewTranslationContext context = new NewTranslationContext(null);
        context.setAudioFile(audioFileName);
        return context;
    }

    private NewTranslationContext createContext(Translation translation){
        return new NewTranslationContext(null, translation);
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
}