package org.mercycorps.translationcards.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationTabsFragmentTest {

    public static final String BUNDLE_KEY_DICTIONARIES = "Dictionaries";
    private TranslationTabsFragment translationTabsFragment;
    private Dictionary dictionary;

    @Before
    public void setUp() throws Exception {
        translationTabsFragment = new TranslationTabsFragment();
        Bundle bundle = new Bundle();
        ArrayList<Dictionary> arrayList = new ArrayList<>();
        dictionary = mock(Dictionary.class);
        when(dictionary.getLabel()).thenReturn("arabic");
        arrayList.add(dictionary);
        bundle.putSerializable(BUNDLE_KEY_DICTIONARIES, arrayList);
        translationTabsFragment.setArguments(bundle);
        startFragment(translationTabsFragment);
    }

    @Test
    public void shouldNotBeNull() {
        assertNotNull(translationTabsFragment);
    }

    @Test
    public void shouldInflateFragmentWithLanguagesTab() {
        assertNotNull(getFragmentView());
    }

    @Test
    public void shouldContainHorizontalScrollView() {
        HorizontalScrollView horizontalScrollView = (HorizontalScrollView) getFragmentView().findViewById(R.id.languages_scroll);
        assertNotNull(horizontalScrollView);
    }

    @Test
    public void shouldContainArabicInLanguageTabWhenNewTranslationContextLanguageIsArabic() {
        LinearLayout linearLayout = (LinearLayout) getFragmentView().findViewById(R.id.languages_scroll_list);

        assertEquals("ARABIC", ((TextView)linearLayout.getChildAt(0).findViewById(R.id.tab_label_text)).getText());
    }



    private View getFragmentView() {
        return translationTabsFragment.getView();
    }
}
