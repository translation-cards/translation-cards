package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.activity.addDeck.AddDeckActivity.INTENT_KEY_DECK;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDefaultDeck;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.getContextFromIntent;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GetStartedDeckActivityTest {
    @Test
    public void shouldStartEnterDeckTitleActivityWhenGetStartedButtonIsClicked() {
        Activity activity = createActivityToTest(GetStartedDeckActivity.class);
        click(activity, R.id.deck_get_started_button);
        assertEquals(EnterDeckTitleActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldPassDeckToNextActivity() {
        Activity activity = createActivityToTestWithDefaultDeck(GetStartedDeckActivity.class);
        NewDeckContext expectedDeckContext = getContextFromIntent(activity);
        click(activity, R.id.deck_get_started_button);
        Intent nextStartedActivity = shadowOf(activity).getNextStartedActivity();
        NewDeckContext actualDeckContext = (NewDeckContext) nextStartedActivity.getSerializableExtra(INTENT_KEY_DECK);
        assertEquals(expectedDeckContext, actualDeckContext);
    }

    @Test
    public void shouldReturnToMyDecksWhenBackArrowIsClicked() {
        Activity activity = createActivityToTest(GetStartedDeckActivity.class);
        click(activity, R.id.deck_get_started_back);
        assertEquals(MyDecksActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldInflateGetStartedImageWhenActivityIsCreated() {
        Activity activity = createActivityToTest(GetStartedDeckActivity.class);
        ImageView getStartedImage = findImageView(activity, R.id.deck_get_started_image);
        assertEquals(R.drawable.get_started_image, shadowOf(getStartedImage.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldDisplayGetStartedTitleWhenActivityIsCreated() {
        Activity activity = createActivityToTest(GetStartedDeckActivity.class);
        TextView title = findTextView(activity, R.id.deck_get_started_header);
        assertEquals("Make your own deck", title.getText().toString());
    }

    @Test
    public void shouldDisplayGetStartedDescriptionWhenActivityIsCreated() {
        Activity activity = createActivityToTest(GetStartedDeckActivity.class);
        TextView description = findTextView(activity, R.id.deck_get_started_detail);
        assertEquals("Creating a new deck allows you to add\\ncustom translation cards.", description.getText().toString());

    }
}
