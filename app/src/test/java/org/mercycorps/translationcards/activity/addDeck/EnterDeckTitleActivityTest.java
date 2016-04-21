package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDefaultDeck;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by njimenez on 4/12/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckTitleActivityTest {

    @Test
    public void shouldReturnToGetStartedActivityWhenBackButtonIsClicked(){
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        click(activity, R.id.enter_title_back);
        assertEquals(GetStartedDeckActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToDestinationLanguageActivityWhenNextButtonIsClicked() {
        Activity activity = createActivityToTestWithDefaultDeck(EnterDeckTitleActivity.class);
        click(activity, R.id.enter_deck_title_next_label);
        assertEquals(EnterDeckDestinationLanguagesActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldShowHintTextInDeckTitleInputField() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        TextView inputTitleField = findTextView(activity, R.id.deck_title_input);
        assertEquals("e.g. Lesvos Beach Arrivals", inputTitleField.getHint().toString());
    }

    @Test
    public void shouldNotHaveNextButtonClickableWhenThereIsNoDeckTitleText() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        assertFalse(findLinearLayout(activity, R.id.enter_deck_title_next_label).isClickable());
    }

    @Test
    public void shouldShowActivityTitleWhenCreated() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        TextView title = findTextView(activity, R.id.enter_deck_title);
        assertEquals("Enter the deck title", title.getText().toString());
    }

    @Test
    public void shouldInflateEnterDeckTitleImageWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        ImageView imageView = findImageView(activity, R.id.enter_deck_title_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldShowActivityDescriptionWhenCreated() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        TextView description = findTextView(activity, R.id.enter_deck_title_description);
        assertEquals("Let's begin by entering the deck title. A good\\ntranslation card deck helps with translations\\nin a specific scenario. The title should be\\nshort and descriptive.", description.getText().toString());
    }
}