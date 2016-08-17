package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.util.AddTranslationActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getContextFromIntent;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GetStartedActivityTest {

    private static final String CONTEXT_INTENT_KEY = "AddNewTranslationContext";

    AddTranslationActivityHelper<GetStartedActivity> helper= new AddTranslationActivityHelper<>(GetStartedActivity.class);

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldNotChangeDictionaryWhenStartingActivity() {
        Activity activity = helper.createActivityToTest();
        AddNewTranslationContext newTranslation = getContextFromIntent(activity);
        assertEquals(helper.dictionary, newTranslation.getNewTranslations().get(0).getDictionary());
    }

    @Test
    public void shouldStartEnterSourcePhraseActivityWhenGetStartedButtonClicked() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.get_started_button);
        assertEquals(EnterSourcePhraseActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldPassNewTranslationContextWhenStartingEnterSourcePhraseActivity() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.get_started_button);
        assertEquals(getContextFromIntent(activity), shadowOf(activity).getNextStartedActivity().getParcelableExtra(CONTEXT_INTENT_KEY));
    }

    @Test
    public void shouldContainImageWhenLoaded() {
        Activity activity = helper.createActivityToTest();
        ImageView getStartedImage = findImageView(activity, R.id.get_started_image);
        assertEquals(R.drawable.get_started_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSetGetStartedActivityTitleWhenActivityStarts() throws Exception {
        Activity activity = helper.createActivityToTest();
        TextView getStartedTitle = findTextView(activity, R.id.get_started_title);
        assertEquals("Card Maker", getStartedTitle.getText().toString());
    }

    @Test
    public void shouldLaunchTranslationsActivityWhenGetStartedBackButtonIsClicked() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.get_started_back);
        assertEquals(TranslationsActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldDisplayDescriptionWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView getStartedDescription = findTextView(activity, R.id.get_started_detail);
        assertEquals("Write your script, record your phrase and use your flashcard in the field.", getStartedDescription.getText().toString());
    }

    @Test
    public void shouldDisplayDescriptionTitleWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        TextView getStartedDescriptionTitle = findTextView(activity, R.id.get_started_decription_title);
        assertEquals("Make your own card", getStartedDescriptionTitle.getText().toString());
    }
}
