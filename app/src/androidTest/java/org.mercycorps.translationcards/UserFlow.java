package org.mercycorps.translationcards;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.activity.DecksActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserFlow {

    public static final String ORIGIN_TEXT = "Where does it hurt?";
    public static final String TRANSLATION_TEXT = "حيث أنها لا تؤذي؟";

    @Rule
    public ActivityTestRule<DecksActivity> activityTestRule = new ActivityTestRule<>(DecksActivity.class);

    @Test
    public void createAndDeleteCardFlow() throws InterruptedException {

        onView(withText("Default")).perform(click());

        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.get_started_button)).perform(click());

        onView(withText("Flashcard phrase")).perform(typeText(ORIGIN_TEXT));
        closeSoftKeyboard();
        onView(withText("Phrase translation (Optional)")).perform(replaceText(TRANSLATION_TEXT));
        onView(withId(R.id.enter_source_phrase_next_label)).perform(click());

        onView(withId(R.id.record_audio_button)).perform(click());
        sleep(2000);
        onView(withId(R.id.record_audio_button)).perform(click());
        onView(withId(R.id.record_activity_next)).perform(click());

        onView(withId(R.id.origin_translation_text)).check(matches(withText(ORIGIN_TEXT)));
        onView(withId(R.id.translated_text)).check(matches(withText(TRANSLATION_TEXT)));
        onView(withId(R.id.recording_done)).perform(click());

        onView(withId(R.id.indicator_icon)).check(isRightOf(withText(ORIGIN_TEXT))).perform(click());
        onView(withText("Delete this flashcard")).perform(click());
        onView(withText("OK")).perform(click());
    }
}