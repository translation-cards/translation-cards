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
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.util.ActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LanguageSelectorActivityTest {

    private ActivityHelper<LanguageSelectorActivity> helper;
    private Activity activity;
    private MainApplication mainApplication;

    @Before
    public void setup() {
        helper = new ActivityHelper<>(LanguageSelectorActivity.class);
        activity = helper.getActivityWithIntent(new Intent());
        mainApplication = (MainApplication) activity.getApplication();
    }

    @Test
    public void shouldAddLanguagesFromLanguageServiceToAdapter() throws Exception {
        ListView languagesList = (ListView) activity.findViewById(R.id.languages_list);
        ListAdapter languagesListAdapter = languagesList.getAdapter();
        languagesListAdapter.getCount();
        LanguageService languageService = mainApplication.getLanguageService();
        assertEquals(languageService.getLanguageNames().size(), languagesListAdapter.getCount());
    }
}