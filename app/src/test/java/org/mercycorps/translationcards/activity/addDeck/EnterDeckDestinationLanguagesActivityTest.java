package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.setText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckDestinationLanguagesActivityTest {

    private static final String A_LANGUAGE = "Arabic";
    private static final String EMPTY_LANGUAGE = "";
    private AddDeckActivityHelper<EnterDeckDestinationLanguagesActivity> helper;

    @Before
    public void setup() {
        helper = new AddDeckActivityHelper<>(EnterDeckDestinationLanguagesActivity.class);
    }

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldGoToMyDecksActivityWhenNextButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), A_LANGUAGE, false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.enter_destination_next_label);
        assertEquals(MyDecksActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToEnterTitleActivityWhenBackButtonIsClicked() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_destination_back_arrow);
        assertEquals(EnterDeckTitleActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldInflateActivityHeaderImageWhenCreated() {
        Activity activity = helper.createActivityToTest();
        ImageView header = findImageView(activity, R.id.enter_deck_destination_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(header.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSetActivityTitleWhenCreated() {
        Activity activity = helper.createActivityToTest();
        TextView title = findTextView(activity, R.id.enter_deck_destination_title);
        assertEquals("Set the destination language", title.getText().toString());
    }

    @Test
    public void shouldSetActivityDescriptionWhenCreated() {
        Activity activity = helper.createActivityToTest();
        TextView description = findTextView(activity, R.id.enter_deck_destination_description);
        assertEquals("A deck has one or more destination languages, these are the languages you would like to translate to. Separate each language by using a comma.", description.getText().toString());
    }

    @Test
    public void shouldDisplayCorrectHintTextWhenDestinationLanguageInputFieldIsEmpty() {
        Activity activity = helper.createActivityToTest();
        TextView input = findTextView(activity, R.id.enter_deck_destination_input);
        assertEquals("e.g. Arabic, Farsi, Pashto", input.getHint().toString());
    }

    @Test
    public void shouldUpdateNewDeckContextWhenUserClicksSaveWithLanguages() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        String languagesInput = "Arabic, Farsi";
        setText(activity, R.id.enter_deck_destination_input, languagesInput);
        click(activity, R.id.enter_destination_next_label);
        verify(newDeckContext).updateLanguagesInput(languagesInput);
    }

    @Test
    public void shouldSaveNewDeckContextWhenUserClicksSave() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        setText(activity, R.id.enter_deck_destination_input, "Arabic ");
        click(activity, R.id.enter_destination_next_label);
        verify(newDeckContext).save();
    }

    @Test
    public void shouldUpdateNewDeckContextWhenUserClicksBack() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        setText(activity, R.id.enter_deck_destination_input, "Arabic, Farsi");
        click(activity, R.id.enter_destination_back_arrow);
        verify(newDeckContext).updateLanguagesInput("Arabic, Farsi");
    }

    @Test
    public void shouldChangeNextButtonColorWhenDestinationLanguageIsNotEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.enter_deck_destination_input, A_LANGUAGE);
        TextView nextButtonText = findTextView(activity, R.id.enter_destination_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenDestinationLanguageIsNotEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.enter_deck_destination_input, A_LANGUAGE);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_destination_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeNextButtonColorWhenDestinationLanguageIsSetEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.enter_deck_destination_input, A_LANGUAGE);
        setText(activity, R.id.enter_deck_destination_input, EMPTY_LANGUAGE);
        TextView nextButtonText = findTextView(activity, R.id.enter_destination_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorToDisabledWhenDestinationLanguageIsSetEmpty() {
        Activity activity = helper.createActivityToTest();
        setText(activity, R.id.enter_deck_destination_input, A_LANGUAGE);
        setText(activity, R.id.enter_deck_destination_input, EMPTY_LANGUAGE);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_destination_next_image);
        assertEquals(R.drawable.forward_arrow_40p, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldFillDestinationLanguageFieldWhenActivityIsCreatedWithLanguages() {
        NewDeckContext newDeckContext = new NewDeckContext(null, "Arabic", false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView destinationLanguageField = findTextView(activity, R.id.enter_deck_destination_input);
        assertEquals("Arabic", destinationLanguageField.getText().toString());
    }

    @Test
    public void shouldNotBeAbleToClickSaveWhenNoDestinationLanguagesHaveBeenEntered() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_destination_next_label);
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void shouldUpdateDeckInDatabaseWhenDeckIsSavedAfterEditing() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        when(newDeckContext.getIsEditFlag()).thenReturn(true);
        when(newDeckContext.getLanguagesInput()).thenReturn("arabic");

        Activity activity = helper.createActivityToTestWithContext(newDeckContext);

        click(activity, R.id.enter_destination_next_label);

        verify(newDeckContext).update();
    }
}
