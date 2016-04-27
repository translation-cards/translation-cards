package org.mercycorps.translationcards.fragment;

import android.widget.HorizontalScrollView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationTabsFragmentTest {

    private TranslationTabsFragment translationTabsFragment;

    @Before
    public void setUp() throws Exception {
        translationTabsFragment = new TranslationTabsFragment();
        startFragment(translationTabsFragment);
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(translationTabsFragment);
    }

    @Test
    public void shouldInflateFragmentWithLanguagesTab() {
        assertNotNull(translationTabsFragment.getView());
    }

    @Test
    public void shouldContainHorizontalScrollView() {
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView)translationTabsFragment.getView().findViewById(R.id.languages_scroll);
        assertNotNull(horizontalScrollView);
    }
}
