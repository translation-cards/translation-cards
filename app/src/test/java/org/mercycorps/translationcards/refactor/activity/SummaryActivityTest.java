package org.mercycorps.translationcards.refactor.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.data.Translation;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class SummaryActivityTest {

    public static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    private static  final String DEFAULT_TRANSLATED_TEXT = "TranslatedText";
    private String DEFAULT_SOURCE_PHRASE = "SourcePhrase";
    ;

    @Test
    public void shouldNotBeNull(){
        Activity activity = createActivityToTest();
        assertNotNull(activity);
    }

    @Test
    public void shouldHaveValidTranslationContextOnStart(){ // TODO: 3/23/16 Does this even make sense to test this?
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);
        NewTranslationContext newTranslationContext = fetchTranslationContext(activity);
        assertEquals(dict, newTranslationContext.getDictionary());
    }

    @Test
    public void shouldStartTranslationActivityWhenUserClicksDone() {
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);
        activity.findViewById(R.id.summary_done).performClick();

    }

    @Test
    public void shouldShowTranslationSourcePhraseInCardView() {
        NewTranslationContext context = new NewTranslationContext(createDictionary(), createTranslation(DEFAULT_SOURCE_PHRASE));
        Activity activity = createActivityToTest(context);
        TextView sourceTextView = (TextView) activity.findViewById(R.id.origin_translation_text);
        assertEquals(DEFAULT_SOURCE_PHRASE, sourceTextView.getText().toString());
    }

    @Test
    public void shouldShowTranslatedPhraseInCardView() {
        NewTranslationContext context = new NewTranslationContext(createDictionary(), createTranslationWithTranslatedText(DEFAULT_TRANSLATED_TEXT));
        Activity activity = createActivityToTest(context);
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(DEFAULT_TRANSLATED_TEXT, translatedTextView.getText().toString());
    }

    @Test
    public void shouldShowHintTextWhenNoTranslatedTextPhraseIsProvided() {
        Activity activity = createActivityToTest(createDictionary());
        TextView translatedTextView = (TextView) activity.findViewById(R.id.translated_text);
        assertEquals(String.format("Add %s translation", DEFAULT_DICTIONARY_LABEL), translatedTextView.getHint());
    }

    @Test
    public void shouldMakeTranslationChildLinearLayoutVisibleWhenLayoutIsLoaded() {
        Activity activity = createActivityToTest();
        assertEquals(View.VISIBLE, activity.findViewById(R.id.translation_child).getVisibility());
    }

    private Dictionary createDictionary() {
        return new Dictionary(DEFAULT_DICTIONARY_LABEL);
    }

    private Translation createTranslation(String sourcePhrase) {
        Translation translation = new Translation();
        translation.setLabel(sourcePhrase);
        return translation;
    }

    // TODO: 3/23/16 Please refactor
    private Translation createTranslationWithTranslatedText(String translatedText){
        Translation translation = new Translation();
        translation.setTranslatedText(translatedText);
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
        return Robolectric.buildActivity(SummaryActivity.class).withIntent(intent).create().get();
    }

    private Activity createActivityToTest() {
        Intent intent = new Intent();
        NewTranslationContext context = new NewTranslationContext(new Dictionary(DEFAULT_DICTIONARY_LABEL));
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(SummaryActivity.class).withIntent(intent).create().get();
    }
}
