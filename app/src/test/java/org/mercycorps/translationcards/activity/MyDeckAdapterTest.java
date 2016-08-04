package org.mercycorps.translationcards.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.view.DeckItem;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowPopupMenu;
import org.robolectric.util.ActivityController;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDeckAdapterTest {

    private static final String DEFAULT_DECK_NAME = "DefaultDeckName";
    private static final String DEFAULT_PUBLISHER = "DefaultPublisher";
    private static final String DEFAULT_FORMATTED_DATE = "12/25/05";
    private static final String DEFAULT_DECK_INFORMATION = DEFAULT_PUBLISHER + ", " + DEFAULT_FORMATTED_DATE;
    private static final String DEFAULT_TRANSLATION_LANGUAGE = "DefaultTranslationLanguages";
    private static final String DEFAULT_ALERT_DIALOG_TITLE = "Are you sure you want to delete this deck?";
    private static final String ALPHABETICALLY_HIGH_LANGUAGE = "A";
    private static final String DELIMITER = "  ";
    private static final String NAME_FOR_SHARED_DECK = "Name for shared deck?";
    private static final String DEFAULT_SOURCE_LANGUAGE_ISO = "en";
    private static final String DEFAULT_SOURCE_LANGUAGE_NAME = "English";
    private Deck deck;
    private View view;
    private MyDecksActivity activity;
    private ActivityController<MyDecksActivity> controller;
    private DeckService deckService = ((TestMainApplication) RuntimeEnvironment.application).getDeckService();
    private DictionaryService dictionaryService = ((TestMainApplication) RuntimeEnvironment.application).getDictionaryService();

    @Before
    public void setUp() throws Exception {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);
        Cursor cursor = mock(Cursor.class);
        when(cursor.getCount()).thenReturn(1);
        when(cursor.moveToFirst()).thenReturn(true);
        when(cursor.getString(anyInt())).thenReturn("cursor string");
        when(cursor.getLong(anyInt())).thenReturn(12345L);
        when(cursor.getInt(anyInt())).thenReturn(1234);
        SQLiteDatabase sqlLiteDatabase = mock(SQLiteDatabase.class);
        when(sqlLiteDatabase.query(DatabaseHelper.DecksTable.TABLE_NAME, null, null, null, null, null, String.format("%s DESC", DatabaseHelper.DecksTable.ID))).thenReturn(cursor);
        DatabaseHelper databaseHelper = mock(DatabaseHelper.class);
        when(databaseHelper.getReadableDatabase()).thenReturn(sqlLiteDatabase);

        Dictionary[] dictionaries = {new Dictionary(ALPHABETICALLY_HIGH_LANGUAGE), new Dictionary(DEFAULT_TRANSLATION_LANGUAGE)};
        deck = new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0L, 1135497600000L, false, new Language(DEFAULT_SOURCE_LANGUAGE_ISO, DEFAULT_SOURCE_LANGUAGE_NAME), dictionaries);
        view = getAdapterViewForDeck(deck);
    }

    @After
    public void tearDown() {
        controller.pause().stop().destroy();
    }

    private View getAdapterViewForDeck(Deck deck) {
        Intent intent = new Intent();
        controller = Robolectric.buildActivity(MyDecksActivity.class);
        activity = controller.withIntent(intent).create().get();
        ArrayAdapter<Deck> adapter = new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(deck), deckService, dictionaryService);
        return adapter.getView(0, null, null);
    }

    @Test
    public void shouldHaveValidDeckNameWhenDeckIsPresent() {
        View view = getAdapterViewForDeck(deck);

        TextView deckNameTextView = (TextView) view.findViewById(R.id.deck_name);

        assertEquals(DEFAULT_DECK_NAME, deckNameTextView.getText().toString());
    }

    @Test
    public void shouldHaveDeckInformationWhenDeckIsPresent() {
        View view = getAdapterViewForDeck(deck);

        TextView deckInformationTextView = findAnyView(view, R.id.deck_information);

        assertEquals(DEFAULT_DECK_INFORMATION, deckInformationTextView.getText().toString());
    }

    @Test
    public void shouldHaveTranslationLanguagesTextWhenDeckIsPresent() {
        TextView translationLanguagesTextView = findAnyView(view, R.id.translation_languages);

        String expectedLanguages = ALPHABETICALLY_HIGH_LANGUAGE.toUpperCase() + DELIMITER + DEFAULT_TRANSLATION_LANGUAGE.toUpperCase();
        assertEquals(expectedLanguages, translationLanguagesTextView.getText().toString());
    }

    @Test
    public void shouldOpenDeckWhenTitleClicked() {
        view.findViewById(R.id.deck_card).performClick();
        assertEquals(TranslationsActivity.class.getName(), shadowOf((MyDecksActivity) view.getContext()).getNextStartedActivity().getComponent().getClassName());
        verify(deckService).setCurrentDeck(deck);
        verify(dictionaryService).setCurrentDictionary(0);
    }

    @Test
    public void shouldHaveAClickListenerWhenCreatingDeckMenuIcon() {
        View deckMenu = view.findViewById(R.id.deck_menu);

        assertNotNull(shadowOf(deckMenu).getOnClickListener());
    }

    @Test
    public void shouldShowPopupMenuWhenMenuIsClicked() {
        PopupMenu deckMenu = openDeckPopupMenu();

        assertNotNull(deckMenu);
    }

    @Test
    public void shouldShowDeleteButtonWhenMenuIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        assertEquals("Delete", popupMenu.getMenu().findItem(R.id.delete_deck).toString());
    }

    @Test
    public void shouldShowShareButtonWhenMenuIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        assertEquals("Share", popupMenu.getMenu().findItem(R.id.share_deck).toString());
    }

    @Test
    public void shouldHaveClickListenerWhenDeleteDeckMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        assertNotNull(shadowOf(popupMenu).getOnMenuItemClickListener());
    }

    @Test
    public void shouldLaunchAlertDialogWhenDeleteButtonClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemWithId(popupMenu, R.id.delete_deck);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        ShadowDialog shadowDialog = shadowOf(alertDialog);
        assertThat(shadowDialog.getTitle().toString(), is(DEFAULT_ALERT_DIALOG_TITLE));
    }

    @Test
    public void shouldDeleteDeckWhenDeleteDeckMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemWithId(popupMenu, R.id.delete_deck);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();
        verify(deckService).delete(deck);
    }

    @Test
    public void shouldNotDisplayLockIconWhenDeckIsUnlocked() {
        View view = getAdapterViewForDeck(deck);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.lock_icon);
        assertEquals(View.GONE, frameLayout.getVisibility());
    }

    @Test
    public void shouldDisplayLockIconWhenDeckIsLocked() {
        Deck lockedDeck = new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0L, 1135497600000L, true,
                new Language(DEFAULT_SOURCE_LANGUAGE_ISO, DEFAULT_SOURCE_LANGUAGE_NAME), new Dictionary[0]);
        View view = getAdapterViewForDeck(lockedDeck);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.lock_icon);
        assertEquals(View.VISIBLE, frameLayout.getVisibility());
    }

    @Test
    public void shouldShowPopupAlertDialogWhenShareMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemWithId(popupMenu, R.id.share_deck);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        ShadowAlertDialog shadowAlertDialog = shadowOf(alertDialog);
        String alertDialogTitle = shadowAlertDialog.getTitle().toString();
        assertEquals(NAME_FOR_SHARED_DECK, alertDialogTitle);
    }

    @Test
    public void shouldStartExportTaskWhenShareMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemWithId(popupMenu, R.id.share_deck);
        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();

        assertEquals(Intent.ACTION_SEND, shadowOf(activity).getNextStartedActivity().getAction());
    }

    @Test
    public void shouldCreateNewViewWhenExistingViewIsNull() {
        controller = Robolectric.buildActivity(MyDecksActivity.class);
        activity = controller.withIntent(new Intent()).create().get();
        ArrayAdapter<Deck> adapter = new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(deck), deckService, dictionaryService);

        View view = adapter.getView(0, null, null);
        TextView deckName = (TextView) view.findViewById(R.id.deck_name);

        assertEquals(DeckItem.class.getName(), view.getClass().getName());
        assertEquals(deck.getTitle(), deckName.getText().toString());
    }

    @Test
    public void shouldModifyExistingViewIfViewNotNull() {
        controller = Robolectric.buildActivity(MyDecksActivity.class);
        activity = controller.withIntent(new Intent()).create().get();
        ArrayAdapter<Deck> adapter = new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(deck), deckService, dictionaryService);

        DeckItem view = new DeckItem(activity.getApplicationContext());
        adapter.getView(0, view, null);

        TextView deckName = (TextView) view.findViewById(R.id.deck_name);
        assertEquals(deck.getTitle(), deckName.getText().toString());
    }

    private void clickMenuItemWithId(PopupMenu popupMenu, int id) {
        shadowOf(popupMenu).getOnMenuItemClickListener().onMenuItemClick(popupMenu.getMenu().findItem(id));
    }

    private PopupMenu openDeckPopupMenu() {
        View deckMenu = view.findViewById(R.id.deck_menu);
        deckMenu.performClick();
        return ShadowPopupMenu.getLatestPopupMenu();
    }
}