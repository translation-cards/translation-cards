package org.mercycorps.translationcards.activity;

import android.app.Activity;
import android.content.Intent;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RouterTest {
    @Test
    public void shouldLaunchFeedbackActivity() {
        Router router = new Router();

        Activity activity = mock(Activity.class);
        router.launchFeedbackActivity(activity);

        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(activity).startActivity(argument.capture());
        assertEquals(Intent.ACTION_VIEW, argument.getValue().getAction());
    }
}