package org.mercycorps.translationcards.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.activity.AddDeckActivity;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RouterTest {

    private Router router;

    @Before
    public void setUp() throws Exception {
        router = new Router();
    }

    @Test
    public void shouldLaunchFeedbackActivity() {
        Activity activity = mock(Activity.class);

        router.launchFeedbackActivity(activity);

        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(activity).startActivity(argument.capture());
        assertEquals(Intent.ACTION_VIEW, argument.getValue().getAction());
    }

    @Test
    public void shouldLaunchImportActivity() {
        Uri data = mock(Uri.class);
        Activity activity = mock(Activity.class);

        router.launchImportActivityForResult(activity, data);

        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(activity).startActivityForResult(argument.capture(), eq(Router.REQUEST_CODE_IMPORT_FILE));
        assertEquals(data, argument.getValue().getData());
    }

    @Test
    public void shouldLaunchCreateDeckFlow() {
        Activity activity = mock(Activity.class);
        NewDeckContext newDeckContext = mock(NewDeckContext.class);

        router.launchCreateDeckFlow(activity, newDeckContext);

        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(activity).startActivityForResult(argument.capture(), eq(Router.REQUEST_CODE_CREATE_DECK));
        assertEquals(newDeckContext, argument.getValue().getParcelableExtra(AddDeckActivity.INTENT_KEY_DECK));
    }
}