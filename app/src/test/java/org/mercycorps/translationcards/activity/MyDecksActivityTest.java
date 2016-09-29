package org.mercycorps.translationcards.activity;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.activity.GetStartedDeckActivity;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.util.MyDecksActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDecksActivityTest {
    public static final String URI = "https://docs.google.com/forms/d" +
            "/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/viewform?entry.1158658650=1.1.0";
    private static final String DEFAULT_LANGUAGE_NAME = "English";
    private MyDecksActivityHelper<MyDecksActivity> helper = new MyDecksActivityHelper<>(MyDecksActivity.class);

    @Inject DeckRepository deckRepository;

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
    public void testDecksActivityCreation(){
        setUpDeckRepositoryWithNoDecks();
        Activity activity = helper.createActivityToTest();
        assertNotNull(activity);
    }
    
    @Test
    public void shouldShowDecksHeader() {
        setUpDeckRepositoryWithDecks();
        Activity activity = helper.createActivityToTest();
        LinearLayout linearLayout = findLinearLayout(activity, R.id.my_decks_header);
        assertEquals(View.VISIBLE, linearLayout.getVisibility());
    }

    @Test
    public void shouldLaunchFeedbackFormWhenFeedbackButtonIsClicked() throws Exception {
        setUpDeckRepositoryWithDecks();
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.feedback_button);
        assertEquals(URI, shadowOf(activity).getNextStartedActivity().getData().toString());
    }

    @Test
    public void shouldLaunchImportDeckWhenImportButtonIsClicked(){
        setUpDeckRepositoryWithNoDecks();
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.import_deck_button);
        assertEquals("file/*", shadowOf(activity).getNextStartedActivity().getType());
    }

    @Test
    public void shouldLaunchCreateDeckFlowWhenCreateButtonIsClicked(){
        setUpDeckRepositoryWithNoDecks();
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.create_deck_button);
        assertEquals(GetStartedDeckActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldRefreshDecksOnActivityResume() throws Exception {
        Deck firstDeck = new Deck("First Deck", "", "", 0L, false, DEFAULT_LANGUAGE_NAME);
        Deck secondDeck = new Deck("Second Deck", "", "", 1L, false, DEFAULT_LANGUAGE_NAME);
        when(deckRepository.getAllDecks())
                .thenReturn(new Deck[]{firstDeck})
                .thenReturn(new Deck[]{firstDeck, secondDeck});
        Activity activity = helper.createActivityToTest();
        ShadowActivity shadowActivity = shadowOf(activity);
        ListView decksView = (ListView) activity.findViewById(R.id.my_decks_list);

        //Note that the header and footer in the list view contribute to the adapter item count
        //http://stackoverflow.com/a/11106567
        assertEquals(3, decksView.getAdapter().getCount());
        shadowActivity.pauseAndThenResume();

        assertEquals(4, decksView.getAdapter().getCount());
    }

    private void setUpDeckRepositoryWithDecks(){
        setUpDeckRepository(true);
    }

    private void setUpDeckRepositoryWithNoDecks(){
        setUpDeckRepository(false);
    }

    private void setUpDeckRepository(boolean shouldCreateDeck){
        when(deckRepository.getAllDecks()).thenReturn(createStubDeckArray(shouldCreateDeck));
    }

    private Deck[] createStubDeckArray(boolean shouldCreateDeck){
        if(!shouldCreateDeck) return new Deck[0];
        Deck[] arrayOfDecks = new Deck[1];
        Deck deck = new Deck("", "", "", 0L, false, DEFAULT_LANGUAGE_NAME);
        arrayOfDecks[0] = deck;
        return arrayOfDecks;
    }


}
