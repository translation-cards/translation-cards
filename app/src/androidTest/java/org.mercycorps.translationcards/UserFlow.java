package org.mercycorps.translationcards;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.activity.DecksActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;

@RunWith(AndroidJUnit4.class)
public class UserFlow {

    public static final String ORIGIN_TEXT = "Where does it hurt?";
    public static final String TRANSLATION_TEXT = "حيث أنها لا تؤذي؟";
    @Rule
    public ActivityTestRule<DecksActivity> activityTestRule = new ActivityTestRule<>(DecksActivity.class);

    @Test
    public void shouldFindTheDefaultDeck() throws InterruptedException {
        onView(withText("Default")).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        onView(withId(R.id.recording_instructions_start)).perform(click());
        onView(withText("Flashcard phrase")).perform(typeText(ORIGIN_TEXT));
        closeSoftKeyboard();
        onView(withText("Phrase translation (Optional)")).perform(replaceText(TRANSLATION_TEXT));
        onView(withId(R.id.recording_label_next)).perform(click());
        onView(withId(R.id.recording_audio_button_record)).perform(click());
        sleep(2000);
        onView(withId(R.id.recording_audio_button_record)).perform(click());
        onView(withId(R.id.recording_audio_save)).perform(click());
        onView(withText(ORIGIN_TEXT));
        onView(withText(TRANSLATION_TEXT));
        onView(withId(R.id.recording_done)).perform(click());
    }
}