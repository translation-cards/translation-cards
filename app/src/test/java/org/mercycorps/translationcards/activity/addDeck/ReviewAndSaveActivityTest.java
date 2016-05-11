package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddDeckActivityHelper.createActivityToTestWithContext;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class ReviewAndSaveActivityTest {

    @Test
    public void shouldGoToAuthorAndLockActivityWhenBackButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), "", false);
        Activity activity = createActivityToTestWithContext(ReviewAndSaveActivity.class, newDeckContext);
        click(activity, R.id.deck_review_and_save_back);
        assertEquals(AuthorAndLockActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToMyDecksActivityWhenSavekButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), "", false);
        Activity activity = createActivityToTestWithContext(ReviewAndSaveActivity.class, newDeckContext);
        click(activity, R.id.deck_review_and_save_button);
        assertEquals(MyDecksActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }


    @Test
    public void shouldInflateEnterSourceLanguageImageWhenActivityIsCreated() {
        Activity activity = createActivityToTest(ReviewAndSaveActivity.class);
        ImageView imageView = findImageView(activity, R.id.summary_image);
        assertEquals(R.drawable.summary_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }

}