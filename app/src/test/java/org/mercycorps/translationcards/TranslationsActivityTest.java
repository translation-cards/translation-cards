package org.mercycorps.translationcards;

import android.content.Intent;
import android.support.v7.widget.Toolbar;
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

    public static final int DEFAULT_DECK_ID = 1;
    public static final String DEFAULT_DECK_NAME = "Default";
    public static final String NO_VALUE = "";
    private TranslationsActivity translationsActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, DEFAULT_DECK_ID, NO_VALUE);
        intent.putExtra("Deck", deck);
        translationsActivity = Robolectric.buildActivity(TranslationsActivity.class).withIntent(intent).create().get();
    }

    @Test
    public void shouldShowDeckNameInToolbar() throws Exception {
        Toolbar toolbar = (Toolbar) translationsActivity.findViewById(R.id.translation_cards_toolbar);
        assertThat(toolbar.getTitle().toString(), is("Default"));
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