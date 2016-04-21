package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.util.TestAddDeckActivityHelper;
import org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDefaultDeck;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.setText;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by njimenez on 4/12/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckTitleActivityTest {

    private static final String NO_TEXT = "";

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

    @Test
    public void shouldChangeNextButtonColorWhenDeckTitleIsNotEmpty() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        setText(activity, R.id.deck_title_input, TestAddDeckActivityHelper.DEFAULT_DECK_NAME);
        TextView nextButtonLabelText = findTextView(activity, R.id.enter_deck_title_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenDeckTitleIsNotEmpty() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        setText(activity, R.id.deck_title_input, TestAddDeckActivityHelper.DEFAULT_DECK_NAME);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_deck_title_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeNextButtonColorWhenDeckTitleIsSetEmpty() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        setText(activity, R.id.deck_title_input, TestAddDeckActivityHelper.DEFAULT_DECK_NAME);
        setText(activity, R.id.deck_title_input, NO_TEXT);
        TextView nextButtonLabelText = findTextView(activity, R.id.enter_deck_title_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorToDisabledWhenDeckTitleIsSetEmpty() {
        Activity activity = createActivityToTest(EnterDeckTitleActivity.class);
        setText(activity, R.id.deck_title_input, TestAddDeckActivityHelper.DEFAULT_DECK_NAME);
        setText(activity, R.id.deck_title_input, NO_TEXT);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_deck_title_next_image);
        assertEquals(R.drawable.forward_arrow_40p, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }
}