package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class ReviewAndSaveActivityTest {

    AddDeckActivityHelper<ReviewAndSaveActivity> helper = new AddDeckActivityHelper<>(ReviewAndSaveActivity.class);

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldGoToAuthorAndLockActivityWhenBackButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), "", false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.deck_review_and_save_back);
        assertEquals(EnterAuthorActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToMyDecksActivityWhenSaveButtonClicked() {
        NewDeckContext newDeckContext = new NewDeckContext(new Deck(), "", false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.deck_review_and_save_button);
        assertEquals(MyDecksActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }


    @Test
    public void shouldInflateEnterSourceLanguageImageWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        ImageView imageView = findImageView(activity, R.id.enter_source_language_image);
        assertEquals(R.drawable.enter_source_language_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSaveNewDeckContextWhenUserClicksSave() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.deck_review_and_save_button);
        verify(newDeckContext).save();
    }

    @Test
    public void shouldShowDeckTitleFromContextWhenActivityIsCreated() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        when(newDeckContext.getDeckLabel()).thenReturn("Deck Title");
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView deckName = findTextView(activity,R.id.deck_name);
        assertEquals(deckName.getText().toString(),"Deck Title");
    }

    @Test
    public void shouldShowDeckInformationFromContextWhenActivityIsCreated() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        when(newDeckContext.getDeckInformation()).thenReturn("Author, 11/11/1993");
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView deckInformation = findTextView(activity,R.id.deck_information);
        assertEquals(deckInformation.getText().toString(),"Author, 11/11/1993");
    }
}