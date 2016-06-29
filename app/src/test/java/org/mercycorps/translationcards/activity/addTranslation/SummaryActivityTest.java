package org.mercycorps.translationcards.activity.addTranslation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;
import org.mercycorps.translationcards.util.AddTranslationActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class SummaryActivityTest {
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    private static final boolean IS_NOT_ASSET = false;

    AddTranslationActivityHelper<SummaryActivity> helper = new AddTranslationActivityHelper<>(SummaryActivity.class);;

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldNotBeNull(){
        Activity activity = helper.createActivityToTest();
        assertNotNull(activity);
    }

    @Test
    public void shouldStartTranslationActivityWhenUserClicksDone() {
        Activity activity = helper.createActivityToTest();
        activity.findViewById(R.id.save_translation_button).performClick();
    }

    @Test
    public void shouldShowTranslationSourcePhraseInCardView() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView sourceTextView = (TextView) activity.findViewById(R.id.origin_translation_text);
        assertEquals(helper.DEFAULT_SOURCE_PHRASE, sourceTextView.getText().toString());
    }

    @Test
    public void shouldShowTranslatedPhraseInCardView() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(helper.DEFAULT_TRANSLATED_TEXT, translatedTextView.getText().toString());
    }

    @Test
    public void shouldShowHintTextWhenNoTranslatedTextPhraseIsProvided() {
        Activity activity = helper.createActivityToTest();
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(String.format("Add %s translation", helper.DEFAULT_DICTIONARY_LABEL), translatedTextView.getHint());
    }

    @Test
    public void shouldSetTextViewToEmptyStringWhenNoTranslatedTextPhraseIsProvided() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        clickLanguageTabAtPosition(activity, 1);
        clickLanguageTabAtPosition(activity, 0);

        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals("", translatedTextView.getText().toString());
    }

    @Test
    public void shouldMakeTranslationChildLinearLayoutVisibleWhenLayoutIsLoaded() {
        Activity activity = helper.createActivityToTest();
        assertEquals(View.VISIBLE, activity.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldMakeTranslationGrandchildLinearLayoutGone() {
        Activity activity = helper.createActivityToTest();
        assertEquals(View.GONE, activity.findViewById(R.id.translation_grandchild).getVisibility());
    }

    @Test
    public void shouldPlayAudioFileWhenTranslationCardIsClicked() throws AudioFileException {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        activity.findViewById(R.id.summary_translation_card).performClick();
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.summary_progress_bar);
        verify(getDecoratedMediaManager()).play(helper.DEFAULT_AUDIO_FILE, progressBar, IS_NOT_ASSET);
    }

    @Test
    public void shouldShowToastNotificationWhenTranslationCardWithoutAudioFileIsClicked() throws AudioFileException {
        Activity activity = helper.createActivityToTest();
        when(getDecoratedMediaManager().isPlaying()).thenReturn(false);
        doThrow(new AudioFileException()).when(getDecoratedMediaManager()).play(anyString(), any(ProgressBar.class), anyBoolean());

        activity.findViewById(R.id.summary_translation_card).performClick();

        assertEquals(helper.DEFAULT_DICTIONARY_LABEL + " translation not recorded.", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void shouldGoBackToEnterTranslatedTextActivityWhenEditIsClicked() throws Exception{
        Activity activity = helper.createActivityToTest();
        activity.findViewById(R.id.summary_activity_back).performClick();
        assertEquals(RecordAudioActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = helper.createActivityToTest();
        ImageView getStartedImage = findImageView(activity, R.id.enter_source_language_image);
        assertEquals(R.drawable.enter_source_language_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSaveTranslationContextWhenUserClicksSave() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.save_translation_button);
        verify(getDbManager()).saveTranslationContext(any(NewTranslation.class));
    }

    @Test
    public void shouldSetSummaryTitleWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView summaryTitle = findTextView(activity, R.id.summary_title);
        assertEquals("Review and save your card", summaryTitle.getText().toString());
    }

    @Test
    public void shouldHideActionBarWhenActivityIsCreated() {
        AppCompatActivity activity = (AppCompatActivity) helper.createActivityToTest();
        Assert.assertNotNull(activity.getSupportActionBar());
        assertFalse(activity.getSupportActionBar().isShowing());
    }

    @Test
    public void shouldSetSummaryDetailWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        clickLanguageTabAtPosition(activity, 1);

        TextView summaryDetail = findTextView(activity, R.id.summary_detail);
        assertEquals("Here's your new card. Before you save, be sure to review the phrase, translation, and recording.", summaryDetail.getText().toString());
    }

    @Test
    public void shouldStopPlayingWhenPlayButtonIsClickedTwice() throws AudioFileNotSetException {
        setupAudioPlayerManager();
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.summary_translation_card);
        click(activity, R.id.summary_translation_card);
        verify(getDecoratedMediaManager()).stop();
    }

    @Test
    public void shouldShowCollapseIndicatorWhenTranslationCardIsCreated() {
        Activity activity = helper.createActivityToTest();
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldCollapseTranslationCardWhenCardIndicatorIsClicked() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.translation_indicator_layout);
        assertEquals(View.GONE, findView(activity, R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowExpandCardIndicatorWhenTranslationCardIsCollapsed() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.translation_indicator_layout);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.expand_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldExpandTranslationCardWhenCardIndicatorIsClickedTwice() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.translation_indicator_layout);
        click(activity, R.id.translation_indicator_layout);
        assertEquals(View.VISIBLE, findView(activity, R.id.translation_child).getVisibility());
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
    public void shouldStopPlayingAudioWhenSaveTranslationButtonIsClicked() {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(true);
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.save_translation_button);
        verify(getDecoratedMediaManager()).stop();
    }

    @Test
    public void shouldStopPlayingAudioWhenBackButtonIsClicked() {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(true);
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.summary_activity_back);
        verify(getDecoratedMediaManager()).stop();
    }

    @Test
    public void shouldDisplayLanguageTabsFragmentWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();

        assertNotNull(activity.findViewById(R.id.languages_scroll));
    }

    @Test
    public void shouldChangeTranslatedPhraseWhenANewLanguageTabIsSelected() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        clickLanguageTabAtPosition(activity, 1);

        assertEquals("Arabic Translation", findTextView(activity, R.id.translated_text).getText().toString());
    }

    @Test
    public void shouldUpdateSummaryDetailWhenALanguageTabIsSelectedWithNoAudioRecording() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();

        assertEquals("It looks like you didn't record the phrase audio for this language. You can record the audio now but it's okay to come back later when you're ready."
                , findTextView(activity, R.id.summary_detail).getText().toString());
    }

    @Test
    public void shouldStopAudioWhenAudioIsPlayingAndDifferentLanguageTabSelected() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();
        when(getDecoratedMediaManager().isPlaying()).thenReturn(true);

        clickLanguageTabAtPosition(activity, 1);

        verify(getDecoratedMediaManager()).stop();
    }

    @Test
    public void shouldGreyOutTranslationSourceTextWhenItContainsNoAudio() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab();

        TextView translationText = findTextView(activity, R.id.origin_translation_text);
        assertEquals(getColor(activity, R.color.textDisabled), translationText.getCurrentTextColor());
    }

    @Test
    public void shouldGreyOutTranslationDestinationTextWhenItContainsNoAudio() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab();

        TextView translationText = findTextView(activity, R.id.translated_text);
        assertEquals(getColor(activity, R.color.textDisabled), translationText.getCurrentTextColor());
    }

    @Test
    public void shouldGreyOutTranslationCardWhenItContainsNoAudio() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab();

        LinearLayout translationCard = findLinearLayout(activity, R.id.summary_translation_card);
        assertEquals(translationCard.getAlpha(), SummaryActivity.DISABLED_OPACITY);
    }

    @Test
    @TargetApi(19)
    public void shouldGreyOutAudioIconWhenTranslationContainsNoAudio() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContextsAudioOnSecondTab();
        ImageView audioIcon = findImageView(activity, R.id.audio_icon);
        assertEquals(SummaryActivity.DISABLED_BITMAP_OPACITY, audioIcon.getDrawable().getAlpha());
    }

    public void setupAudioPlayerManager() throws AudioFileNotSetException {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(false).thenReturn(true);
    }
}
