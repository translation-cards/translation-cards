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
import org.mercycorps.translationcards.activity.addDeck.EnterDeckTitleActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowPopupMenu;
import org.robolectric.util.ActivityController;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getAlertDialogTitleId;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDeckAdapterTest {

    private static final String DEFAULT_DECK_NAME = "DefaultDeckName";
    private static final String DEFAULT_PUBLISHER = "DefaultPublisher";
    private static final String DEFAULT_FORMATTED_DATE = "12/31/69";
    private static final String DEFAULT_DECK_INFORMATION = DEFAULT_PUBLISHER+ ", "+DEFAULT_FORMATTED_DATE;
    private static final String DEFAULT_TRANSLATION_LANGUAGE = "DefaultTranslationLanguages";
    public static final String DEFAULT_ALERT_DIALOG_TITLE= "Are you sure you want to delete this deck?";
    private static final String ALPHABETICALLY_HIGH_LANGUAGE = "A";
    private static final String DELIMITER = "  ";
    public static final String NAME_FOR_SHARED_DECK = "Name for shared deck?";
    private Deck deck;
    private View view;
    private MyDecksActivity activity;
    private ActivityController<MyDecksActivity> controller;

    @Before
    public void setUp() throws Exception {
        deck = mock(Deck.class);
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
        when(deck.getDictionaries()).thenReturn(new Dictionary[]{new Dictionary(ALPHABETICALLY_HIGH_LANGUAGE), new Dictionary(DEFAULT_TRANSLATION_LANGUAGE)});
        ArrayAdapter<Deck> adapter = new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(deck));
        return adapter.getView(0, null, null);
    }

    @Test
    public void shouldHaveValidDeckNameWhenDeckIsPresent() {
        when(deck.getLabel()).thenReturn(DEFAULT_DECK_NAME);
        View view = getAdapterViewForDeck(deck);

        TextView deckNameTextView = (TextView) view.findViewById(R.id.deck_name);

        assertEquals(DEFAULT_DECK_NAME, deckNameTextView.getText().toString());
    }

    @Test
    public void shouldHaveDeckInformationWhenDeckIsPresent() {
        when(deck.getAuthor()).thenReturn("DefaultPublisher");
        when(deck.getCreationDateString()).thenReturn("12/31/69");
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
        click(view, R.id.translation_card);

        assertEquals(TranslationsActivity.class.getName(), shadowOf((MyDecksActivity) view.getContext()).getNextStartedActivity().getComponent().getClassName());
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

        assertEquals("Delete", popupMenu.getMenu().getItem(2).toString());
    }

    @Test
    public void shouldShowShareButtonWhenMenuIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        assertEquals("Share", popupMenu.getMenu().getItem(1).toString());
    }

    @Test
    public void shouldHaveClickListenerWhenDeleteDeckMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        assertNotNull(shadowOf(popupMenu).getOnMenuItemClickListener());
    }

    @Test
    public void shouldLaunchAlertDialogWhenDeleteButtonClicked(){
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 2);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        String alertDialogTitle = ((DialogTitle) alertDialog.findViewById(getAlertDialogTitleId())).getText().toString();
        assertThat(alertDialogTitle, is(DEFAULT_ALERT_DIALOG_TITLE));
    }

    @Test
    public void shouldDeleteDeckWhenDeleteDeckMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 2);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();
        verify(deck).delete();
    }

    @Test
    public void shouldNotDisplayLockIconWhenDeckIsUnlocked() {
        when(deck.isLocked()).thenReturn(false);

        View view = getAdapterViewForDeck(deck);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.lock_icon);
        assertEquals(View.GONE, frameLayout.getVisibility());
    }

    @Test
    public void shouldDisplayLockIconWhenDeckIsLocked() {
        when(deck.isLocked()).thenReturn(true);

        View view = getAdapterViewForDeck(deck);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.lock_icon);
        assertEquals(View.VISIBLE, frameLayout.getVisibility());
    }

    @Test
    public void shouldShowPopupAlertDialogWhenShareMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 1);

        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        String alertDialogTitle = ((DialogTitle) alertDialog.findViewById(getAlertDialogTitleId())).getText().toString();
        assertEquals(NAME_FOR_SHARED_DECK, alertDialogTitle);
    }

    @Test
    public void shouldStartExportTaskWhenShareMenuItemIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 1);
        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();

        assertEquals(Intent.ACTION_SEND, shadowOf(activity).getNextStartedActivity().getAction());
    }

    @Test
    public void shouldShowEditButtonWhenMenuIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        assertEquals("Edit", popupMenu.getMenu().getItem(0).toString());
    }

    @Test
    public void shouldOpenEnterDeckTitleUserFlowWhenEditMenuOptionIsSelected() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 0);

        assertEquals(EnterDeckTitleActivity.class.getCanonicalName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldBuildCorrectLanguagesInputForNewDeckContextWhenEditMenuOptionIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 0);

        assertEquals(ALPHABETICALLY_HIGH_LANGUAGE + ", " + DEFAULT_TRANSLATION_LANGUAGE, ((NewDeckContext) shadowOf(activity).getNextStartedActivity().getSerializableExtra("Deck")).getLanguagesInput());
    }

    @Test
    public void shouldPassCorrectDeckForNewDeckContextWhenEditMenuOptionIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 0);

        assertEquals(deck.getLabel(), ((NewDeckContext) shadowOf(activity).getNextStartedActivity().getSerializableExtra("Deck")).getDeckLabel());
    }

    @Test
    public void shouldPassEditFlagWhenEditMenuOptionIsClicked() {
        PopupMenu popupMenu = openDeckPopupMenu();

        clickMenuItemAtIndex(popupMenu, 0);

        assertEquals(true, ((NewDeckContext) shadowOf(activity).getNextStartedActivity().getSerializableExtra("Deck")).getIsEditFlag());
    }

    private void clickMenuItemAtIndex(PopupMenu popupMenu, int index) {
        shadowOf(popupMenu).getOnMenuItemClickListener().onMenuItemClick(popupMenu.getMenu().getItem(index));
    }

    private PopupMenu openDeckPopupMenu() {
        View deckMenu = view.findViewById(R.id.deck_menu);
        deckMenu.performClick();
        return ShadowPopupMenu.getLatestPopupMenu();
    }
}