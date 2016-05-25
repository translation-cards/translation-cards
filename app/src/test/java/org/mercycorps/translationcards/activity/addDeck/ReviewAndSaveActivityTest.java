package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.model.DeckBuilder;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
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
    public void shouldDisplayDeckLabel() {
        String deckName = "Ritsona Camp";
        Deck deck = new DeckBuilder().withLabel(deckName).value();
        NewDeckContext newDeckContext = new NewDeckContext(deck, "", false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView nameField = findTextView(activity, R.id.deck_name);
        assertEquals(deckName, nameField.getText().toString());
    }

    @Test
    public void shouldDisplayDeckAuthor() {
        String deckAuthor = "Vicky Volunteer";
        Deck deck = new DeckBuilder().withAuthor(deckAuthor).value();
        NewDeckContext newDeckContext = new NewDeckContext(deck, "", false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView infoField = findTextView(activity, R.id.deck_information);
        assertEquals(deckAuthor, infoField.getText().toString());
    }

    @Test
    public void shouldDisplayTranslationLanguages() {
        String deckLanguages = "Kurdish, Spanish";
        Deck deck = new DeckBuilder().value();
        NewDeckContext newDeckContext = new NewDeckContext(deck, deckLanguages, false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView languagesField = findTextView(activity, R.id.translation_languages);
        assertEquals(deckLanguages, languagesField.getText().toString());
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
        ImageView imageView = findImageView(activity, R.id.summary_image);
        assertEquals(R.drawable.summary_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSaveNewDeckContextWhenUserClicksSave() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.deck_review_and_save_button);
        verify(newDeckContext).save();
    }
}