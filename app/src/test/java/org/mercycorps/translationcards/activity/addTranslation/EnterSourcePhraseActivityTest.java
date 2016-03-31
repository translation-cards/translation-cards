package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
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

    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_TRANSLATION_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    private static final String NO_TEXT = "";

    @Test
    public void shouldNotChangeNewTranslationContextWhenStartingActivity() {
        Dictionary dict = createDefaultDictionary();
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class, dict);
        NewTranslationContext newTranslationContext = (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(dict, newTranslationContext.getDictionary());
    }

    @Test
    public void shouldStartEnterTranslatedPhraseActivityWhenUserClicksNext() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setSourceTextAndClick(activity);
        assertEquals(EnterTranslatedPhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldUpdateNewTranslationContextWhenUserClicksNextWithValidSourceText() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setSourceTextAndClick(activity);
        assertEquals(DEFAULT_TRANSLATION_TEXT, getContextFromIntent(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldUpdateTranslationSourceTextToUserInputtedText() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setSourceTextAndClick(activity);
        assertEquals(DEFAULT_TRANSLATION_TEXT, getContextFromIntent(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldNotStartNextActivityWhenThereIsNoSourceText() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        click(activity, R.id.activity_enter_source_phrase_next_label);
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void shouldNotUpdateNewTranslationContextWhenThereIsNoSourceText() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        click(activity, R.id.activity_enter_source_phrase_next_label);
        assertEquals(null, getContextFromIntent(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldNotHaveNextButtonClickableWhenThereIsNoSourceText() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        assertFalse(findLinearLayout(activity, R.id.activity_enter_source_phrase_next_label).isClickable());
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        ImageView getStartedImage = findImageView(activity, R.id.enter_source_phrase_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldGoToGetStartedActivityWhenCancelButtonIsClicked() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        click(activity, R.id.enter_source_phrase_activity_back_label);
        assertEquals(GetStartedActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldUpdateNewTranslationContextWhenUserClicksBackWithValidSourceText() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        click(activity, R.id.enter_source_phrase_activity_back_label);
        assertEquals(DEFAULT_TRANSLATION_TEXT, getContextFromIntent(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldSetEnterSourcePhraseActivityTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class, createDefaultDictionary());
        TextView sourcePhraseActivityTitle = findTextView(activity, R.id.source_phrase_title);
        assertEquals("Write your phrase", sourcePhraseActivityTitle.getText().toString());
    }

    @Test
    public void shouldPopulateSourcePhraseFieldWithValueWhenTranslationContextHasSourceText(){
        Activity activity = createActivityToTestWithTContextAndSourceText(EnterSourcePhraseActivity.class);
        TextView sourcePhraseField = findTextView(activity, R.id.source_phrase_field);
        assertEquals(DEFAULT_SOURCE_PHRASE, sourcePhraseField.getText().toString());
    }

    @Test
    public void shouldChangeNextButtonColorWhenSourcePhraseIsNotEmpty() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        TextView nextButtonLabelText = findTextView(activity, R.id.activity_enter_source_phrase_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenSourcePhraseIsNotEmpty() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        ImageView nextButtonImage = findImageView(activity, R.id.activity_enter_source_phrase_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeNextButtonColorWhenSourcePhraseIsSetEmpty() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        setText(activity, R.id.source_phrase_field, NO_TEXT);
        TextView nextButtonLabelText = findTextView(activity, R.id.activity_enter_source_phrase_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorToDisabledWhenSourcePhraseIsSetEmpty() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        setText(activity, R.id.source_phrase_field, NO_TEXT);
        ImageView nextButtonImage = findImageView(activity, R.id.activity_enter_source_phrase_next_image);
        assertEquals(R.drawable.forward_arrow_40p, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    private void setSourceTextAndClick(Activity activity) {
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        click(activity, R.id.activity_enter_source_phrase_next_label);
    }

}
