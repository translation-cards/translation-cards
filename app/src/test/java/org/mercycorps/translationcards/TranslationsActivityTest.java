package org.mercycorps.translationcards;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationsActivityTest {

    private TranslationsActivity translationsActivity;

    @Before
    public void setUp() {
        translationsActivity = Robolectric.setupActivity(TranslationsActivity.class);
    }

    @Test
    public void initTabs_shouldShowLanguageTabsWhenOnHomeScreen() {
        LinearLayout tabContainer = (LinearLayout) translationsActivity.findViewById(R.id.tabs);

        assertThat(tabContainer.getChildCount(), is(3));

        List<String> languages = Arrays.asList("ARABIC", "FARSI", "PASHTO");
        int tabIndex = 0;
        for (String language : languages) {
            View languageTab = tabContainer.getChildAt(tabIndex);
            TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
            assertThat(languageTabText.getText().toString(), is(language));
            tabIndex++;
        }
    }

    @Test
    public void setDictionary_shouldNotHaveAnyTranslationCardsWhenNoneHaveBeenCreated() {
        TextView translationCardText = (TextView) translationsActivity.findViewById(R.id.card_text);

        assertThat(translationCardText, is(nullValue()));
    }
}