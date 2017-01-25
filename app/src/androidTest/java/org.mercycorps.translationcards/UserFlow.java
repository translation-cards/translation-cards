package org.mercycorps.translationcards;

import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.activity.MyDecksActivity;

import static android.support.test.espresso.Espresso.closeSoftKeyboard;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.PositionAssertions.isRightOf;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static java.lang.Thread.sleep;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserFlow {

    public static final String DECK_TITLE = "Common Phrases";
    public static final String DESTINATION_LANGUAGE = "Arabic";
    public static final String DECK_AUTHOR = "TW";
    public static final String ORIGIN_TEXT = "Do you understand this language?";
    public static final String TRANSLATION_TEXT = "حيث أنها لا تؤذي؟";

    @Rule
    public ActivityTestRule<MyDecksActivity> activityTestRule = new ActivityTestRule<>(MyDecksActivity.class);

    @Test
    public void addNewDeckCreateNewCardOpenExistingDeckAndPlayCard() throws InterruptedException {
        //ADD NEW DECK
        onView(withId(R.id.create_deck_button)).perform(click());

        onView(withId(R.id.deck_get_started_button)).perform(click());

        onView(withId(R.id.deck_title_input)).perform(typeText(DECK_TITLE));
        closeSoftKeyboard();
        onView(withId(R.id.enter_deck_title_next_label)).perform(click());

        onView(withId(R.id.deck_source_language_next_label)).perform(click());

        onView(withId(R.id.add_language_button)).perform(click());
        onView(withId(R.id.language_filter_field)).perform(typeText(DESTINATION_LANGUAGE));
        onView(withId(R.id.languages_list)).perform(click());
        onView(withId(R.id.enter_destination_next_label)).perform(click());

        onView(withId(R.id.deck_author_input)).perform(typeText(DECK_AUTHOR));
        closeSoftKeyboard();
        onView(withId(R.id.deck_author_next_label)).perform(click());

        onView(withId(R.id.deck_review_and_save_button)).perform(click());

        //CREATE AND DELETE NEW CARD
        onData(anything()).inAdapterView(withId(R.id.my_decks_list)).atPosition(1).perform(click());
        onView(withId(R.id.add_translation_button)).perform(click());

        onView(withId(R.id.get_started_button)).perform(click());

        onView(withId(R.id.source_phrase_field)).perform(typeText(ORIGIN_TEXT));
        closeSoftKeyboard();
        onView(withId(R.id.activity_enter_source_phrase_next_label)).perform(click());

        onView(withId(R.id.translated_phrase_field)).perform(replaceText(TRANSLATION_TEXT));
        closeSoftKeyboard();
        onView(withId(R.id.enter_translated_phrase_next_label)).perform(click());

        onView(withId(R.id.record_audio_button)).perform(click());
        sleep(2000);
        onView(withId(R.id.record_audio_button)).perform(click());

        onView(withId(R.id.record_activity_next)).perform(click());

        onView(withId(R.id.origin_translation_text)).check(matches(withText(ORIGIN_TEXT)));
        onView(withId(R.id.translated_text)).check(matches(withText(TRANSLATION_TEXT)));
        onView(withId(R.id.save_translation_button)).perform(click());
        onView(withId(R.id.indicator_icon)).check(isRightOf(withText(ORIGIN_TEXT))).perform(click());
        onView(withText("Delete this flashcard")).perform(click());
        onView(withText("OK")).perform(click());

        //PLAY CARD IN EXISTING DECK
        //onData(anything()).inAdapterView(withId(R.id.my_decks_list)).atPosition(2).perform(click());
        //onView(withText(ORIGIN_TEXT)).perform(click());
        //onView(withId(R.id.translation_card_progress_bar)).check(matches(isDisplayed())); NEED TO FIGURE OUT HOW TO VERIFY CARD HAS BEEN PLAYED

    }

    //TODO: Add test that deletes added deck
}