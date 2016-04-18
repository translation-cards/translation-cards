package org.mercycorps.translationcards.activity.refactored;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.DeckCreationActivity;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.createActivityToTest;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.createActivityToTestWithTranslationContext;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findView;
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
    private static final String DEFAULT_ISO_CODE = "en";

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
        TextView textView = findTextView(activity, R.id.empty_my_decks_title);
        assertEquals(View.VISIBLE, textView.getVisibility());
    }

    @Test
    public void shouldShowDecksHeader() {
        setUpMockWithDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        LinearLayout linearLayout = findLinearLayout(activity, R.id.my_decks_header);
        assertEquals(View.VISIBLE, linearLayout.getVisibility());
    }

    @Test
    public void shouldNotShowDecksFooterTitleWhenDecksArePresent(){
        setUpMockWithDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        TextView textView = findTextView(activity, R.id.empty_my_decks_title);
        assertEquals(View.GONE, textView.getVisibility());
    }

    @Test
    public void shouldNotShowDecksFooterMessageWhenDecksArePresent(){
        setUpMockWithDecks();
        Activity activity = createActivityToTest(MyDecksActivity.class);
        TextView textView = findAnyView(activity, R.id.empty_my_decks_message);
        assertEquals(View.GONE, textView.getVisibility());
    }

    @Test
    public void shouldLaunchFeedbackFormWhenFeedbackButtonIsClicked() throws Exception {
        setUpMockWithDecks();
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
        Deck deck = new Deck("", "", "", 0l, false, DEFAULT_ISO_CODE);
        arrayOfDecks[0] = deck;
        return arrayOfDecks;
    }


}
