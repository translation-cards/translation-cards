package org.mercycorps.translationcards.activity.addTranslation;


import android.app.Activity;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.util.AddTranslationActivityHelper;
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
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    private static final String DEFAULT_TRANSLATED_TEXT = "Translation";
    private static final String EMPTY_STRING = "";

    AddTranslationActivityHelper<EnterTranslatedPhraseActivity> helper = new AddTranslationActivityHelper<>(EnterTranslatedPhraseActivity.class);

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
    public void shouldHaveValidTranslationContextOnStart(){
        Activity activity = helper.createActivityToTest();
        assertEquals(helper.dictionary, getFirstNewTranslationFromContext(activity).getDictionary());
    }

    @Test
    public void shouldHaveTranslatedTextPhraseTextViewInIntent(){ //Testing Butter Knife hookup
        Activity activity = helper.createActivityToTest();
        assertNotNull(findTextView(activity, R.id.translated_phrase_field));
    }

    @Test
    public void shouldUpdateTranslationContextWithTranslatedTextWhenSaveButtonIsClicked(){
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.translated_phrase_field, DEFAULT_TRANSLATED_TEXT);
        click(activity, R.id.enter_translated_phrase_next_label);
        assertEquals(DEFAULT_TRANSLATED_TEXT, getFirstNewTranslationFromContext(activity).getTranslation().getTranslatedText());
    }

    @Test
    public void shouldUpdateTranslationContextWithEmptyStringWhenNoTranslationAddedAndSaveButtonIsClicked() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_translated_phrase_next_label);
        assertEquals(EMPTY_STRING, getFirstNewTranslationFromContext(activity).getTranslation().getTranslatedText());
    }

    @Test
    public void shouldStartRecordAudioActivityWhenUserClicksSave() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_translated_phrase_next_label);
        assertEquals(RecordAudioActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartEnterSourcePhraseActivityWhenBackButtonIsClicked(){
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_translated_phrase_back_label);
        assertEquals(EnterSourcePhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldUpdateNewTranslationContextWhenBackButtonIsClickedAndATranslatedPhraseIsPresent() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.translated_phrase_field, DEFAULT_TRANSLATED_TEXT);
        click(activity, R.id.enter_translated_phrase_back_label);
        assertEquals(DEFAULT_TRANSLATED_TEXT, getFirstNewTranslationFromContext(activity).getTranslation().getTranslatedText());
    }

    @Test
    public void shouldPopulateTranslatedPhraseFieldWithValueWhenTranslationContextHasTranslatedText(){
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView translatedPhraseTextField = findTextView(activity, R.id.translated_phrase_field);
        assertEquals(getFirstNewTranslationFromContext(activity).getTranslation().getTranslatedText(), translatedPhraseTextField.getText().toString());
    }

    @Test
    public void shouldSetEnterTranslatedTextActivityTitleWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView summaryTitle = findTextView(activity, R.id.translated_phrase_title);
        assertEquals("Optional: add the translation", summaryTitle.getText().toString());
    }

    @Test
    public void shouldDisplayDescriptionWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView activityDescription = findTextView(activity, R.id.translated_phrase_activity_description);
        assertEquals("Add optional translation so you're able to show the card to people who may prefer to read.", activityDescription.getText().toString());
    }

    @Test
    public void shouldDisplayLanguageLabelWithCorrectLanguageWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView languageLabel = findTextView(activity, R.id.translated_phrase_input_language_label);
        assertEquals(String.format("%s TEXT", DEFAULT_DICTIONARY_LABEL.toUpperCase()), languageLabel.getText().toString());
    }

    @Test
    public void shouldDisplayNextLabelWhenInputIsEntered() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView nextLabel = findTextView(activity, R.id.recording_label_next_text);
        assertEquals("NEXT", nextLabel.getText().toString());
    }

    @Test
    public void shouldDisplaySkipLabelWhenInputIsRemoved() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView label = findTextView(activity, R.id.recording_label_next_text);
        TextView translatedText = findTextView(activity, R.id.translated_phrase_field);
        translatedText.setText("");
        assertEquals("SKIP", label.getText().toString());
    }

    @Test
    public void shouldDisplaySkipLabelWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView skipLabel = findTextView(activity,R.id.recording_label_next_text);
        assertEquals("SKIP", skipLabel.getText().toString());
    }

    @Test
    public void shouldIncludeSourcePhraseInHeaderWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTestWithNewTranslationContext();
        TextView sourceText = findTextView(activity, R.id.origin_translation_text);
        assertEquals(DEFAULT_SOURCE_PHRASE, sourceText.getText().toString());
    }

    @Test
    public void shouldDisplayLanguageTabsFragmentWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();

        assertNotNull(activity.findViewById(R.id.languages_scroll));
    }

    @Test
    public void shouldDisplayArabicLanguageAboveInputFieldWhenArabicLanguageTabSelected() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();
        clickLanguageTabAtPosition(activity, 1);

        assertEquals("ARABIC TEXT", findTextView(activity, R.id.translated_phrase_input_language_label).getText().toString());
    }

    @Test
    public void shouldUpdateTranslatedTextFieldWhenArabicLanguageTabSelected() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();
        clickLanguageTabAtPosition(activity, 1);

        assertEquals("Arabic Translation", findTextView(activity, R.id.translated_phrase_field).getText().toString());
    }

    @Test
    public void shouldSaveTranslatedPhraseToContextPhraseWhenNewLanguageTabIsSelected() {
        Activity activity = helper.createActivityToTestWithMultipleNewTranslationContexts();
        String translatedPhrase = "TranslatedPhrase";
        findTextView(activity, R.id.translated_phrase_field).setText(translatedPhrase);

        clickLanguageTabAtPosition(activity, 1);
        clickLanguageTabAtPosition(activity, 0);

        assertEquals(translatedPhrase, findTextView(activity, R.id.translated_phrase_field).getText().toString());
    }
}
