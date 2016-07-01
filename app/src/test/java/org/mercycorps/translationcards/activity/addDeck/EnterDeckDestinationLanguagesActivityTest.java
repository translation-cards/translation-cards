package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashSet;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.setText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckDestinationLanguagesActivityTest {

    private static final String A_LANGUAGE = "Arabic";
    private static final String EMPTY_LANGUAGE = "";
    private AddDeckActivityHelper<EnterDeckDestinationLanguagesActivity> helper = new AddDeckActivityHelper<>(EnterDeckDestinationLanguagesActivity.class);

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldGoToAuthorAndLockActivityWhenNextButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), A_LANGUAGE, false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.enter_destination_next_label);
        assertEquals(EnterAuthorActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToEnterDeckSourceLanguageActivityWhenBackButtonIsClicked() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_destination_back_arrow);
        assertEquals(EnterDeckSourceLanguageActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
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
        assertEquals(R.drawable.forward_arrow_disabled, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
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

    //TODO: Delete test
    @Test
    public void shouldAddLanguageFromTextFieldToSelectedLanguageList() {
        NewDeckContext newDeckContext = new NewDeckContext(null, "", false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        AutoCompleteTextView destinationLanguageInput = (AutoCompleteTextView) findView(activity, R.id.enter_deck_destination_input);
        setText(activity, R.id.enter_deck_destination_input, "Arabic");
        destinationLanguageInput.onEditorAction(EditorInfo.IME_ACTION_DONE);

        assertEquals(1, newDeckContext.getDestinationLanguages().size());
        assertTrue(newDeckContext.getDestinationLanguages().contains("Arabic"));
    }

    @Test
    public void shouldAddLanguageOnActivityResult() {
        NewDeckContext newDeckContext = new NewDeckContext(null, "", false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity)helper.createActivityToTestWithContext(newDeckContext);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(DestinationLanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "Some Language");

        activity.showLanguageSelector();
        Intent launchSelectorIntent = shadowOf(activity).getNextStartedActivity();
        shadowOf(activity).receiveResult(launchSelectorIntent, Activity.RESULT_OK, resultIntent);

        assertTrue(newDeckContext.getDestinationLanguages().contains("Some Language"));
    }

    //TODO: refactor test
    @Test
    public void shouldNotAddLanguageIfAlreadyPresent() {
        NewDeckContext newDeckContext = new NewDeckContext(null, "", false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        AutoCompleteTextView destinationLanguageInput = (AutoCompleteTextView) findView(activity, R.id.enter_deck_destination_input);
        setText(activity, R.id.enter_deck_destination_input, "Arabic");
        destinationLanguageInput.onEditorAction(EditorInfo.IME_ACTION_DONE);
        setText(activity, R.id.enter_deck_destination_input, "Arabic");
        destinationLanguageInput.onEditorAction(EditorInfo.IME_ACTION_DONE);

        HashSet<String> destinationLanguages = newDeckContext.getDestinationLanguages();
        assertEquals(1, destinationLanguages.size());
        assertTrue(destinationLanguages.contains("Arabic"));
    }
}
