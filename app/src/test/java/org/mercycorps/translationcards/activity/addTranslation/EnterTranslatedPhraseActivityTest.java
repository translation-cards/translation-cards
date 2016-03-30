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

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterTranslatedPhraseActivityTest {
    public static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_TRANSLATION_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    private static final String DEFAULT_TRANSLATED_TEXT = "Translation";
    private static final String EMPTY_STRING = "";
    private static final String DEFAULT_TRANSLATION_LABEL = "TranslationLabel";

    @Test
    public void shouldNotBeNull(){
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveValidTranslationContextOnStart(){
        Dictionary dict = createDefaultDictionary();
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class, dict);
        assertEquals(dict, getContextFromIntent(activity).getDictionary());
    }

    @Test
    public void shouldHaveTranslatedTextPhraseTextViewInIntent(){ //Testing Butter Knife hookup
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        assertNotNull(findTextView(activity, R.id.translated_phrase_field));
    }

    @Test
    public void shouldUpdateTranslationContextWithTranslatedTextWhenSaveButtonIsClicked(){
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        setText(activity, R.id.translated_phrase_field, DEFAULT_TRANSLATED_TEXT);
        click(activity, R.id.enter_translated_phrase_next_label);
        assertEquals(DEFAULT_TRANSLATED_TEXT, getContextFromIntent(activity).getTranslation().getTranslatedText());
    }

    @Test
    public void shouldUpdateTranslationContextWithEmptyStringWhenNoTranslationAddedAndSaveButtonIsClicked() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        click(activity, R.id.enter_translated_phrase_next_label);
        assertEquals(EMPTY_STRING, getContextFromIntent(activity).getTranslation().getTranslatedText());
    }

    @Test
    public void shouldStartRecordAudioActivityWhenUserClicksSave() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        click(activity, R.id.enter_translated_phrase_next_label);
        assertEquals(RecordAudioActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartEnterSourcePhraseActivityWhenBackButtonIsClicked(){
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        click(activity, R.id.enter_translated_phrase_back_label);
        assertEquals(EnterSourcePhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        ImageView getStartedImage = findImageView(activity, R.id.enter_translated_phrase_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldPopulateTranslatedPhraseFieldWithValueWhenTranslationContextHasTranslatedText(){
        Activity activity = createActivityToTestWithTranslationContext(EnterTranslatedPhraseActivity.class);
        TextView translatedPhraseTextField = findTextView(activity, R.id.translated_phrase_field);
        assertEquals(getContextFromIntent(activity).getTranslation().getTranslatedText(), translatedPhraseTextField.getText().toString());
    }

    @Test
    public void shouldSetEnterTranslatedTextActivityTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class, createDefaultDictionary());
        TextView summaryTitle = findTextView(activity, R.id.translated_phrase_title);
        assertEquals(String.format("Write your %s translation", DEFAULT_DICTIONARY_LABEL), summaryTitle.getText().toString());
    }
}
