package org.mercycorps.translationcards.activity.addDeck;

import android.support.v4.content.ContextCompat;
import android.view.View;
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

import static org.junit.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckTitleActivityTest {

    private AddDeckActivityHelper<EnterDeckTitleActivity> helper = new AddDeckActivityHelper<>(EnterDeckTitleActivity.class);
    private EnterDeckTitleActivity activity;

    @After
    public void teardown() {
        helper.teardown();
    }

    @Before
    public void setUp() throws Exception {
        activity = (EnterDeckTitleActivity) helper.createActivityToTest();
    }

    @Test
    public void updateNextButton() {
        boolean buttonClickable = true;
        int buttonColor = R.color.primaryTextColor;
        int buttonArrow = R.drawable.forward_arrow;
        activity.updateNextButton(buttonClickable, buttonColor, buttonArrow);

        View nextButton = activity.findViewById(R.id.enter_deck_title_next_label);
        TextView nextButtonText = (TextView) activity.findViewById(R.id.enter_deck_title_next_text);
        ImageView nextButtonImage = findImageView(activity, R.id.enter_deck_title_next_image);

        assertEquals(buttonClickable, nextButton.isClickable());
        assertEquals(ContextCompat.getColor(activity, buttonColor), nextButtonText.getCurrentTextColor());
        assertEquals(buttonArrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void setDeckTitleText() {
        String deckTitleText = "Some Text!";
        activity.setDeckTitleText(deckTitleText);

        TextView deckTitleInput = findTextView(activity, R.id.deck_title_input);
        assertEquals(deckTitleText, deckTitleInput.getText().toString());
    }
}