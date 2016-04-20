package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;
import org.mercycorps.translationcards.activity.addTranslation.EnterTranslatedPhraseActivity;
import org.mercycorps.translationcards.activity.addTranslation.SummaryActivity;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslationContext;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class SummaryActivityTest {
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";

    @Test
    public void shouldNotBeNull(){
        Activity activity = createActivityToTest(SummaryActivity.class);
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveValidTranslationContextOnStart(){ // TODO: 3/23/16 Does this even make sense to test this?
        Dictionary dict = createDefaultDictionary();
        Activity activity = createActivityToTest(SummaryActivity.class, dict);
        NewTranslationContext newTranslationContext = fetchTranslationContext(activity);
        assertEquals(dict, newTranslationContext.getDictionary());
    }

    @Test
    public void shouldStartTranslationActivityWhenUserClicksDone() {
        Dictionary dict = createDefaultDictionary();
        Activity activity = createActivityToTest(SummaryActivity.class, dict);
        activity.findViewById(R.id.save_translation_button).performClick();
    }

    @Test
    public void shouldShowTranslationSourcePhraseInCardView() {
        Activity activity = createActivityToTestWithTranslationContext(SummaryActivity.class);
        TextView sourceTextView = (TextView) activity.findViewById(R.id.origin_translation_text);
        assertEquals(DEFAULT_TRANSLATION_LABEL, sourceTextView.getText().toString());
    }

    @Test
    public void shouldShowTranslatedPhraseInCardView() {
        Activity activity = createActivityToTestWithTranslationContext(SummaryActivity.class);
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(DEFAULT_TRANSLATED_TEXT, translatedTextView.getText().toString());
    }

    @Test
    public void shouldShowHintTextWhenNoTranslatedTextPhraseIsProvided() {
        Activity activity = createActivityToTest(SummaryActivity.class, createDefaultDictionary());
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(String.format("Add %s translation", DEFAULT_DICTIONARY_LABEL), translatedTextView.getHint());
    }

    @Test
    public void shouldMakeTranslationChildLinearLayoutVisibleWhenLayoutIsLoaded() {
        Activity activity = createActivityToTest(SummaryActivity.class, createDefaultDictionary());
        assertEquals(View.VISIBLE, activity.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldMakeTranslationGrandchildLinearLayoutGone() {
        Activity activity = createActivityToTest(SummaryActivity.class, createDefaultDictionary());
        assertEquals(View.GONE, activity.findViewById(R.id.translation_grandchild).getVisibility());
    }

    @Test
    public void shouldPlayAudioFileWhenTranslationCardIsClicked() throws AudioFileException {
        Activity activity = createActivityToTestWithTranslationContext(SummaryActivity.class);
        activity.findViewById(R.id.summary_translation_card).performClick();
        ProgressBar progressBar = (ProgressBar) activity.findViewById(R.id.summary_progress_bar);
        verify(getDecoratedMediaManager()).play(DEFAULT_AUDIO_FILE, progressBar);
    }

    @Test
    public void shouldGoBackToEnterTranslatedTextActivityWhenEditIsClicked() throws Exception{
        Activity activity = createActivityToTest(SummaryActivity.class);
        activity.findViewById(R.id.go_to_enter_translated_phrase_activity).performClick();
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = createActivityToTest(SummaryActivity.class);
        ImageView getStartedImage = findImageView(activity, R.id.summary_image);
        assertEquals(R.drawable.summary_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSaveTranslationContextWhenUserClicksSave() {
        Activity activity = createActivityToTestWithTContextAndSourceText(SummaryActivity.class);
        click(activity, R.id.save_translation_button);
        verify(getDbManager()).saveTranslationContext(any(NewTranslationContext.class));
    }

    @Test
    public void shouldSetSummaryTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(SummaryActivity.class, createDefaultDictionary());
        TextView summaryTitle = findTextView(activity, R.id.summary_title);
        assertEquals(String.format("New %s flashcard created", DEFAULT_DICTIONARY_LABEL), summaryTitle.getText().toString());
    }

    @Test
    public void shouldHideActionBarWhenActivityIsCreated() {
        AppCompatActivity activity = createCompatActivityToTest(SummaryActivity.class);
        Assert.assertNotNull(activity.getSupportActionBar());
        assertFalse(activity.getSupportActionBar().isShowing());
    }

    @Test
    public void shouldSetSummaryDetailWhenActivityIsCreated() {
        Activity activity = createActivityToTest(SummaryActivity.class, createDefaultDictionary());
        TextView summaryDetail = findTextView(activity, R.id.summary_detail);
        assertEquals(String.format("Find your new flashcard at the top of the list in the %s category.", DEFAULT_DICTIONARY_LABEL), summaryDetail.getText().toString());
    }

    @Test
    public void shouldStopPlayingWhenPlayButtonIsClickedTwice() throws AudioFileNotSetException {
        setupAudioPlayerManager();
        Activity activity = createActivityToTestWithTranslationContext(SummaryActivity.class);
        click(activity, R.id.summary_translation_card);
        click(activity, R.id.summary_translation_card);
        verify(getDecoratedMediaManager()).stop();
    }

    @Test
    public void shouldShowCollapseIndicatorWhenTranslationCardIsCreated() {
        Activity activity = createActivityToTest(SummaryActivity.class);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldCollapseTranslationCardWhenCardIndicatorIsClicked() {
        Activity activity = createActivityToTest(SummaryActivity.class);
        click(activity, R.id.translation_indicator_layout);
        assertEquals(View.GONE, findView(activity, R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowExpandCardIndicatorWhenTranslationCardIsCollapsed() {
        Activity activity = createActivityToTest(SummaryActivity.class);
        click(activity, R.id.translation_indicator_layout);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.expand_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldExpandTranslationCardWhenCardIndicatorIsClickedTwice() {
        Activity activity = createActivityToTest(SummaryActivity.class);
        click(activity, R.id.translation_indicator_layout);
        click(activity, R.id.translation_indicator_layout);
        assertEquals(View.VISIBLE, findView(activity, R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowCollapseCardIndicatorWhenTranslationCardIsClickedTwice() {
        Activity activity = createActivityToTest(SummaryActivity.class);
        click(activity, R.id.translation_indicator_layout);
        click(activity, R.id.translation_indicator_layout);
        ImageView indicatorIcon = findImageView(activity, R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldStopPlayingAudioWhenSaveTranslationButtonIsClicked() {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(true);
        Activity activity = createActivityToTestWithTranslationContext(SummaryActivity.class);
        click(activity, R.id.save_translation_button);
        verify(getDecoratedMediaManager()).stop();
    }

    @Test
    public void shouldStopPlayingAudioWhenBackButtonIsClicked() {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(true);
        Activity activity = createActivityToTestWithTranslationContext(SummaryActivity.class);
        click(activity, R.id.go_to_enter_translated_phrase_activity);
        verify(getDecoratedMediaManager()).stop();
    }

    public static void setupAudioPlayerManager() throws AudioFileNotSetException {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(false).thenReturn(true);
    }
}