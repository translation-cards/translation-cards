package org.mercycorps.translationcards.refactor.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterSourcePhraseActivityTest {

    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String DEFAULT_TRANSLATION_TEXT = "Sleep here";
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";

    @Test
    public void shouldNotChangeNewTranslationContextWhenStartingActivity() {
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);
        NewTranslationContext newTranslationContext = (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(dict, newTranslationContext.getDictionary());
    }

    @Test
    public void shouldStartRecordAudioActivityWhenUserClicksNext() {
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);

        setSourceTextAndClick(activity);

        assertEquals(RecordAudioActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldUpdateNewTranslationContextWhenUserClicksNextWithValidSourceText() {
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);

        setSourceTextAndClick(activity);

        NewTranslationContext newTranslationContext = (NewTranslationContext) shadowOf(activity).getNextStartedActivity().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(DEFAULT_TRANSLATION_TEXT, newTranslationContext.getTranslation().getLabel());
    }

    @Test
    public void shouldUpdateTranslationSourceTextToUserInputtedText() {
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);

        setSourceTextAndClick(activity);

        NewTranslationContext newTranslationContext = (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(DEFAULT_TRANSLATION_TEXT, newTranslationContext.getTranslation().getLabel());
    }

    @Test
    public void shouldNotStartNextActivityWhenThereIsNoSourceText() {
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);

        activity.findViewById(R.id.enter_translated_phrase_save_label).performClick();

        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void shouldNotUpdateNewTranslationContextWhenThereIsNoSourceText() {
        Dictionary dict = new Dictionary(DEFAULT_DICTIONARY_LABEL);
        Activity activity = createActivityToTest(dict);

        activity.findViewById(R.id.enter_translated_phrase_save_label).performClick();

        NewTranslationContext newTranslationContext = (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(null, newTranslationContext.getTranslation().getLabel());
    }

    private void setSourceTextAndClick(Activity activity) { //TODO CODE SMELL
        TextView textView = (TextView) activity.findViewById(R.id.source_phrase_field);
        textView.setText(DEFAULT_TRANSLATION_TEXT);
        activity.findViewById(R.id.enter_translated_phrase_save_label).performClick();
    }


    private Activity createActivityToTest(Dictionary dict) {
        NewTranslationContext context = new NewTranslationContext(dict);
        return createActivityToTest(context);
    }

    private Activity createActivityToTest(NewTranslationContext context) {
        Intent intent = new Intent();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(EnterSourcePhraseActivity.class).withIntent(intent).create().get();
    }


}
