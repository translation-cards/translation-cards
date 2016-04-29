package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.TranslationsActivity;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.createDefaultDictionary;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getContextFromIntent;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GetStartedActivityTest {

    private static final String CONTEXT_INTENT_KEY = "AddNewTranslationContext";

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void shouldNotChangeDictionaryWhenStartingActivity() {
        Dictionary dict = createDefaultDictionary();
        Activity activity = createActivityToTest(GetStartedActivity.class, dict);
        AddNewTranslationContext newTranslation = getContextFromIntent(activity);
        assertEquals(dict, newTranslation.getNewTranslations().get(0).getDictionary());
    }

    @Test
    public void shouldStartEnterSourcePhraseActivityWhenGetStartedButtonClicked() {
        Activity activity = createActivityToTest(GetStartedActivity.class);
        click(activity, R.id.get_started_button);
        assertEquals(EnterSourcePhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldPassNewTranslationContextWhenStartingEnterSourcePhraseActivity() {
        Activity activity = createActivityToTest(GetStartedActivity.class);
        click(activity, R.id.get_started_button);
        assertEquals(getContextFromIntent(activity), shadowOf(activity).getNextStartedActivity().getSerializableExtra(CONTEXT_INTENT_KEY));
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = createActivityToTest(GetStartedActivity.class);
        ImageView getStartedImage = findImageView(activity, R.id.get_started_image);
        assertEquals(R.drawable.get_started_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSetGetStartedActivityTitleWhenActivityStarts() throws Exception {
        Activity activity = createActivityToTest(GetStartedActivity.class);
        TextView getStartedTitle = findTextView(activity, R.id.get_started_title);
        assertEquals("Card Maker", getStartedTitle.getText().toString());
    }

    @Test
    public void shouldLaunchTranslationsActivityWhenGetStartedBackButtonIsClicked() {
        Activity activity = createActivityToTest(GetStartedActivity.class);
        click(activity, R.id.get_started_back);
        assertEquals(TranslationsActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldDisplayDescriptionWhenActivityIsCreated() {
        Activity activity = createActivityToTest(GetStartedActivity.class);
        TextView getStartedDescription = findTextView(activity, R.id.get_started_detail);
        assertEquals("Write your script, record your phrase and use your flashcard in the field.", getStartedDescription.getText().toString());
    }

    @Test
    public void shouldDisplayDescriptionTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(GetStartedActivity.class);
        TextView getStartedDescriptionTitle = findTextView(activity, R.id.get_started_decription_title);
        assertEquals("Make your own card", getStartedDescriptionTitle.getText().toString());
    }
}
