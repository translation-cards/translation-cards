package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.DEFAULT_DECK_AUTHOR;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithContext;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithDefaultDeck;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.getContextFromIntent;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterAuthorActivityTest {

    @Test
    public void shouldGoToReviewAndSaveActivityWhenNextButtonClicked() {
        Activity activity= createActivityToTestWithDefaultDeck(EnterAuthorActivity.class);
        click(activity, R.id.deck_author_next_label);
        assertEquals(ReviewAndSaveActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToEnterDestinationLanguagesActivityWhenBackButtonClicked() {
        Activity activity = createActivityToTest(EnterAuthorActivity.class);
        click(activity, R.id.deck_author_back);
        assertEquals(EnterDeckDestinationLanguagesActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldInflateAuthorAndLockImageWhenActivityIsCreated() {
        Activity activity = createActivityToTest(EnterAuthorActivity.class);
        ImageView imageView = findImageView(activity, R.id.deck_author_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldShowHintTextInDeckAuthorInputField() {
        Activity activity = createActivityToTest(EnterAuthorActivity.class);
        TextView inputAuthorField = findTextView(activity, R.id.deck_author_input);
        assertEquals("Your name or organisation", inputAuthorField.getHint().toString());
    }

    @Test
    public void shouldNotHaveNextButtonClickableWhenThereIsNoAuthorText() {
        Activity activity = createActivityToTest(EnterAuthorActivity.class);
        assertFalse(findLinearLayout(activity, R.id.deck_author_next_label).isClickable());
    }

    @Test
    public void shouldPopulateAuthorInputFieldWithContext() {
        Activity activity = createActivityToTestWithDefaultDeck(EnterAuthorActivity.class);
        EditText enterAuthorTitle = findAnyView(activity, R.id.deck_author_input);
        assertEquals(TestAddDeckActivityHelper.DEFAULT_DECK_AUTHOR, enterAuthorTitle.getText().toString());
    }

    @Test
    public void shouldChangeNextButtonColorWhenAuthorTitleIsNotEmpty() {
        Activity activity = createActivityToTestWithDefaultDeck(EnterAuthorActivity.class);
        TextView nextButtonLabelText = findTextView(activity, R.id.deck_author_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonLabelText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenAuthorTitleIsNotEmpty() {
        Activity activity = createActivityToTestWithDefaultDeck(EnterAuthorActivity.class);
        ImageView nextButtonImage = findImageView(activity, R.id.deck_author_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldSaveAuthorToDeckContextWhenNextButtonIsPressed() {
        Activity activity = createActivityToTest(EnterAuthorActivity.class);
        EditText enterAuthor = findAnyView(activity, R.id.deck_author_input);
        enterAuthor.setText(TestAddDeckActivityHelper.NEW_AUTHOR);
        click(activity, R.id.deck_author_next_label);
        assertEquals(TestAddDeckActivityHelper.NEW_AUTHOR, getContextFromIntent(activity).getAuthor());
    }

    @Test
    public void shouldSaveAuthorToDeckContextWhenBackButtonIsPressed() {
        Activity activity = createActivityToTest(EnterAuthorActivity.class);
        EditText enterAuthor = findAnyView(activity, R.id.deck_author_input);
        enterAuthor.setText(TestAddDeckActivityHelper.NEW_AUTHOR);
        click(activity, R.id.deck_author_back);
        assertEquals(TestAddDeckActivityHelper.NEW_AUTHOR, getContextFromIntent(activity).getAuthor());
    }
}