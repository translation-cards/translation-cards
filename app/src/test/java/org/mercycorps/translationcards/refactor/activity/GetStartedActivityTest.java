package org.mercycorps.translationcards.refactor.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import org.junit.Before;
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
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GetStartedActivityTest {

    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    private static final Dictionary NO_DICTIONARY = null;

    @Before
    public void setUp() throws Exception {}

    @Test
    public void shouldNotChangeDictionaryWhenStartingActivity() {
        Dictionary dict = new Dictionary("Bla");
        NewTranslationContext context = createContext(dict);
        Activity activity = createActivityToTest(context);
        NewTranslationContext newTranslationContext = (NewTranslationContext) activity.getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        assertEquals(dict, newTranslationContext.getDictionary());
    }

    private NewTranslationContext createContext(Dictionary dict) {
        return new NewTranslationContext(dict);
    }


    @Test
    public void shouldStartEnterSourcePhraseActivityWhenGetStartedButtonClicked() {
        Activity activity = createActivityToTest(new NewTranslationContext(NO_DICTIONARY));
        View getStartedButton  = activity.findViewById(R.id.get_started_button);
        getStartedButton.performClick();
        assertEquals(EnterSourcePhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldPassNewTranslationContextWhenStartingEnterSourcePhraseActivty() {
        NewTranslationContext newTranslationContext = new NewTranslationContext(NO_DICTIONARY);
        Activity activity = createActivityToTest(newTranslationContext);

        activity.findViewById(R.id.get_started_button).performClick();

        assertEquals(newTranslationContext, shadowOf(activity).getNextStartedActivity().getSerializableExtra(CONTEXT_INTENT_KEY));
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = createActivityToTest(new NewTranslationContext(NO_DICTIONARY));
        ImageView getStartedImage = (ImageView) activity.findViewById(R.id.get_started_image);
        assertEquals(R.drawable.get_started_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }


    private Activity createActivityToTest(NewTranslationContext context) {
        Intent intent = new Intent();
        intent.putExtra(CONTEXT_INTENT_KEY, context);
        return Robolectric.buildActivity(GetStartedActivity.class).withIntent(intent).create().get();
    }


}
