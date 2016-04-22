package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDefaultDeck;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by njimenez on 4/21/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckDestinationLanguagesActivityTest {

    @Test
    public void shouldGoToMyDecksActivityWhenNextButtonClicked() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        click(activity, R.id.enter_destination_next_label);
        assertEquals(MyDecksActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToEnterTitleActivityWhenBackButtonIsClicked() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        click(activity, R.id.enter_destination_back_arrow);
        assertEquals(EnterDeckTitleActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldInflateActivityHeaderImageWhenCreated() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        ImageView header = findImageView(activity, R.id.enter_deck_destination_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(header.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSetActivityTitleWhenCreated() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        TextView title = findTextView(activity, R.id.enter_deck_destination_title);
        assertEquals("Set the destination language", title.getText().toString());
    }

    @Test
    public void shouldSetActivityDescriptionWhenCreated() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        TextView description = findTextView(activity, R.id.enter_deck_destination_description);
        assertEquals("A deck has one or more destination\\nlanguages, these are the languages you\\nwould like to translate to. Separate each language by using a comma.", description.getText().toString());
    }

    @Test
    public void shouldDisplayCorrectHintTextWhenDestinationLanguageInputFieldIsEmpty() {
        Activity activity = createActivityToTest(EnterDeckDestinationLanguagesActivity.class);
        TextView input = findTextView(activity, R.id.enter_deck_destination_input);
        assertEquals("e.g. Arabic, Farsi, Pashto", input.getHint().toString());
    }
}
