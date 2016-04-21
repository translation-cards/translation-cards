package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDefaultDeck;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by njimenez on 4/21/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckDestinationLanguagesActivityTest {

    @Test
    public void shouldGoToMyDecksActivityWhenNextButtonClicked() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        click(activity, R.id.enter_destination_next_label);
        assertEquals(MyDecksActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToEnterTitleActivityWhenBackButtonIsClicked() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        click(activity, R.id.enter_destination_back_arrow);
        assertEquals(EnterDeckTitleActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }
}
