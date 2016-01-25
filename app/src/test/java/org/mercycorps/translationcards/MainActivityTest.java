package org.mercycorps.translationcards;

import android.view.View;
import android.widget.Button;
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
public class MainActivityTest {

    private MainActivity mainActivity;

    @Before
    public void setUp() {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void initTabs_shouldShowLanguageTabsWhenOnHomeScreen() {
        LinearLayout tabContainer = (LinearLayout) mainActivity.findViewById(R.id.tabs);

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
    public void initFeedbackButton_shouldShowFeedbackButtonOnHomeScreen() {
        Button feedbackButton = (Button) mainActivity.findViewById(R.id.main_feedback_button);

        assertThat(feedbackButton.getText().toString(), is("GIVE FEEDBACK"));
    }

    @Test
    public void setDictionary_shouldNotHaveAnyTranslationCardsWhenNoneHaveBeenCreated() {
        TextView translationCardText = (TextView) mainActivity.findViewById(R.id.card_text);

        assertThat(translationCardText, is(nullValue()));
    }
}