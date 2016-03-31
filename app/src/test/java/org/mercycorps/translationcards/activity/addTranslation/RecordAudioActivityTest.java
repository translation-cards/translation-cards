package org.mercycorps.translationcards.activity.addTranslation;

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
import static org.mockito.Mockito.when;
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
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_back);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisableNextButtonWhenRecordingIsHappening() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.GONE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisableBackButtonWhenRecordingIsHappening() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_back);
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
    public void shouldStartEnterTranslatedPhraseActivityWhenBackButtonIsClicked() {
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        click(activity, R.id.record_activity_back);
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartSummaryActivityWhenNextButtonIsClicked() {
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        click(activity, R.id.record_activity_next);
        assertEquals(SummaryActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldRecordAudioActivityTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class, createDefaultDictionary());
        TextView recordAudioTitle = findTextView(activity, R.id.record_audio_title);
        assertEquals(("Record your phrase"), recordAudioTitle.getText().toString());
    }

    @Test
    public void shouldShowTranslationSourceTextIfSourceTextExistsInNewTranslationContext() {
        Activity activity = createActivityToTestWithTContextAndSourceText(RecordAudioActivity.class);
        TextView translationCardOriginText = findTextView(activity, R.id.origin_translation_text);
        assertEquals(DEFAULT_SOURCE_PHRASE, translationCardOriginText.getText().toString());
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

    @Test
    public void shouldChangeRecordButtonToActiveImageWhenItIsRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        ImageButton recordButton = (ImageButton) activity.findViewById(R.id.record_audio_button);
        assertEquals(R.drawable.button_record_active, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordButtonToEnabledImageWhenItIsFinishedRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        ImageButton recordButton = (ImageButton) activity.findViewById(R.id.record_audio_button);
        assertEquals(R.drawable.button_record_enabled, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordButtonToEnabledImageWhenItIsFinishedRecordingByPressingPlay() {
        when(getAudioRecorderManager().isRecording()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.play_audio_button);
        ImageButton recordButton = (ImageButton) activity.findViewById(R.id.record_audio_button);
        assertEquals(R.drawable.button_record_enabled, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldStopRecordingWhenPlayButtonIsClicked() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.play_audio_button);
        verify(getAudioRecorderManager()).stop();
    }

    @Test
    public void shouldStopPlayingWhenRecordButtonIsClicked() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.play_audio_button);
        click(activity, R.id.record_audio_button);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopPlayingWhenNextButtonIsClicked(){
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.record_activity_next);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopPlayingWhenBackButtonIsClicked(){
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.record_activity_back);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopRecordingWhenNextButtonIsClicked(){
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_activity_next);
        verify(getAudioRecorderManager()).stop();
    }

    @Test
    public void shouldStopRecordingWhenBackButtonIsClicked() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_activity_back);
        verify(getAudioRecorderManager()).stop();
    }

    @Test
    public void shouldHaveVisibleBackButtonWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        assertEquals(View.VISIBLE, activity.findViewById(R.id.record_activity_back).getVisibility());
    }

    @Test
    public void shouldEnableBackButtonsWhenRecordingIsStoppedByPlayClick(){
        when(getAudioRecorderManager().isRecording()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.play_audio_button);
        assertEquals(View.VISIBLE, activity.findViewById(R.id.record_activity_back).getVisibility());
    }

    @Test
    public void shouldEnableNextButtonWhenRecordingIsStoppedByPlayClick() {
        when(getAudioRecorderManager().isRecording()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.play_audio_button);
        assertEquals(View.VISIBLE, activity.findViewById(R.id.record_activity_next).getVisibility());
    }

    @Test
    public void shouldNotShowTranslationTextIndicatorDividerOnStart() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        View indicatorDivider = findView(activity, R.id.text_indicator_divider);
        assertEquals(View.GONE, indicatorDivider.getVisibility());
    }
}