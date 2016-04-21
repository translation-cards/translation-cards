package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;

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
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDeckContext;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GetStartedDeckActivityTest {
    @Test
    public void shouldStartEnterDeckTitleActivityWhenGetStartedButtonIsClicked() {
        Activity activity = createActivityToTest(GetStartedDeckActivity.class);
        click(activity, R.id.deck_get_started_button);
        assertEquals(EnterDeckTitleActivity.class.getName(),shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldPassDeckToNextActivity() {
        NewDeckContext expectedDeckContext = new NewDeckContext();
        Activity activity = createActivityToTestWithDeckContext(GetStartedDeckActivity.class, expectedDeckContext);
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
}
