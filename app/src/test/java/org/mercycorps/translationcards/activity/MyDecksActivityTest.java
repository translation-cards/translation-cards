package org.mercycorps.translationcards.activity;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.GetStartedDeckActivity;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.util.MyDecksActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findTextView;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDecksActivityTest {
    public static final String URI = "https://docs.google.com/forms/d" +
            "/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/viewform?entry.1158658650=1.1.0";
    private static final String DEFAULT_ISO_CODE = "en";
    private static final String DEFAULT_LANGUAGE_NAME = "English";
    private MyDecksActivityHelper<MyDecksActivity> helper = new MyDecksActivityHelper<>(MyDecksActivity.class);

    @Inject DeckRepository deckRepository;

    @Before
    public void setup() {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);

        Cursor cursor = mock(Cursor.class);
        when(cursor.getCount()).thenReturn(1);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getString(anyInt())).thenReturn("cursor string");
        when(cursor.getLong(anyInt())).thenReturn(12345l);
        when(cursor.getInt(anyInt())).thenReturn(1234);
        SQLiteDatabase sqlLiteDatabase = mock(SQLiteDatabase.class);
        when(sqlLiteDatabase.query(DatabaseHelper.DecksTable.TABLE_NAME, null, null, null, null, null, String.format("%s DESC", DatabaseHelper.DecksTable.ID))).thenReturn(cursor);
        DatabaseHelper databaseHelper = mock(DatabaseHelper.class);
        when(databaseHelper.getReadableDatabase()).thenReturn(sqlLiteDatabase);
    }
    
    @After 
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void testDecksActivityCreation(){
        setUpMocksWithEmptyDecks();
        Activity activity = helper.createActivityToTest();
        assertNotNull(activity);
    }

    @Test
    public void shouldShowDecksFooterTitleWhenNoDecksArePresent(){
        setUpMocksWithEmptyDecks();
        Activity activity = helper.createActivityToTest();
        TextView textView = findTextView(activity, R.id.empty_my_decks_title);
        assertEquals(View.VISIBLE, textView.getVisibility());
    }

    @Test
    public void shouldShowDecksHeader() {
        setUpMockWithDecks();
        Activity activity = helper.createActivityToTest();
        LinearLayout linearLayout = findLinearLayout(activity, R.id.my_decks_header);
        assertEquals(View.VISIBLE, linearLayout.getVisibility());
    }

    @Test
    public void shouldNotShowDecksFooterTitleWhenDecksArePresent(){
        setUpMockWithDecks();
        Activity activity = helper.createActivityToTest();
        TextView textView = findTextView(activity, R.id.empty_my_decks_title);
        assertEquals(View.GONE, textView.getVisibility());
    }

    @Test
    public void shouldNotShowDecksFooterMessageWhenDecksArePresent(){
        setUpMockWithDecks();
        Activity activity = helper.createActivityToTest();
        TextView textView = findAnyView(activity, R.id.empty_my_decks_message);
        assertEquals(View.GONE, textView.getVisibility());
    }

    @Test
    public void shouldLaunchFeedbackFormWhenFeedbackButtonIsClicked() throws Exception {
        setUpMockWithDecks();
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.feedback_button);
        assertEquals(URI, shadowOf(activity).getNextStartedActivity().getData().toString());
    }

    @Test
    public void shouldLaunchImportDeckWhenImportButtonIsClicked(){
        setUpMocksWithEmptyDecks();
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.import_deck_button);
        assertEquals("file/*", shadowOf(activity).getNextStartedActivity().getType());
    }

    @Test
    public void shouldLaunchCreateDeckFlowWhenCreateButtonIsClicked(){
        setUpMocksWithEmptyDecks();
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.create_deck_button);
        assertEquals(GetStartedDeckActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    private void setUpMockWithDecks(){
        setUpMocks(true);
    }

    private void setUpMocksWithEmptyDecks(){
        setUpMocks(false);
    }

    private void setUpMocks(boolean shouldCreateDeck){
        when(deckRepository.getAllDecks()).thenReturn(mockDecks(shouldCreateDeck));
    }

    private Deck[] mockDecks(boolean shouldCreateDeck){
        if(!shouldCreateDeck) return null;
        Deck[] arrayOfDecks = new Deck[1];
        Deck deck = new Deck("", "", "", 0l, false, new Language(DEFAULT_ISO_CODE, DEFAULT_LANGUAGE_NAME));
        arrayOfDecks[0] = deck;
        return arrayOfDecks;
    }


}
