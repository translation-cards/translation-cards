package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Translation;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Collections;

import static android.support.v4.content.ContextCompat.getColor;
import static java.lang.Thread.sleep;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
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

    private static final boolean IS_NOT_ASSET = false;

    @Test
    public void playButtonShouldBeGreyWhenActivityStarts() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        View playButton = findView(activity, R.id.play_audio_button);
        assertEquals(R.color.grey, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldNotBeAbleToClickPlayButtonWhenThereIsNoAudioFilePresent() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        View playButton = findView(activity, R.id.play_audio_button);
        assertFalse(playButton.isClickable());
    }

    @Test
    public void shouldBeAbleToClickPlayButtonWhenAudioFileIsPresent() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        View playButton = findView(activity, R.id.play_audio_button);
        assertTrue(playButton.isClickable());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsNotRecording() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertTrue(nextButton.isClickable());
    }

    @Test
    public void shouldDisableNextButtonWhenNoAudioFileIsPresent() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertFalse(nextButton.isClickable());
    }

    @Test
    public void shouldShowNextButtonWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioHasBeenRecorded() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertTrue(nextButton.isClickable());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsRecordingIsFinished() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableBackButtonWhenAudioFileIsPresentAndIsRecordingIsFinished() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_back);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisplayGreyNextButtonTextAndArrowWhenActivityIsCreatedWithoutRecording() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        TextView nextButtonText = findTextView(activity, R.id.recording_audio_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayGreyNextButtonArrowWhenActivityIsCreatedWithoutRecording() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        ImageView nextButtonArrow = findImageView(activity, R.id.recording_audio_save_image);
        assertEquals(R.drawable.forward_arrow_40p, shadowOf(nextButtonArrow.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisplayBlackNextButtonTextWhenActivityIsCreatedWithARecording() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        TextView nextButtonText = findTextView(activity, R.id.recording_audio_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayBlackNextButtonArrowWhenActivityIsCreatedWithARecording() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        ImageView nextButtonArrow = findImageView(activity, R.id.recording_audio_save_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonArrow.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisplayBlackNextButtonTextWhenActivityIsCreatedWithoutRecordingThenWeRecord() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        TextView nextButtonText = findTextView(activity, R.id.recording_audio_next_text);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayBlackNextButtonArrowWhenActivityIsCreatedWithoutRecordingThenWeRecord() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        ImageView nextButtonArrow = findImageView(activity, R.id.recording_audio_save_image);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonArrow.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisableNextButtonWhenRecordingIsHappening() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_audio_button);
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
    public void shouldHaveAValidFileNameAfterRecordingIsComplete() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        assertTrue(getFirstNewTranslationFromContext(activity).getTranslation().getFilename().contains("TranslationCards"));
    }

    @Test
    public void shouldPlayAudioFileWhenPlayButtonIsClicked() throws Exception {
        setupAudioPlayerManager();
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        click(activity, R.id.play_audio_button);
        verify(getAudioPlayerManager()).play(DEFAULT_AUDIO_FILE, IS_NOT_ASSET);
    }

    @Test
    public void shouldStartEnterTranslatedPhraseActivityWhenBackButtonIsClicked() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(RecordAudioActivity.class);
        click(activity, R.id.record_activity_back);
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartSummaryActivityWhenNextButtonIsClicked() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(RecordAudioActivity.class);
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
        Activity activity = createActivityToTestWithSourceAndTranslatedText(RecordAudioActivity.class);
        TextView translationCardOriginText = findTextView(activity, R.id.origin_translation_text);
        assertEquals(DEFAULT_SOURCE_PHRASE, translationCardOriginText.getText().toString());
    }

    @Test
    public void shouldHavePlayButtonBeClickableWhenAFileIsRecorded() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        View playButton = activity.findViewById(R.id.play_audio_button);
        assertTrue(playButton.isClickable());
    }

    @Test
    public void shouldChangePlayButtonsBackgroundToGreenWhenItBecomesClickable() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        View playButton = activity.findViewById(R.id.play_audio_button);
        assertEquals(R.color.green, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordBackgroundToDeepRedWhenItIsRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        View recordButton = activity.findViewById(R.id.record_audio_button);
        assertEquals(R.color.deep_red, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordButtonBackgroundToRedWhenItIsFinishedRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        View recordButton = activity.findViewById(R.id.record_audio_button);
        assertEquals(R.color.red, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordButtonBackgroundToRedWhenItIsFinishedRecordingByPressingPlay() {
        when(getAudioRecorderManager().isRecording()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.play_audio_button);
        View recordButton = activity.findViewById(R.id.record_audio_button);
        assertEquals(R.color.red, shadowOf(recordButton.getBackground()).getCreatedFromResId());
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
    public void shouldStopPlayingWhenNextButtonIsClicked() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.record_activity_next);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopPlayingWhenBackButtonIsClicked() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.record_activity_back);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopRecordingWhenNextButtonIsClicked() {
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
    public void shouldEnableBackButtonsWhenRecordingIsStoppedByPlayClick() {
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
    public void shouldShowCollapseArrowIconWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        ImageView expandArrowIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(expandArrowIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldShowTranslationChildWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        View translationChild = findView(activity, R.id.translation_child);
        assertEquals(View.VISIBLE, translationChild.getVisibility());
    }

    @Test
    public void shouldDisplayDescriptionWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        TextView activityDescription = findTextView(activity, R.id.recording_audio_instructions);
        assertEquals("Tap record then speak clearly at a normal speed. When you're done, tap record again. Play back and re-record until you're satisfied.", activityDescription.getText().toString());
    }

    @Test
    public void shouldBeAbleToClickIndicatorIcon() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        View indicatorLayout = findView(activity, R.id.translation_indicator_layout);
        assertTrue(indicatorLayout.isClickable());
    }

    @Test
    public void shouldCollapseCardWhenIndicatorIconIsClicked() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.translation_indicator_layout);
        View translationChild = findView(activity, R.id.translation_child);
        assertEquals(View.GONE, translationChild.getVisibility());
    }

    @Test
    public void shouldMakeTranslationGrandchildLinearLayoutGone() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        assertEquals(View.GONE, activity.findViewById(R.id.translation_grandchild).getVisibility());
    }

    @Test
    public void shouldShowExpandCardIndicatorWhenTranslationCardIsCollapsed() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.translation_indicator_layout);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.expand_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldExpandTranslationCardWhenCardIndicatorIsClickedTwice() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.translation_indicator_layout);
        click(activity, R.id.translation_indicator_layout);
        assertEquals(View.VISIBLE, findView(activity, R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowCollapseCardIndicatorWhenTranslationCardIsClickedTwice() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        click(activity, R.id.translation_indicator_layout);
        click(activity, R.id.translation_indicator_layout);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldShowTranslatedPhraseInCardView() {
        Activity activity = createActivityToTestWithTranslationContext(RecordAudioActivity.class);
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(DEFAULT_TRANSLATED_TEXT, translatedTextView.getText().toString());
    }

    @Test
    public void shouldShowHintTextWhenNoTranslatedTextPhraseIsProvided() {
        Activity activity = createActivityToTest(RecordAudioActivity.class, createDefaultDictionary());
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(String.format("Add %s translation", DEFAULT_DICTIONARY_LABEL), translatedTextView.getHint());
    }

    @Test
    public void shouldNotShowAudioIconInTranslationCardWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);
        View audioIconLayout = findView(activity, R.id.audio_icon_layout);
        assertEquals(View.GONE, audioIconLayout.getVisibility());
    }

    @Test
    public void shouldChangeIsAssetWhenAudioEditedAndWasAsset() {
        Translation isAudioAssetTranslation = createTranslation();
        isAudioAssetTranslation.setIsAsset(true);
        NewTranslation translationContext = new NewTranslation(createDefaultDictionary(), isAudioAssetTranslation, true);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(translationContext));
        Activity activity = createActivityToTest(RecordAudioActivity.class, context);
        click(activity, R.id.record_audio_button);
        assertEquals(false, isAudioAssetTranslation.getIsAsset());
    }

    @Test
    public void shouldDisplayLanguageTabsFragmentWhenActivityIsCreated() {
        Activity activity = createActivityToTest(RecordAudioActivity.class);

        assertNotNull(activity.findViewById(R.id.languages_scroll));
    }

    @Test
    public void shouldUpdatePlayButtonToStateWhenNewTranslationTabIsSelectedAndNoAudioHasBeenRecorded() {
        Activity activity = createActivityToTestWithMultipleNewTranslationContexts(RecordAudioActivity.class);

        clickLanguageTabAtPosition(activity, 1);

        View playButton = activity.findViewById(R.id.play_audio_button);
        assertEquals(R.color.grey, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldUpdateTranslatedTextWhenNewTranslationTabIsSelected() {
        Activity activity = createActivityToTestWithMultipleNewTranslationContexts(RecordAudioActivity.class);

        clickLanguageTabAtPosition(activity, 1);

        assertEquals("Arabic Translation", findTextView(activity, R.id.translated_text).getText().toString());
    }

    @Test
    public void shouldDisplayHintTextWhenNewTranslationTabIsSelectedWithNoTranslatedText() {
        Activity activity = createActivityToTestWithMultipleNewTranslationContexts(RecordAudioActivity.class);

        clickLanguageTabAtPosition(activity, 1);
        clickLanguageTabAtPosition(activity, 0);

        assertEquals("Add Dictionary translation", findTextView(activity, R.id.translated_text).getHint().toString());
    }

    @Test
    public void shouldEnableNextButtonWhenSecondLanguageHasAudioRecordedAndFirstLanguageDoesNot() {
        Activity activity = createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab(RecordAudioActivity.class);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertTrue(nextButton.isClickable());
    }

    @Test
    public void shouldStopAudioWhenPlayingAndLanguageTabIsChanged() {
        Activity activity = createActivityToTestWithMultipleNewTranslationContexts(RecordAudioActivity.class);

        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        clickLanguageTabAtPosition(activity, 1);

        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopRecordingWhenLanguageTabIsChanged() {
        Activity activity = createActivityToTestWithMultipleNewTranslationContexts(RecordAudioActivity.class);

        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        clickLanguageTabAtPosition(activity, 1);

        verify(getAudioRecorderManager()).stop();
    }
}