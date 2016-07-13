package org.mercycorps.translationcards.activity.addTranslation;

import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.service.PermissionService;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.util.AddTranslationActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowToast;

import java.util.Collections;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.DEFAULT_AUDIO_FILE;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.DEFAULT_DICTIONARY_LABEL;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.DEFAULT_SOURCE_PHRASE;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.DEFAULT_TRANSLATED_TEXT;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.clickLanguageTabAtPosition;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.createTranslation;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getAudioPlayerManager;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getAudioRecorderManager;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getFirstNewTranslationFromContext;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.setupAudioPlayerManager;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RecordAudioActivityTest {

    private static final boolean IS_NOT_ASSET = false;

    AddTranslationActivityHelper<RecordAudioActivity> helper = new AddTranslationActivityHelper<>(RecordAudioActivity.class);
    private PermissionService permissionService = ((TestMainApplication)RuntimeEnvironment.application).getPermissionService();

    @Before
    public void setup() {
        when(permissionService.checkPermission(any(Activity.class), anyString())).thenReturn(true);
    }

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void playButtonShouldBeGreyWhenActivityStarts() {
        Activity activity = helper.createActivityToTest();
        View playButton = findView(activity, R.id.play_audio_button);
        assertEquals(R.color.grey, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldNotBeAbleToClickPlayButtonWhenThereIsNoAudioFilePresent() {
        Activity activity = helper.createActivityToTest();
        View playButton = findView(activity, R.id.play_audio_button);
        assertFalse(playButton.isClickable());
    }

    @Test
    public void shouldBeAbleToClickPlayButtonWhenAudioFileIsPresent() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        View playButton = findView(activity, R.id.play_audio_button);
        assertTrue(playButton.isClickable());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsNotRecording() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertTrue(nextButton.isClickable());
    }

    @Test
    public void shouldDisableNextButtonWhenNoAudioFileIsPresent() {
        Activity activity = helper.createActivityToTest();
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertFalse(nextButton.isClickable());
    }

    @Test
    public void shouldShowNextButtonWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioHasBeenRecorded() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertTrue(nextButton.isClickable());
    }

    @Test
    public void shouldEnableNextButtonWhenAudioFileIsPresentAndIsRecordingIsFinished() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldEnableBackButtonWhenAudioFileIsPresentAndIsRecordingIsFinished() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_back);
        assertEquals(View.VISIBLE, nextButton.getVisibility());
    }

    @Test
    public void shouldDisplayGreyNextButtonTextAndArrowWhenActivityIsCreatedWithoutRecording() {
        Activity activity = helper.createActivityToTest();
        TextView nextButtonText = findTextView(activity, R.id.recording_audio_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayGreyNextButtonArrowWhenActivityIsCreatedWithoutRecording() {
        Activity activity = helper.createActivityToTest();
        ImageView nextButtonArrow = findImageView(activity, R.id.recording_audio_next_arrow);
        assertEquals(R.drawable.forward_arrow_disabled, shadowOf(nextButtonArrow.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisplayBlackNextButtonTextWhenActivityIsCreatedWithARecording() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView nextButtonText = findTextView(activity, R.id.recording_audio_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayBlackNextButtonArrowWhenActivityIsCreatedWithARecording() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        ImageView nextButtonArrow = findImageView(activity, R.id.recording_audio_next_arrow);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonArrow.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisplayBlackNextButtonTextWhenActivityIsCreatedWithoutRecordingThenWeRecord() {
        Activity activity = helper.createActivityToTest();
        TextView nextButtonText = findTextView(activity, R.id.recording_audio_next_text);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldDisplayBlackNextButtonArrowWhenActivityIsCreatedWithoutRecordingThenWeRecord() {
        Activity activity = helper.createActivityToTest();
        ImageView nextButtonArrow = findImageView(activity, R.id.recording_audio_next_arrow);
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonArrow.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisableNextButtonWhenRecordingIsHappening() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab();
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_audio_button);

        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertEquals(false, nextButton.isClickable());

        TextView nextButtonText = findTextView(activity, R.id.recording_audio_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonText.getCurrentTextColor());

        ImageView nextButtonArrow = findImageView(activity, R.id.recording_audio_next_arrow);
        assertEquals(R.drawable.forward_arrow_disabled, shadowOf(nextButtonArrow.getBackground()).getCreatedFromResId());
    }


    @Test
    public void shouldDisableBackButtonWhenRecordingIsHappening() {
        Activity activity = helper.createActivityToTest();
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_audio_button);

        LinearLayout backButton = findLinearLayout(activity, R.id.record_activity_back);
        assertEquals(false, backButton.isClickable());

        ImageView backButtonArrow = findImageView(activity, R.id.record_activity_back_arrow);
        assertEquals(R.drawable.back_arrow_disabled, shadowOf(backButtonArrow.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldHaveAValidFileNameAfterRecordingIsComplete() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        assertTrue(getFirstNewTranslationFromContext(activity).getTranslation().getFilename().contains("TranslationCards"));
    }

    @Test
    public void shouldPlayAudioFileWhenPlayButtonIsClicked() throws Exception {
        setupAudioPlayerManager();
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.play_audio_button);
        verify(getAudioPlayerManager()).play(DEFAULT_AUDIO_FILE, IS_NOT_ASSET);
    }

    @Test
    public void shouldStartEnterTranslatedPhraseActivityWhenBackButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.record_activity_back);
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartSummaryActivityWhenNextButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.record_activity_next);
        assertEquals(SummaryActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldRecordAudioActivityTitleWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView recordAudioTitle = findTextView(activity, R.id.record_audio_title);
        assertEquals(("Record your phrase"), recordAudioTitle.getText().toString());
    }

    @Test
    public void shouldShowTranslationSourceTextIfSourceTextExistsInNewTranslationContext() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView translationCardOriginText = findTextView(activity, R.id.origin_translation_text);
        assertEquals(DEFAULT_SOURCE_PHRASE, translationCardOriginText.getText().toString());
    }

    @Test
    public void shouldHavePlayButtonBeClickableWhenAFileIsRecorded() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        View playButton = activity.findViewById(R.id.play_audio_button);
        assertTrue(playButton.isClickable());
    }

    @Test
    public void shouldChangePlayButtonsBackgroundToGreenWhenItBecomesClickable() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        View playButton = activity.findViewById(R.id.play_audio_button);
        assertEquals(R.color.green, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordBackgroundToDeepRedWhenItIsRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        View recordButton = activity.findViewById(R.id.record_audio_button);
        assertEquals(R.color.deep_red, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

     @Test
    public void shouldChangeRecordAudioIconToStopIconWhenItIsRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        View recordIcon = activity.findViewById(R.id.record_audio_icon);
        assertEquals(R.drawable.stop, shadowOf(recordIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordButtonBackgroundToRedWhenFinishedRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        View recordButton = activity.findViewById(R.id.record_audio_button);
        assertEquals(R.color.red, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordAudioIconToRecordIconWhenFinishedRecording() {
        when(getAudioRecorderManager().isRecording()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.record_audio_button);
        View recordIcon = activity.findViewById(R.id.record_audio_icon);
        assertEquals(R.drawable.record, shadowOf(recordIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordButtonBackgroundToRedWhenItIsFinishedRecordingByPressingPlay() {
        Activity activity = helper.createActivityToTest();

        when(getAudioRecorderManager().isRecording())
                .thenReturn(false)
                .thenReturn(true);
        click(activity, R.id.record_audio_button);

        when(getAudioRecorderManager().isRecording())
                .thenReturn(true)
                .thenReturn(false);
        click(activity, R.id.play_audio_button);

        View recordButton = activity.findViewById(R.id.record_audio_button);

        assertEquals(R.color.red, shadowOf(recordButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeRecordButtonIconToRecordIconWhenItIsFinishedRecordingByPressingPlay() {
        when(getAudioRecorderManager().isRecording()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.play_audio_button);
        View recordIcon = activity.findViewById(R.id.record_audio_icon);
        assertEquals(R.drawable.record, shadowOf(recordIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldStopRecordingWhenPlayButtonIsClicked() {
        Activity activity = helper.createActivityToTest();
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.play_audio_button);
        verify(getAudioRecorderManager()).stop();
    }

    @Test
    public void shouldStopPlayingWhenRecordButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.play_audio_button);
        click(activity, R.id.record_audio_button);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopPlayingWhenNextButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.record_activity_next);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopPlayingWhenBackButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        click(activity, R.id.record_activity_back);
        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopRecordingWhenNextButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_activity_next);
        verify(getAudioRecorderManager()).stop();
    }

    @Test
    public void shouldStopRecordingWhenBackButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        click(activity, R.id.record_activity_back);
        verify(getAudioRecorderManager()).stop();
    }

    @Test
    public void shouldHaveVisibleBackButtonWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        assertEquals(View.VISIBLE, activity.findViewById(R.id.record_activity_back).getVisibility());
    }

    @Test
    public void shouldEnableBackButtonsWhenRecordingIsStoppedByPlayClick() {
        when(getAudioRecorderManager().isRecording()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.play_audio_button);
        assertEquals(View.VISIBLE, activity.findViewById(R.id.record_activity_back).getVisibility());
    }

    @Test
    public void shouldEnableNextButtonWhenRecordingIsStoppedByPlayClick() {
        when(getAudioRecorderManager().isRecording()).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.record_audio_button);
        click(activity, R.id.play_audio_button);
        assertEquals(View.VISIBLE, activity.findViewById(R.id.record_activity_next).getVisibility());
    }

    @Test
    public void shouldShowCollapseArrowIconWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        ImageView expandArrowIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(expandArrowIcon.getBackground()).getCreatedFromResId());
    }


    @Test
    public void shouldDisplayDescriptionWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView activityDescription = findTextView(activity, R.id.recording_audio_instructions);
        assertEquals("Tap record then speak clearly at a normal speed. When you're done, tap record again. Play back and re-record until you're satisfied.", activityDescription.getText().toString());
    }

    @Test
    public void shouldBeAbleToClickIndicatorIcon() {
        Activity activity = helper.createActivityToTest();
        View indicatorLayout = findView(activity, R.id.translation_indicator_layout);
        assertTrue(indicatorLayout.isClickable());
    }


    @Test
    public void shouldMakeTranslationGrandchildLinearLayoutGone() {
        Activity activity = helper.createActivityToTest();
        assertEquals(View.GONE, activity.findViewById(R.id.translation_grandchild).getVisibility());
    }

    @Test
    public void shouldShowExpandCardIndicatorWhenTranslationCardIsCollapsed() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.translation_indicator_layout);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.expand_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }


    @Test
    public void shouldShowCollapseCardIndicatorWhenTranslationCardIsClickedTwice() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.translation_indicator_layout);
        click(activity, R.id.translation_indicator_layout);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldShowTranslatedPhraseInCardView() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(DEFAULT_TRANSLATED_TEXT, translatedTextView.getText().toString());
    }

    @Test
    public void shouldShowHintTextWhenNoTranslatedTextPhraseIsProvided() {
        Activity activity = helper.createActivityToTest();
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(String.format("Add %s translation", DEFAULT_DICTIONARY_LABEL), translatedTextView.getHint());
    }

    @Test
    public void shouldNotShowAudioIconInTranslationCardWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        View audioIconLayout = findView(activity, R.id.audio_icon_layout);
        assertEquals(View.GONE, audioIconLayout.getVisibility());
    }

    @Test
    public void shouldChangeIsAssetWhenAudioEditedAndWasAsset() {
        Translation isAudioAssetTranslation = createTranslation();
        isAudioAssetTranslation.setIsAsset(true);
        NewTranslation translationContext = new NewTranslation(helper.dictionary, isAudioAssetTranslation, true);
        AddNewTranslationContext context = new AddNewTranslationContext(Collections.singletonList(translationContext));
        Activity activity = helper.createActivityToTestWithContext(context);
        click(activity, R.id.record_audio_button);
        assertEquals(false, isAudioAssetTranslation.getIsAsset());
    }

    @Test
    public void shouldDisplayLanguageTabsFragmentWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();

        assertNotNull(activity.findViewById(R.id.languages_scroll));
    }

    @Test
    public void shouldUpdatePlayButtonToStateWhenNewTranslationTabIsSelectedAndNoAudioHasBeenRecorded() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        clickLanguageTabAtPosition(activity, 1);

        View playButton = activity.findViewById(R.id.play_audio_button);
        assertEquals(R.color.grey, shadowOf(playButton.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldUpdateTranslatedTextWhenNewTranslationTabIsSelected() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        clickLanguageTabAtPosition(activity, 1);

        assertEquals("Arabic Translation", findTextView(activity, R.id.translated_text).getText().toString());
    }

    @Test
    public void shouldDisplayHintTextWhenNewTranslationTabIsSelectedWithNoTranslatedText() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        clickLanguageTabAtPosition(activity, 1);
        clickLanguageTabAtPosition(activity, 0);

        assertEquals("Add Dictionary translation", findTextView(activity, R.id.translated_text).getHint().toString());
    }

    @Test
    public void shouldEnableNextButtonWhenSecondLanguageHasAudioRecordedAndFirstLanguageDoesNot() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab();
        LinearLayout nextButton = findLinearLayout(activity, R.id.record_activity_next);
        assertTrue(nextButton.isClickable());
    }

    @Test
    public void shouldStopAudioWhenPlayingAndLanguageTabIsChanged() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        when(getAudioPlayerManager().isPlaying()).thenReturn(true);
        clickLanguageTabAtPosition(activity, 1);

        verify(getAudioPlayerManager()).stop();
    }

    @Test
    public void shouldStopRecordingWhenLanguageTabIsChanged() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        when(getAudioRecorderManager().isRecording()).thenReturn(true);
        clickLanguageTabAtPosition(activity, 1);

        verify(getAudioRecorderManager()).stop();
    }

    @Test
    public void shouldCheckRecordingPermissionWhenRecordButtonIsPressed() {
        Activity activity = helper.createActivityToTest();

        click(activity, R.id.record_audio_button);

        verify(permissionService).checkPermission(activity, Manifest.permission.RECORD_AUDIO);
    }



    @Test
    public void shouldShowPermissionDialogWhenRecordButtonIsPressedAndPermissionDoesNotExist() {
        Activity activity = helper.createActivityToTest();
        when(permissionService.checkPermission(activity, Manifest.permission.RECORD_AUDIO)).thenReturn(false);

        click(activity, R.id.record_audio_button);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        View dialog = alertDialog.findViewById(R.id.microphone_permission_dialog);
        Assert.assertNotNull(dialog);
    }

    @Test
    public void shouldRequestRecordingPermissionWhenPermissionDialogButtonIsPressed() {
        Activity activity = helper.createActivityToTest();
        when(permissionService.checkPermission(activity, Manifest.permission.RECORD_AUDIO)).thenReturn(false);

        click(activity, R.id.record_audio_button);
        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();

        verify(permissionService).requestPermissions(activity, Manifest.permission.RECORD_AUDIO, PermissionService.PERMISSIONS_REQUEST_RECORD_AUDIO);
    }


    @Test
    public void shouldShowPositiveToastMessageIfPermissionNotGranted() {
        RecordAudioActivity activity = (RecordAudioActivity)helper.createActivityToTest();
        int grantResults[] = {};
        when(permissionService.permissionGranted(grantResults)).thenReturn(true);

        activity.onRequestPermissionsResult(0, new String[]{}, grantResults);

        assertEquals(activity.getString(R.string.record_audio_permission_granted), ShadowToast.getTextOfLatestToast());
    }
}
