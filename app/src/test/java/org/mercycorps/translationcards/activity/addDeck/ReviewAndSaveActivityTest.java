package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class ReviewAndSaveActivityTest {

    AddDeckActivityHelper<ReviewAndSaveActivity> helper = new AddDeckActivityHelper<>(ReviewAndSaveActivity.class);
    private DeckService deckService = ((TestMainApplication) RuntimeEnvironment.application).getDeckService();

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
        Deck deck = mock(Deck.class);
        when(newDeckContext.getDeck()).thenReturn(deck);
        String languages = "languages, languag";
        when(newDeckContext.getLanguagesInput()).thenReturn(languages);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.deck_review_and_save_button);
        verify(deckService).save(deck, languages);
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

    @Test
    public void shouldLockIconVisibilityWhenActivityIsCreated(){
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        when(newDeckContext.isDeckLocked()).thenReturn(false);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        FrameLayout lockIcon =(FrameLayout) findView(activity, R.id.lock_icon);
        assertEquals(lockIcon.getVisibility(),View.GONE);
    }

    @Test
    public void shouldShowLanguagesListFromContextWhenActivityIsCreated() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        when(newDeckContext.getLanguagesInput()).thenReturn("Arabic, Chinese, Spanish");
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView translationLanguages = findTextView(activity,R.id.translation_languages);
        assertEquals("ARABIC  CHINESE  SPANISH",translationLanguages.getText().toString());

    }
}