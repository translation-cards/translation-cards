package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.setText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckTitleActivityTest {

    private static final String NO_TEXT = "";

    private AddDeckActivityHelper<EnterDeckTitleActivity> helper;

    @Before
    public void setup() {
        helper = new AddDeckActivityHelper<>(EnterDeckTitleActivity.class);
    }

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldReturnToGetStartedActivityWhenBackButtonIsClicked(){
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_deck_title_back);
        assertEquals(GetStartedDeckActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToDestinationLanguageActivityWhenNextButtonIsClicked() {
        Activity activity = helper.createActivityToTestWithDefaultDeck();
        click(activity, R.id.enter_deck_title_next_label);
        assertEquals(EnterDeckDestinationLanguagesActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldShowHintTextInDeckTitleInputField() {
        Activity activity = helper.createActivityToTest();
        TextView inputTitleField = findTextView(activity, R.id.deck_title_input);
        assertEquals("e.g. Lesvos Beach Arrivals", inputTitleField.getHint().toString());
    }

    @Test
    public void shouldNotHaveNextButtonClickableWhenThereIsNoDeckTitleText() {
        Activity activity = helper.createActivityToTest();
        assertFalse(findLinearLayout(activity, R.id.enter_deck_title_next_label).isClickable());
    }

    @Test
    public void shouldShowActivityTitleWhenCreated() {
        Activity activity = helper.createActivityToTest();
        TextView title = findTextView(activity, R.id.enter_deck_title);
        assertEquals("Enter the deck title", title.getText().toString());
    }

    @Test
    public void shouldInflateEnterDeckTitleImageWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        ImageView imageView = findImageView(activity, R.id.enter_deck_title_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldShowActivityDescriptionWhenCreated() {
        Activity activity = helper.createActivityToTest();
        TextView description = findTextView(activity, R.id.enter_deck_title_description);
        assertEquals("Let's begin by entering the deck title. A good translation card deck helps with translations in a specific scenario. The title should be short and descriptive.", description.getText().toString());
    }

    @Test
    public void shouldChangeNextButtonColorWhenDeckTitleIsNotEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.deck_title_input, helper.DEFAULT_DECK_NAME);
        TextView nextButtonLabelText = findTextView(activity, R.id.enter_deck_title_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenDeckTitleIsNotEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.deck_title_input, helper.DEFAULT_DECK_NAME);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_deck_title_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeNextButtonColorWhenDeckTitleIsSetEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.deck_title_input, helper.DEFAULT_DECK_NAME);
        setText(activity, R.id.deck_title_input, NO_TEXT);
        TextView nextButtonLabelText = findTextView(activity, R.id.enter_deck_title_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorToDisabledWhenDeckTitleIsSetEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.deck_title_input, helper.DEFAULT_DECK_NAME);
        setText(activity, R.id.deck_title_input, NO_TEXT);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_deck_title_next_image);
        assertEquals(R.drawable.forward_arrow_40p, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldSetDeckTitleWhenActivityIsCreatedWithExistingDeckTitle() {
        Activity activity = helper.createActivityToTestWithDefaultDeck();
        EditText enterDeckTitle = findAnyView(activity, R.id.deck_title_input);
        assertEquals(helper.DEFAULT_DECK_NAME, enterDeckTitle.getText().toString());
    }

    @Test
    public void shouldSaveDeckTitleToContextWhenNextButtonIsClicked() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        setText(activity, R.id.deck_title_input, helper.DEFAULT_DECK_NAME);
        click(activity, R.id.enter_deck_title_next_label);
        verify(newDeckContext).setDeckTitle(helper.DEFAULT_DECK_NAME);
    }

    @Test
    public void shouldSaveDeckTitleToContextWhenBackButtonIsClicked() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        setText(activity, R.id.deck_title_input, helper.DEFAULT_DECK_NAME);
        click(activity, R.id.enter_deck_title_back);
        verify(newDeckContext).setDeckTitle(helper.DEFAULT_DECK_NAME);
    }
}