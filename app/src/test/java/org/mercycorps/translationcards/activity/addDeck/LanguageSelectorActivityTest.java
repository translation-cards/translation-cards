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
import org.mercycorps.translationcards.TestBaseComponent;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.util.ActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;

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
}