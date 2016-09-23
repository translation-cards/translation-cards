package org.mercycorps.translationcards.addDeck.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;

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

import java.util.Arrays;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mercycorps.translationcards.addDeck.activity.LanguageSelectorActivity.CANCEL_BUTTON_ID;
import static org.mercycorps.translationcards.addDeck.activity.LanguageSelectorActivity.SELECTED_LANGUAGE_KEY;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LanguageSelectorActivityTest {

    @Inject LanguageService languageService;

    private Activity activity;

    @Before
    public void setup() {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);

        when(languageService.getLanguageNames()).thenReturn(Arrays.asList("Farsi", "Imaginary", "Persian"));
        ActivityHelper<LanguageSelectorActivity> helper = new ActivityHelper<>(LanguageSelectorActivity.class);
        activity = helper.getActivityWithIntent(new Intent());
    }

    @Test
    public void shouldAddLanguagesFromLanguageServiceToAdapter() throws Exception {
        RecyclerView languagesList = (RecyclerView) activity.findViewById(R.id.languages_list);

        assertEquals(languageService.getLanguageNames().size(), languagesList.getAdapter().getItemCount());
    }

    @Test
    public void shouldFilterLanguagesInLanguageAdapter() {
        RecyclerView languagesList = (RecyclerView) activity.findViewById(R.id.languages_list);

        LanguageSelectorActivity.LanguageSelectorAdapter adapter = (LanguageSelectorActivity.LanguageSelectorAdapter) languagesList.getAdapter();
        adapter.filter("Imag");

        assertEquals(1, languagesList.getAdapter().getItemCount());
    }

    @Test
    public void shouldReturnDefaultLanguageValueWhenListItemSelected() throws Exception {
        RecyclerView languagesList = (RecyclerView) activity.findViewById(R.id.languages_list);
        ShadowActivity shadowActivity = shadowOf(activity);

        // workaround robolectric recyclerView issue
        // http://stackoverflow.com/questions/36039575/how-do-i-unit-test-recyclerview-linearlayoutmanager-in-robolectric
        languagesList.measure(0,0);
        languagesList.layout(0,0,100,1000);

        languagesList.findViewHolderForAdapterPosition(0).itemView.performClick();

        Intent resultIntent = shadowActivity.getResultIntent();
        assertEquals(Activity.RESULT_OK, shadowActivity.getResultCode());
        assertEquals(resultIntent.getStringExtra(SELECTED_LANGUAGE_KEY), "Farsi");
    }

    @Test
    public void shouldReturnCancelResultCodeWhenUserClicksCancel() throws Exception {
        ShadowActivity shadowActivity = shadowOf(activity);

        activity.onOptionsItemSelected(new RoboMenuItem(CANCEL_BUTTON_ID));

        assertEquals(Activity.RESULT_CANCELED, shadowActivity.getResultCode());
        assertTrue(shadowActivity.getResultIntent().getStringExtra(SELECTED_LANGUAGE_KEY).isEmpty());
    }

    @Test
    public void shouldDisplayLanguagesFromLanguageServiceList() throws Exception {
        RecyclerView languagesList = (RecyclerView) activity.findViewById(R.id.languages_list);

        String languageDisplayName = ((LanguageSelectorActivity.LanguageSelectorAdapter) languagesList.getAdapter()).getItem(0);

        assertEquals("Farsi", languageDisplayName);
    }
}