package org.mercycorps.translationcards.activity.addTranslation;


import android.app.Activity;
import android.view.View;
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
import static org.mockito.Matchers.anyChar;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterTranslatedPhraseActivityTest {
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    private static final String DEFAULT_TRANSLATED_TEXT = "Translation";
    private static final String EMPTY_STRING = "";

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
    public void shouldUpdateNewTranslationContextWhenBackButtonIsClickedAndATranslatedPhraseIsPresent() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        setText(activity, R.id.translated_phrase_field, DEFAULT_TRANSLATED_TEXT);
        click(activity, R.id.enter_translated_phrase_back_label);
        assertEquals(DEFAULT_TRANSLATED_TEXT, getContextFromIntent(activity).getTranslation().getTranslatedText());
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
        assertEquals("Optional: add the translation", summaryTitle.getText().toString());
    }

    @Test
    public void shouldDisplayDescriptionWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        TextView activityDescription = findTextView(activity, R.id.translated_phrase_activity_description);
        assertEquals("Add optional translation so you're able to show the card to people who may prefer to read.", activityDescription.getText().toString());
    }

    @Test
    public void shouldDisplayLanguageLabelWithCorrectLanguageWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class, createDefaultDictionary());
        TextView languageLabel = findTextView(activity, R.id.translated_phrase_input_language_label);
        assertEquals(String.format("%s TEXT", DEFAULT_DICTIONARY_LABEL.toUpperCase()), languageLabel.getText().toString());
    }

    @Test
    public void shouldDisplayNextLabelWhenInputIsEntered() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(EnterTranslatedPhraseActivity.class);
        TextView nextLabel = findTextView(activity, R.id.recording_label_next_text);
        assertEquals("NEXT", nextLabel.getText().toString());
    }

    @Test
    public void shouldDisplaySkipLabelWhenInputIsRemoved() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(EnterTranslatedPhraseActivity.class);
        TextView label = findTextView(activity, R.id.recording_label_next_text);
        TextView translatedText = findTextView(activity, R.id.translated_phrase_field);
        translatedText.setText("");
        assertEquals("SKIP", label.getText().toString());
    }

    @Test
    public void shouldDisplaySkipLabelWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        TextView skipLabel = findTextView(activity,R.id.recording_label_next_text);
        assertEquals("SKIP", skipLabel.getText().toString());
    }

    @Test
    public void shouldIncludeSourcePhraseInHeaderWhenActivityIsCreated() {
        Activity activity = createActivityToTestWithSourceAndTranslatedText(EnterTranslatedPhraseActivity.class);
        TextView sourceText = findTextView(activity, R.id.origin_translation_text);
        assertEquals(DEFAULT_SOURCE_PHRASE, sourceText.getText().toString());
    }

    @Test
    public void shouldNotShowTranslationTextIndicatorDivider() {
        Activity activity = createActivityToTest(EnterTranslatedPhraseActivity.class);
        View divider = findView(activity, R.id.text_indicator_divider);
        assertEquals(View.GONE, divider.getVisibility());
    }
}
