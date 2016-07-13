package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findView;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class ReviewAndSaveActivityTest {

    private static final String DECK_TITLE = "Deck Title";
    private static final String DECK_AUTHOR = "Author";
    private static final String EXTERNAL_ID = "0";
    private static final long DB_ID = 0L;
    private static final long CREATION_TIMESTAMP = 753004800000L;
    private static final String SOURCE_LANGUAGE_ISO = "en";
    private final AddDeckActivityHelper<ReviewAndSaveActivity> helper = new AddDeckActivityHelper<>(ReviewAndSaveActivity.class);
    private DeckService deckService = ((TestMainApplication) RuntimeEnvironment.application).getDeckService();
    private NewDeckContext newDeckContext;
    private Deck deck;

    @Before
    public void setup() {
        DictionaryRepository dictionaryRepository = ((TestMainApplication) RuntimeEnvironment.application).getDictionaryRepository();
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{});
        deck = new Deck(DECK_TITLE, DECK_AUTHOR, EXTERNAL_ID, DB_ID, CREATION_TIMESTAMP, false, SOURCE_LANGUAGE_ISO);
        newDeckContext = new NewDeckContext(deck, false);
    }

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldGoToAuthorAndLockActivityWhenBackButtonClicked() {
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.deck_review_and_save_back);
        assertEquals(EnterAuthorActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldGoToMyDecksActivityWhenSaveButtonClicked() {
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
        newDeckContext.addDestinationLanguage("Arabic");
        newDeckContext.addDestinationLanguage("Chinese");
        newDeckContext.addDestinationLanguage("Spanish");
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        click(activity, R.id.deck_review_and_save_button);
        verify(deckService).save(deck, newDeckContext.getDestinationLanguages());
    }

    @Test
    public void shouldShowDeckTitleFromContextWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView deckName = findTextView(activity, R.id.deck_name);
        assertEquals(DECK_TITLE, deckName.getText().toString());
    }

    @Test
    public void shouldShowDeckInformationFromContextWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView deckInformation = findTextView(activity, R.id.deck_information);
        assertEquals("Author, 11/11/93", deckInformation.getText().toString());
    }

    @Test
    public void shouldLockIconVisibilityWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        FrameLayout lockIcon = (FrameLayout) findView(activity, R.id.lock_icon);
        assertEquals(View.GONE, lockIcon.getVisibility());
    }

    @Test
    public void shouldShowLanguagesListInAlphabeticalOrderFromContextWhenActivityIsCreated() {
        newDeckContext.addDestinationLanguage("French");
        newDeckContext.addDestinationLanguage("German");
        newDeckContext.addDestinationLanguage("Chinese");
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        TextView translationLanguages = findTextView(activity, R.id.translation_languages);
        assertEquals("CHINESE  FRENCH  GERMAN", translationLanguages.getText().toString());
    }
}