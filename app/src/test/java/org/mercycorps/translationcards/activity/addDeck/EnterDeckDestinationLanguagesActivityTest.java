package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
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

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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
    public void shouldChangeNextButtonColorWhenDestinationLanguageSetIsNotEmpty() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck());
        newDeckContext.addDestinationLanguage(A_LANGUAGE);

        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);

        TextView nextButtonText = findTextView(activity, R.id.enter_destination_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonText.getCurrentTextColor());

        ImageView nextButtonImage = findImageView(activity, R.id.enter_destination_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldNotAddLanguageSelectorResultIfInvalidResultCode() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck());
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexBox = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);

        Intent data = new Intent();
        data.putExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "English");
        int requestCode = 0;
        activity.onActivityResult(requestCode, Activity.RESULT_CANCELED, data);

        assertEquals(0, flexBox.getChildCount());
        assertEquals(0, newDeckContext.getDestinationLanguages().size());
    }

    @Test
    public void shouldRemoveLanguageWhenChipIconTapped() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck());
        newDeckContext.addDestinationLanguage(A_LANGUAGE);
        EnterDeckDestinationLanguagesActivity activity = (EnterDeckDestinationLanguagesActivity) helper.createActivityToTestWithContext(newDeckContext);
        FlexboxLayout flexboxLayout = (FlexboxLayout) activity.findViewById(R.id.language_chip_flexbox);
        View languageChip = flexboxLayout.getChildAt(0);

        click(languageChip, R.id.delete_chip);

        assertTrue(newDeckContext.getDestinationLanguages().isEmpty());
        assertEquals(0, flexboxLayout.getChildCount());
    }
}
