package org.mercycorps.translationcards.refactor.activity;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RecordAudioActivityTest {

    @Test
    public void shouldNotBeAbleToPlayAudioWhenActivityStarts() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        ImageView playButton = findImageView(activity, R.id.play_audio_button);
        assertEquals(R.drawable.play_button_disabled, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void whenThereIsNoAudioFilePlayButtonShouldBeDisabled() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        ImageView playButton = findImageView(activity, R.id.play_audio_button);
        assertFalse(playButton.isClickable());
    }

    @Test
    public void shouldBeAbleToClickPlayButtonWhenAudioFileIsPresent() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        ImageView playButton = findImageView(activity, R.id.play_audio_button);
        assertTrue(playButton.isClickable());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsNotRecording() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsRecordingIsFinished() {
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableBackButtonWhenAudioFileIsPresentAndIsRecordingIsFinished(){
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.go_to_enter_source_phrase_activity);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisableNextButtonWhenRecordingIsHappening() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.GONE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisableBackButtonWhenRecordingIsHappening() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.go_to_enter_source_phrase_activity);
        assertEquals(View.GONE, nextButton.getVisibility());
    }

    @Test
    public void shouldHaveAValidFileNameAfterRecordingIsComplete(){
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        assertTrue(getContextFromIntent(activity).getTranslation().getFilename().contains("TranslationCards"));
    }

    @Test
    public void shouldPlayAudioFileWhenPlayButtonIsClicked() throws Exception {
        setupAudioPlayerManager();
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        click(activity, R.id.play_audio_button);
        verify(getAudioPlayerManager()).play(DEFAULT_AUDIO_FILE);
    }

    @Test
    public void shouldStartEnterSourcePhraseActivityWhenBackButtonIsClicked() {
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        click(activity, R.id.go_to_enter_source_phrase_activity);
        assertEquals(EnterSourcePhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartEnterTranslatedPhraseActivityWhenNextButtonIsClicked() {
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        click(activity, R.id.record_activity_next);
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldRecordAudioActivityTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class, createDefaultDictionary());
        TextView recordAudioTitle = findTextView(activity, R.id.record_audio_title);
        assertEquals(String.format("Record your %s phrase", DEFAULT_DICTIONARY_LABEL), recordAudioTitle.getText().toString());
    }

    @Test
    public void shouldShowTranslationSourceTextIfSourceTextExistsInNewTranslationContext() {
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        TextView recordAudioTitle = findTextView(activity, R.id.origin_translation_text);
        assertEquals(DEFAULT_SOURCE_PHRASE, recordAudioTitle.getText().toString());
    }

    @Test
    public void shouldNotHaveClickablePlayButtonWhenAFileIsNotRecorded() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        ImageButton playButton = (ImageButton) activity.findViewById(R.id.play_audio_button);
        assertFalse(playButton.isClickable());
    }

    @Test
    public void shouldHavePlayButtonBeClickableWhenAFileIsRecorded() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        ImageButton playButton = (ImageButton) activity.findViewById(R.id.play_audio_button);
        assertTrue(playButton.isClickable());
    }

    @Test
    public void shouldChangePlayButtonsImageWhenItBecomesClickable() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        ImageButton playButton = (ImageButton) activity.findViewById(R.id.play_audio_button);
        assertEquals(R.drawable.play_button_enabled, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }
}