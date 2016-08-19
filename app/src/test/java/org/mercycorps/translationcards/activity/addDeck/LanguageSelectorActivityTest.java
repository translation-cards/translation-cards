package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.util.ActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.fakes.RoboMenuItem;
import org.robolectric.shadows.ShadowActivity;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LanguageSelectorActivityTest {

    @Inject LanguageService languageService;

    private ActivityHelper<LanguageSelectorActivity> helper;
    private Activity activity;

    @Before
    public void setup() {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);

        when(languageService.getLanguageNames()).thenReturn(Arrays.asList("Farsi", "Imaginary", "Persian"));
        helper = new ActivityHelper<>(LanguageSelectorActivity.class);
        activity = helper.getActivityWithIntent(new Intent());
    }

    @Test
    public void shouldAddLanguagesFromLanguageServiceToAdapter() throws Exception {
        ListView languagesList = (ListView) activity.findViewById(R.id.languages_list);
        ListAdapter languagesListAdapter = languagesList.getAdapter();
        languagesListAdapter.getCount();
        assertEquals(languageService.getLanguageNames().size(), languagesListAdapter.getCount());
    }

    @Test
    public void shouldReturnDefaultLanguageValueWhenListItemSelected() throws Exception {
        ListView languagesList = (ListView) activity.findViewById(R.id.languages_list);
        ShadowActivity shadowActivity = shadowOf(activity);

        shadowOf(languagesList).performItemClick(0);

        Intent resultIntent = shadowActivity.getResultIntent();
        assertEquals(Activity.RESULT_OK, shadowActivity.getResultCode());
        assertEquals(resultIntent.getStringExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY), "Farsi");
    }

    @Test
    public void shouldReturnCancelResultCodeWhenUserClicksCancel() throws Exception {
        ShadowActivity shadowActivity = shadowOf(activity);

        activity.onOptionsItemSelected(new RoboMenuItem(LanguageSelectorActivity.CANCEL_BUTTON_ID));

        assertEquals(Activity.RESULT_CANCELED, shadowActivity.getResultCode());
        assertNull(shadowActivity.getResultIntent().getStringExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY));
    }

    @Test
    public void shouldDisplayLanguagesFromLanguageServiceList() throws Exception {
        ListView languagesList = (ListView) activity.findViewById(R.id.languages_list);

        String languageDisplayName = ((String)languagesList.getAdapter().getItem(0));

        assertEquals("Farsi", languageDisplayName);
    }
}