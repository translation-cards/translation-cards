package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAbsListView;

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
    public void shouldGoToAuthorAndLockActivityWhenNextButtonClickedAndAdapterHasLanguages() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        languagesAdapter.add(A_LANGUAGE);
        activity.updateNextButtonState();
        click(activity, R.id.enter_destination_next_label);
        assertEquals(EnterAuthorActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldAddLanguageSelectorResultToAdapter() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        Intent data = new Intent();
        data.putExtra(DestinationLanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "English");
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_OK, data);

        assertEquals("English", languagesAdapter.getItem(0));
    }

    @Test
    public void shouldNotAddLanguageSelectorResultIfNull() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        Intent emptyIntent = new Intent();
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_OK, emptyIntent);

        assertEquals(0, languagesAdapter.getCount());
    }

    @Test
    public void shouldNotAddLanguageSelectorResultIfInvalidResultCode() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        Intent data = new Intent();
        data.putExtra(DestinationLanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "English");
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_CANCELED, data);

        assertEquals(0, languagesAdapter.getCount());
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
    public void shouldDisplayLanguageChips() {
        Activity activity = helper.createActivityToTest();
        GridView chipGrid = (GridView) activity.findViewById(R.id.language_chip_grid);
        ShadowAbsListView gridShadow = shadowOf(chipGrid);
        DestinationLanguagesAdapter chipAdapter = (DestinationLanguagesAdapter) chipGrid.getAdapter();
        chipAdapter.add("French");
        chipAdapter.add("English");
        chipAdapter.add("Spanish");
        gridShadow.populateItems();
        int chipCount = chipGrid.getCount();
        if (chipCount > 0) {
            assertEquals(3, chipCount);
            TextView frenchChip = (TextView) chipGrid.getChildAt(0);
            assertEquals("French", frenchChip.getText().toString());
            TextView englishChip = (TextView) chipGrid.getChildAt(1);
            assertEquals("English", englishChip.getText().toString());
            TextView spanishChip = (TextView) chipGrid.getChildAt(2);
            assertEquals("Spanish", spanishChip.getText().toString());
        } else {
            Assert.fail("No language chips found in GridView");
        }
    }


    @Test
    public void shouldGoToDestinationLanguageSelectorActivity() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.add_language_chip);
        assertEquals(DestinationLanguageSelectorActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldAddSelectedLanguagesToDeckContextWhenNextButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        languagesAdapter.add(A_LANGUAGE);
        activity.updateNextButtonState();
        click(activity, R.id.enter_destination_next_label);
        assertEquals(1, newDeckContext.getDestinationLanguages().size());
        assertTrue(newDeckContext.getDestinationLanguages().contains(A_LANGUAGE));
    }

    @Test
    public void shouldUpdateNewDeckContextWhenUserClicksBack() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        languagesAdapter.add(A_LANGUAGE);
        click(activity, R.id.enter_destination_back_arrow);
        assertTrue(newDeckContext.getDestinationLanguages().contains(A_LANGUAGE));
    }

    @Test
    public void shouldChangeNextButtonColorWhenDestinationLanguageIsNotEmpty() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        languagesAdapter.add(A_LANGUAGE);
        activity.updateNextButtonState();
        TextView nextButtonText = findTextView(activity, R.id.enter_destination_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());
    }

    @Test
    public void shouldChangeNextButtonArrowColorWhenDestinationLanguageIsNotEmpty() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), false);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        languagesAdapter.add(A_LANGUAGE);
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
    public void shouldCreateLanguageChipsWhenActivityIsCreatedWithLanguages() {
        NewDeckContext newDeckContext = new NewDeckContext(null, false);
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        GridView chipGridView = (GridView) activity.findViewById(R.id.language_chip_grid);
        DestinationLanguagesAdapter languagesAdapter = (DestinationLanguagesAdapter) chipGridView.getAdapter();
        assertEquals(1, languagesAdapter.getCount());
        assertEquals(A_LANGUAGE, languagesAdapter.getItem(0));
    }

    @Test
    public void shouldNotBeAbleToClickSaveWhenNoDestinationLanguagesHaveBeenEntered() {
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.enter_destination_next_label);
        assertNull(shadowOf(activity).getNextStartedActivity());
    }
}
