package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.activity.addDeck.AddDeckActivity.INTENT_KEY_DECK;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class GetStartedDeckActivityTest {

    private AddDeckActivityHelper<GetStartedDeckActivity> helper = new AddDeckActivityHelper<>(GetStartedDeckActivity.class);;

    @After
    public void teardown() {
        helper.teardown();
    }


    @Test
    public void shouldPassDeckToNextActivity() {
        Activity activity = helper.createActivityToTestWithDefaultDeck();
        NewDeckContext expectedDeckContext = getContextFromIntent(activity);

        click(activity, R.id.deck_get_started_button);
        
        Intent nextStartedActivity = shadowOf(activity).getNextStartedActivity();
        NewDeckContext actualDeckContext = nextStartedActivity.getParcelableExtra(INTENT_KEY_DECK);
        assertEquals(expectedDeckContext, actualDeckContext);
    }

    public NewDeckContext getContextFromIntent(Activity activity) {
        return (NewDeckContext) activity.getIntent().getParcelableExtra(AddDeckActivity.INTENT_KEY_DECK);
    }
}
