package org.mercycorps.translationcards.myDecks;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.myDecks.MyDecksActivity;
import org.mercycorps.translationcards.myDecks.Router;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.util.MyDecksActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDecksActivityTest {
    private static final String DEFAULT_LANGUAGE_NAME = "English";
    private MyDecksActivityHelper<MyDecksActivity> helper = new MyDecksActivityHelper<>(MyDecksActivity.class);

    @Inject DeckRepository deckRepository;
    @Inject
    Router router;

    @Before
    public void setup() {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);
    }
    
    @After 
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldShowDecksHeader() {
        setUpDeckRepositoryWithDecks();
        Activity activity = helper.createActivityToTest();
        LinearLayout linearLayout = findLinearLayout(activity, R.id.my_decks_header);
        assertEquals(View.VISIBLE, linearLayout.getVisibility());
    }

    private void setUpDeckRepositoryWithDecks(){
        setUpDeckRepository(true);
    }

    private void setUpDeckRepository(boolean shouldCreateDeck){
        when(deckRepository.getAllDecks()).thenReturn(createStubDeckArray(shouldCreateDeck));
    }

    private List<Deck> createStubDeckArray(boolean shouldCreateDeck){
        List<Deck> decks = new ArrayList<>();
        if(!shouldCreateDeck) return decks;
        Deck deck = new Deck("", "", "", 0L, false, DEFAULT_LANGUAGE_NAME);
        decks.add(deck);
        return decks;
    }


}
