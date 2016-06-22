package org.mercycorps.translationcards.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.widget.DialogTitle;
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
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowPopupMenu;
import org.robolectric.util.ActivityController;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getAlertDialogTitleId;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDeckAdapterTest {

    private static final String DEFAULT_DECK_NAME = "DefaultDeckName";
    private static final String DEFAULT_PUBLISHER = "DefaultPublisher";
    private static final String DEFAULT_FORMATTED_DATE = "12/25/05";
    private static final String DEFAULT_DECK_INFORMATION = DEFAULT_PUBLISHER+ ", "+DEFAULT_FORMATTED_DATE;
    private static final String DEFAULT_TRANSLATION_LANGUAGE = "DefaultTranslationLanguages";
    private static final String DEFAULT_ALERT_DIALOG_TITLE= "Are you sure you want to delete this deck?";
    private static final String ALPHABETICALLY_HIGH_LANGUAGE = "A";
    private static final String DELIMITER = "  ";
    private static final String NAME_FOR_SHARED_DECK = "Name for shared deck?";
    private static final String DEFAULT_SOURCE_LANGUAGE_ISO = "eng";
    private Deck deck;
    private View view;
    private MyDecksActivity activity;
    private ActivityController<MyDecksActivity> controller;
    private DeckService deckService = ((TestMainApplication) RuntimeEnvironment.application).getDeckService();
    private DictionaryService dictionaryService = ((TestMainApplication) RuntimeEnvironment.application).getDictionaryService();
    private final DbManager dbManager = ((TestMainApplication) RuntimeEnvironment.application).getDbManager();

    @Before
    public void setUp() throws Exception {
        when(dbManager.getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{new Dictionary(ALPHABETICALLY_HIGH_LANGUAGE), new Dictionary(DEFAULT_TRANSLATION_LANGUAGE)});
        deck = new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0L, 1135497600000L, false, DEFAULT_SOURCE_LANGUAGE_ISO);
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
    public void shouldHaveTranslationLanguagesTextWhenDeckIsPresent(){
        TextView translationLanguagesTextView  = findAnyView(view, R.id.translation_languages);

        String expectedLanguages = ALPHABETICALLY_HIGH_LANGUAGE.toUpperCase() + DELIMITER + DEFAULT_TRANSLATION_LANGUAGE.toUpperCase();
        assertEquals(expectedLanguages, translationLanguagesTextView.getText().toString());
    }

    @Test
    public void shouldOpenDeckWhenTitleClicked(){
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
    public void shouldShowPopupMenuWhenMenuIsClicked(){
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
    public void shouldLaunchAlertDialogWhenDeleteButtonClicked(){
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemWithId(popupMenu, R.id.delete_deck);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        String alertDialogTitle = ((DialogTitle) alertDialog.findViewById(getAlertDialogTitleId())).getText().toString();
        assertThat(alertDialogTitle, is(DEFAULT_ALERT_DIALOG_TITLE));
    }

    @Test
    public void shouldDeleteDeckWhenDeleteDeckMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemWithId(popupMenu, R.id.delete_deck);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();
        verify(dbManager).deleteDeck(deck.getDbId());
    }

    @Test
    public void shouldNotDisplayLockIconWhenDeckIsUnlocked() {
        View view = getAdapterViewForDeck(deck);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.lock_icon);
        assertEquals(View.GONE, frameLayout.getVisibility());
    }

    @Test
    public void shouldDisplayLockIconWhenDeckIsLocked() {
        Deck lockedDeck = new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0L, 1135497600000L, true, DEFAULT_SOURCE_LANGUAGE_ISO);

        View view = getAdapterViewForDeck(lockedDeck);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.lock_icon);
        assertEquals(View.VISIBLE, frameLayout.getVisibility());
    }

    @Test
    public void shouldShowPopupAlertDialogWhenShareMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemWithId(popupMenu, R.id.share_deck);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        String alertDialogTitle = ((DialogTitle) alertDialog.findViewById(getAlertDialogTitleId())).getText().toString();
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

    private void clickMenuItemWithId(PopupMenu popupMenu, int id) {
        shadowOf(popupMenu).getOnMenuItemClickListener().onMenuItemClick(popupMenu.getMenu().findItem(id));
    }

    private PopupMenu openDeckPopupMenu() {
        View deckMenu = view.findViewById(R.id.deck_menu);
        deckMenu.performClick();
        return ShadowPopupMenu.getLatestPopupMenu();
    }
}