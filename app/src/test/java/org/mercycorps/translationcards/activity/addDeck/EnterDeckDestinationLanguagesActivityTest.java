package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowView;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckDestinationLanguagesActivityTest {

    private static final String A_LANGUAGE = "Arabic";
    private AddDeckActivityHelper<EnterDeckDestinationLanguagesActivity> helper = new AddDeckActivityHelper<>(EnterDeckDestinationLanguagesActivity.class);

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldGoToAuthorAndLockActivityWhenNextButtonClickedAndNewDeckContextHasADestinationLanguage() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        activity.updateNextButtonState();
        click(activity, R.id.enter_destination_next_label);
        assertEquals(EnterAuthorActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldAddLanguageSelectorResultToViewAndNewDeckContext() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexBox = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);
        Intent data = new Intent();
        data.putExtra(DestinationLanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "English");
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_OK, data);

        assertTrue(newDeckContext.getDestinationLanguages().contains("English"));
        assertEquals("English", ((TextView) flexBox.getChildAt(0)).getText().toString());
    }

    @Test
    public void shouldNotAddLanguageSelectorResultIfNull() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexBox = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);
        Intent emptyIntent = new Intent();
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_OK, emptyIntent);

        assertEquals(0, flexBox.getChildCount());
        assertEquals(0, newDeckContext.getDestinationLanguages().size());
    }

    @Test
    public void shouldNotAddLanguageSelectorResultIfInvalidResultCode() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexBox = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);

        Intent data = new Intent();
        data.putExtra(DestinationLanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "English");
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_CANCELED, data);

        assertEquals(0, flexBox.getChildCount());
        assertEquals(0, newDeckContext.getDestinationLanguages().size());
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
        String descriptionString = activity.getString(R.string.enter_deck_destination_language_description);
        assertEquals(descriptionString, description.getText().toString());
    }

    @Test
    public void shouldGoToDestinationLanguageSelectorActivity() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.add_language_chip);
        assertEquals(DestinationLanguageSelectorActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldChangeNextButtonColorWhenDestinationLanguageSetIsNotEmpty() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        activity.updateNextButtonState();
        TextView nextButtonText = findTextView(activity, R.id.enter_destination_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenDestinationLanguageIsNotEmpty() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        activity.updateNextButtonState();
        ImageView nextButtonImage = findImageView(activity, R.id.enter_destination_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldChangeNextButtonTextColorAndImageWhenDestinationLanguageIsSetEmpty() {
        Activity activity = helper.createActivityToTest();
        TextView nextButtonText = findTextView(activity, R.id.enter_destination_next_text);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_destination_next_image);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonText.getCurrentTextColor());
        assertEquals(R.drawable.forward_arrow_disabled, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldCreateLanguageChipsWhenActivityIsCreatedWithALanguage() {
        NewDeckContext newDeckContext = new NewDeckContext(null, false);
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);
        assertEquals(1, flexboxLayout.getChildCount());
        assertEquals(A_LANGUAGE, ((TextView) flexboxLayout.getChildAt(0)).getText().toString());
    }

    @Test
    public void shouldNotBeAbleToClickNextWhenNoDestinationLanguagesHaveBeenEntered() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_destination_next_label);
        assertNull(shadowOf(activity).getNextStartedActivity());
    }

    @Test
    public void shouldRemoveLanguageWhenChipIconTapped() {
        NewDeckContext newDeckContext = new NewDeckContext(null, false);
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);
        View languageChip = flexboxLayout.getChildAt(0);
        ShadowView shadowView = shadowOf(languageChip);
        View.OnTouchListener onTouchListener = shadowView.getOnTouchListener();
        int xTouchPos = languageChip.getRight() - (languageChip.getPaddingRight() / 2);
        int yTouchPos = (languageChip.getTop() - languageChip.getBottom()) / 2;
        onTouchListener.onTouch(languageChip, MotionEvent.obtain(500L, 500L, MotionEvent.ACTION_DOWN, xTouchPos, yTouchPos, 0));
        onTouchListener.onTouch(languageChip, MotionEvent.obtain(500L, 500L, MotionEvent.ACTION_UP, xTouchPos, yTouchPos, 0));

        assertTrue(newDeckContext.getDestinationLanguages().isEmpty());
        assertEquals(0, flexboxLayout.getChildCount());
    }

    @Test
    public void shouldDisableNextButtonWhenLastLanguageRemoved() {
        NewDeckContext newDeckContext = new NewDeckContext(null, false);
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);
        View languageChip = flexboxLayout.getChildAt(0);
        View.OnTouchListener onTouchListener = shadowOf(languageChip).getOnTouchListener();
        TextView nextButtonText = findTextView(activity, R.id.enter_destination_next_text);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_destination_next_image);

        int xTouchPos = languageChip.getRight() - (languageChip.getPaddingRight() / 2);
        int yTouchPos = (languageChip.getTop() - languageChip.getBottom()) / 2;
        onTouchListener.onTouch(languageChip, MotionEvent.obtain(500L, 500L, MotionEvent.ACTION_DOWN, xTouchPos, yTouchPos, 0));
        onTouchListener.onTouch(languageChip, MotionEvent.obtain(500L, 500L, MotionEvent.ACTION_UP, xTouchPos, yTouchPos, 0));
        click(activity, R.id.enter_destination_next_label);

        assertNull(shadowOf(activity).getNextStartedActivity());
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonText.getCurrentTextColor());
        assertEquals(R.drawable.forward_arrow_disabled, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }
}
