package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.service.TranslationService;
import org.mercycorps.translationcards.util.AddTranslationActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.clickLanguageTabAtPosition;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class SummaryActivityTest {

    AddTranslationActivityHelper<SummaryActivity> helper = new AddTranslationActivityHelper<>(SummaryActivity.class);

    @Inject DecoratedMediaManager decoratedMediaManager;
    @Inject TranslationService translationService;

    @Before
    public void setUp() throws Exception {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);
    }

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
        verify(translationService).saveTranslationContext(any(NewTranslation.class));
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

        clickLanguageTabAtPosition(activity, 0);

        TextView summaryDetail = findTextView(activity, R.id.summary_detail);
        assertEquals("Here's your new card. Before you save, be sure to review the phrase, translation, and recording.", summaryDetail.getText().toString());
    }


    @Test
    public void shouldStopPlayingAudioWhenSaveTranslationButtonIsClicked() {
        when(decoratedMediaManager.isPlaying()).thenReturn(true);
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.save_translation_button);
        verify(decoratedMediaManager).stop();
    }

    @Test
    public void shouldStopPlayingAudioWhenBackButtonIsClicked() {
        when(decoratedMediaManager.isPlaying()).thenReturn(true);
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        click(activity, R.id.summary_activity_back);
        verify(decoratedMediaManager).stop();
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

        clickLanguageTabAtPosition(activity, 1);

        assertEquals("It looks like you didn't record the phrase audio for this language. You can record the audio now but it's okay to come back later when you're ready."
                , findTextView(activity, R.id.summary_detail).getText().toString());
    }

    @Test
    public void shouldStopAudioWhenAudioIsPlayingAndDifferentLanguageTabSelected() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();
        when(decoratedMediaManager.isPlaying()).thenReturn(true);

        clickLanguageTabAtPosition(activity, 1);

        verify(decoratedMediaManager).stop();
    }


    public void setupAudioPlayerManager() throws AudioFileNotSetException {
        when(decoratedMediaManager.isPlaying()).thenReturn(false).thenReturn(true);
    }
}
