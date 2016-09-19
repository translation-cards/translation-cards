package org.mercycorps.translationcards.addDeck.activity;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.activity.EnterAuthorActivity;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterAuthorActivityTest {

    AddDeckActivityHelper<EnterAuthorActivity> helper = new AddDeckActivityHelper<>(EnterAuthorActivity.class);

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldHaveNextButtonClickableWhenThereIsAuthorText() {
        Activity activity = helper.createActivityToTestWithDefaultDeck();

        assertTrue(findLinearLayout(activity, R.id.deck_author_next_label).isClickable());

        TextView nextButtonLabelText = findTextView(activity, R.id.deck_author_next_text);
        assertEquals(getColor(activity, R.color.primaryTextColor), nextButtonLabelText.getCurrentTextColor());

        ImageView nextButtonImage = findImageView(activity, R.id.deck_author_next_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldNotHaveNextButtonClickableWhenThereIsNoAuthorText() {
        Activity activity = helper.createActivityToTest();

        assertFalse(findLinearLayout(activity, R.id.deck_author_next_label).isClickable());

        TextView nextButtonLabelText = findTextView(activity, R.id.deck_author_next_text);
        assertEquals(getColor(activity, R.color.textDisabled), nextButtonLabelText.getCurrentTextColor());

        ImageView nextButtonImage = findImageView(activity, R.id.deck_author_next_image);
        assertEquals(R.drawable.forward_arrow_disabled, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }
}