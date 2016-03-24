package org.mercycorps.translationcards.refactor.activity;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.TranslationsActivity;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.DEFAULT_DICTIONARY_LABEL;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterSourcePhraseActivityTest {

    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_TRANSLATION_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";

    @Test
    public void shouldNotChangeNewTranslationContextWhenStartingActivity() {
        Dictionary dict = createDefaultDictionary();
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class, dict);
        NewTranslationContext newTranslationContext = (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(dict, newTranslationContext.getDictionary());
    }

    @Test
    public void shouldStartRecordAudioActivityWhenUserClicksNext() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        setSourceTextAndClick(activity);
        assertEquals(RecordAudioActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
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
        click(activity, R.id.enter_translated_phrase_save_label);
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void shouldNotUpdateNewTranslationContextWhenThereIsNoSourceText() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        click(activity, R.id.enter_translated_phrase_save_label);
        assertEquals(null, getContextFromIntent(activity).getTranslation().getLabel());
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        ImageView getStartedImage = findImageView(activity, R.id.enter_source_phrase_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldGoToTranslationsActivityWhenCancelButtonIsClicked() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class);
        click(activity, R.id.cancel_add_translation_activity);
        assertEquals(TranslationsActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldSetEnterSourcePhraseActivityTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterSourcePhraseActivity.class, createDefaultDictionary());
        TextView sourcePhraseActivityTitle = findTextView(activity, R.id.source_phrase_title); //TODO push text to string to helper
        assertEquals(String.format("Write your %s phrase", DEFAULT_DICTIONARY_LABEL), sourcePhraseActivityTitle.getText().toString());
    }

    @Test
    public void shouldPopulateSourcePhraseFieldWithValueWhenTranslationContextHasSourceText(){
        Activity activity = createActivityToTestWithTContextAndSourceText(EnterSourcePhraseActivity.class);
        TextView sourcePhraseField = findTextView(activity, R.id.source_phrase_field);
        assertEquals(DEFAULT_SOURCE_PHRASE, sourcePhraseField.getText().toString());
    }

    private void setSourceTextAndClick(Activity activity) {
        setText(activity, R.id.source_phrase_field, DEFAULT_TRANSLATION_TEXT);
        click(activity, R.id.enter_translated_phrase_save_label);
    }

}
