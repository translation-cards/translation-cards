package org.mercycorps.translationcards.activity.refactored;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.DeckCreationActivity;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getDbManager;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by njimenez on 3/31/16.
 */

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDecksActivityTest {
    public static final String URI = "https://docs.google.com/forms/d" +
            "/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/viewform?entry.1158658650=0.3.2";

    @Test
    public void testDecksActivityCreation(){
        setUpMocksWithEmptyDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        assertNotNull(activity);
    }

    @Test
    public void shouldShowDecksFooterTitleWhenNoDecksArePresent(){
        setUpMocksWithEmptyDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        TextView textView = findTextView(activity, R.id.empty_myDecks_title);
        assertTrue(textView.getVisibility() == View.VISIBLE);
    }

    @Test
    public void shouldNotShowDecksFooterTitleWhenDecksArePresent(){
        setUpMockWithDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        TextView textView = findTextView(activity, R.id.empty_myDecks_title);
        assertTrue(textView.getVisibility() == View.GONE);
    }

    @Test
    public void shouldNotShowDecksFooterMessageWhenDecksArePresent(){
        setUpMockWithDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        TextView textView = findAnyView(activity, R.id.empty_myDecks_message);
        assertTrue(textView.getVisibility() == View.GONE);
    }

    @Test
    public void shouldLaunchFeedbackFormWhenFeedbackButtonIsClicked() throws Exception {
        setUpMocksWithEmptyDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        click(activity, R.id.feedback_button);
        assertEquals(URI, shadowOf(activity).getNextStartedActivity().getData().toString());
    }

    @Test
    public void shouldLaunchImportDeckWhenImportButtonIsClicked(){
        setUpMocksWithEmptyDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        click(activity, R.id.import_deck_button);
        assertEquals("file/*", shadowOf(activity).getNextStartedActivity().getType());
    }

    @Test
    public void shouldLaunchCreateDeckFlowWhenCreateButtonIsClicked(){
        setUpMocksWithEmptyDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        click(activity, R.id.create_deck_button);
        assertEquals(DeckCreationActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    private void setUpMockWithDecks(){
        setUpMocks(true);
    }

    private void setUpMocksWithEmptyDecks(){
        setUpMocks(false);
    }

    private void setUpMocks(boolean shouldCreateDeck){
        when(getDbManager().getAllDecks()).thenReturn(mockDecks(shouldCreateDeck));
    }

    private Deck[] mockDecks(boolean shouldCreateDeck){
        if(!shouldCreateDeck) return null;
        Deck[] arrayOfDecks = new Deck[1];
        Deck deck = new Deck("", "", "", 0l, false);
        arrayOfDecks[0] = deck;
        return arrayOfDecks;
    }


}
