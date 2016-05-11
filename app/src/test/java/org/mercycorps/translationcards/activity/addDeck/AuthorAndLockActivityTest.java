package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.util.ActivityHelper;
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
public class AuthorAndLockActivityTest  {

    @Test
    public void shouldGoToReviewAndSaveActivityWhenNextButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), "", false);
        Activity activity = createActivityToTestWithContext(AuthorAndLockActivity.class, newDeckContext);
        click(activity, R.id.deck_author_and_lock_next_label);
        assertEquals(ReviewAndSaveActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToEnterDestinationLanguagesActivityWhenBackButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), "", false);
        Activity activity = createActivityToTestWithContext(AuthorAndLockActivity.class, newDeckContext);
        click(activity, R.id.deck_author_and_lock_back);
        assertEquals(EnterDeckDestinationLanguagesActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldInflateAuthorAndLockImageWhenActivityIsCreated() {
        Activity activity = createActivityToTest(AuthorAndLockActivity.class);
        ImageView imageView = findImageView(activity, R.id.deck_author_and_lock_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }
}