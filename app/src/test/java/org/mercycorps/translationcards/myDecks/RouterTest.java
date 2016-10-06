package org.mercycorps.translationcards.myDecks;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.activity.AddDeckActivity;
import org.mercycorps.translationcards.myDecks.Router;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


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

    @Test
    public void shouldLaunchFilePickerForSamsungPhone() {
        Activity activity = mock(Activity.class, RETURNS_DEEP_STUBS);
        Intent samsungIntent = getSamsungIntent();
        when(activity.getPackageManager().resolveActivity(samsungIntent, 0)).thenReturn(mock(ResolveInfo.class));

        router.launchFilePicker(activity);

        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(activity).startActivityForResult(argument.capture(), eq(Router.REQUEST_CODE_IMPORT_FILE_PICKER));
        assertEquals("com.sec.android.app.myfiles.PICK_DATA", argument.getValue().getAction());
    }

    @Test
    public void shouldLaunchFilePickerForNonSamsungPhones() {
        Activity activity = mock(Activity.class, RETURNS_DEEP_STUBS);
        Intent samsungIntent = getSamsungIntent();
        when(activity.getPackageManager().resolveActivity(samsungIntent, 0)).thenReturn(null);

        router.launchFilePicker(activity);

        ArgumentCaptor<Intent> argument = ArgumentCaptor.forClass(Intent.class);
        verify(activity).startActivityForResult(argument.capture(), eq(Router.REQUEST_CODE_IMPORT_FILE_PICKER));
        assertEquals(Intent.ACTION_GET_CONTENT, argument.getValue().getAction());
    }

    @NonNull
    private Intent getSamsungIntent() {
        Intent samsungIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        samsungIntent.addCategory(Intent.CATEGORY_DEFAULT);
        return samsungIntent;
    }
}