package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.util.AddTranslationActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterSourcePhraseActivityTest {

    public static final String DEFAULT_TRANSLATION_TEXT = "Sleep here";
    private static final String NO_TEXT = "";

    AddTranslationActivityHelper<EnterSourcePhraseActivity> helper = new AddTranslationActivityHelper<>(EnterSourcePhraseActivity.class);

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldNotChangeNewTranslationContextWhenStartingActivity() {
        Activity activity = helper.createActivityToTest();
        NewTranslation newTranslation = getFirstNewTranslationFromContext(activity);
        assertEquals(helper.dictionary, newTranslation.getDictionary());
    }

    @Test
    public void shouldStartEnterTranslatedPhraseActivityWhenUserClicksNext() {
        Activity activity = helper.createActivityToTest();
        setSourceTextAndClick(activity);
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldUpdateNewTranslationContextWhenUserClicksNextWithValidSourceText() {
        Activity activity = helper.createActivityToTest();
        setSourceTextAndClick(activity);
        assertEquals(DEFAULT_TRANSLATION_TEXT, getFirstNewTranslationFromContext(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldNotStartNextActivityWhenThereIsNoSourceText() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.activity_enter_source_phrase_next_label);
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void shouldNotUpdateNewTranslationContextWhenThereIsNoSourceText() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.activity_enter_source_phrase_next_label);
        assertEquals("", getFirstNewTranslationFromContext(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldNotHaveNextButtonClickableWhenThereIsNoSourceText() {
        Activity activity = helper.createActivityToTest();
        assertFalse(findLinearLayout(activity, R.id.activity_enter_source_phrase_next_label).isClickable());
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = helper.createActivityToTest();
        ImageView getStartedImage = findImageView(activity, R.id.enter_source_phrase_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldGoToGetStartedActivityWhenCancelButtonIsClicked() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_source_phrase_activity_back_label);
        assertEquals(GetStartedActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldUpdateNewTranslationContextWhenUserClicksBackWithValidSourceText() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        click(activity, R.id.enter_source_phrase_activity_back_label);
        assertEquals(DEFAULT_TRANSLATION_TEXT, getFirstNewTranslationFromContext(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldSetEnterSourcePhraseActivityTitleWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView sourcePhraseActivityTitle = findTextView(activity, R.id.source_phrase_title);
        assertEquals("Write your phrase", sourcePhraseActivityTitle.getText().toString());
    }

    @Test
    public void shouldPopulateSourcePhraseFieldWithValueWhenTranslationContextHasSourceText(){
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView sourcePhraseField = findTextView(activity, R.id.source_phrase_field);
        assertEquals(DEFAULT_SOURCE_PHRASE, sourcePhraseField.getText().toString());
    }

    @Test
    public void shouldChangeNextButtonColorWhenSourcePhraseIsNotEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        TextView nextButtonLabelText = findTextView(activity, R.id.activity_enter_source_phrase_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenSourcePhraseIsNotEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        ImageView nextButtonImage = findImageView(activity, R.id.activity_enter_source_phrase_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeNextButtonColorWhenSourcePhraseIsSetEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        setText(activity, R.id.source_phrase_field, NO_TEXT);
        TextView nextButtonLabelText = findTextView(activity, R.id.activity_enter_source_phrase_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorToDisabledWhenSourcePhraseIsSetEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        setText(activity, R.id.source_phrase_field, NO_TEXT);
        ImageView nextButtonImage = findImageView(activity, R.id.activity_enter_source_phrase_next_image);
        assertEquals(R.drawable.forward_arrow_40p, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisplayDescriptionWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView activityDescription = findTextView(activity, R.id.source_phrase_description);
        assertEquals("Keep it short, direct, and really clear. Your phrase should make it really easy for the listener to know how to respond.", activityDescription.getText().toString());
    }

    @Test
    public void shouldDisplayInputFieldHintWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView inputFieldHint = findTextView(activity, R.id.source_phrase_field);
        assertEquals("e.g. Wait here for 30 minutes", inputFieldHint.getHint().toString());
    }

    @Test
    public void shouldStartTranslationsActivityWhenBackButtonIsPressedInEditMode() {
        Activity activity = helper.createActivityToTestInEditMode();
        click(activity, R.id.enter_source_phrase_activity_back_label);
        assertEquals(TranslationsActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    private void setSourceTextAndClick(Activity activity) {
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        click(activity, R.id.activity_enter_source_phrase_next_label);
    }

}
