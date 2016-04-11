package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslationContext;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.activity.addDeck.AddDeckActivity.INTENT_KEY_DECK;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDeck;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
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
        Deck expectedDeck = new Deck();
        Activity activity = createActivityToTestWithDeck(GetStartedDeckActivity.class, expectedDeck);
        click(activity, R.id.deck_get_started_button);
        Intent nextStartedActivity = shadowOf(activity).getNextStartedActivity();
        Deck deck = (Deck) nextStartedActivity.getSerializableExtra(INTENT_KEY_DECK);
        assertEquals(expectedDeck, deck);
    }


}
