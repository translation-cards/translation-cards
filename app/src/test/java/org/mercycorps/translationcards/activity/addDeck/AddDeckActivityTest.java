package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.createActivityToTest;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class AddDeckActivityTest {
    @Ignore
    @Test
    public void shouldStartEnterDeckTitleActivityWhenGetStartedButtonIsClicked() {
        Activity activity = createActivityToTest(AddDeckActivity.class);

    }
}
