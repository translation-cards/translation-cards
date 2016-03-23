package org.mercycorps.translationcards.refactor.activity;


import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.data.Translation;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        Activity activity = createActivityToTest();
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveValidTranslationContextOnStart(){
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);
        NewTranslationContext newTranslationContext = fetchTranslationContext(activity);
        assertEquals(dict, newTranslationContext.getDictionary());
    }

    @Test
    public void shouldHaveTranslatedTextPhraseTextViewInIntent(){ //Testing Butter Knife hookup
        Activity activity = createActivityToTest();
        TextView textView = (TextView) activity.findViewById(R.id.translated_phrase_field);
        assertNotNull(textView);
    }

    @Test
    public void shouldUpdateTranslationContextWithTranslatedTextWhenSaveButtonIsClicked(){
        Activity activity = createActivityToTest();
        TextView textView = (TextView) activity.findViewById(R.id.translated_phrase_field);
        textView.setText(DEFAULT_TRANSLATED_TEXT);
        activity.findViewById(R.id.enter_translated_phrase_save_label).performClick();
        NewTranslationContext newTranslationContext = fetchTranslationContext(activity);
        assertEquals(DEFAULT_TRANSLATED_TEXT, newTranslationContext.getTranslation().getTranslatedText());
    }

    @Test
    public void shouldUpdateTranslationContextWithEmptyStringWhenNoTranslationAddedAndSaveButtonIsClicked() {
        Activity activity = createActivityToTest();
        activity.findViewById(R.id.enter_translated_phrase_save_label).performClick();
        NewTranslationContext newTranslationContext = fetchTranslationContext(activity);
        assertEquals(EMPTY_STRING, newTranslationContext.getTranslation().getTranslatedText());
    }

    @Test
    public void shouldSaveTranslationContextWhenUserClicksSave() {
        Activity activity = createActivityToTest(new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL), createTranslation(DEFAULT_TRANSLATION_LABEL)));
        DbManager dbManager = ((TestMainApplication) TestMainApplication.getContextFromMainApp()).getDbManager();
        activity.findViewById(R.id.enter_translated_phrase_save_label).performClick();
        verify(dbManager).saveTranslationContext(any(NewTranslationContext.class));
    }

    @Test
    public void shouldStartSummaryActivityWhenUserClicksSave() {
        Activity activity = createActivityToTest();
        activity.findViewById(R.id.enter_translated_phrase_save_label).performClick();
        assertEquals(SummaryActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    private Translation createTranslation(String defaultTranslationLabel) {
        Translation translation = new Translation();
        translation.setLabel(defaultTranslationLabel);
        return translation;
    }

    private NewTranslationContext fetchTranslationContext(Activity activity) {
        return (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
    }

    private Activity createActivityToTest(Dictionary dict) {
        NewTranslationContext context = new NewTranslationContext(dict);
        return createActivityToTest(context);
    }

    private Activity createActivityToTest(NewTranslationContext context) {
        Intent intent = new Intent();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(EnterTranslatedPhraseActivity.class).withIntent(intent).create().get();
    }

    private Activity createActivityToTest() {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(EnterTranslatedPhraseActivity.class).withIntent(intent).create().get();
    }

}
