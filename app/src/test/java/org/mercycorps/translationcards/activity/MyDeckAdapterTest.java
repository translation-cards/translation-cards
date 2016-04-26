package org.mercycorps.translationcards.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.widget.DialogTitle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowPopupMenu;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getAlertDialogTitleId;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getDbManager;
import static org.mockito.Matchers.anyLong;
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
    private Deck unlockedDeck;

    @Test
    public void shouldHaveValidDeckNameWhenDeckIsPresent() throws Exception{
        setupMockDictionaries();
        ArrayAdapter<Deck> adapter = createAdapterUnlockedDeck();
        View view = adapter.getView(0, null, null);
        TextView deckNameTextView = (TextView) view.findViewById(R.id.deck_name);
        assertEquals(DEFAULT_DECK_NAME, deckNameTextView.getText().toString());
    }

    @Ignore
    @Test
    public void shouldHaveDeckInformationWhenDeckIsPresent() throws Exception {
        setupMockDictionaries();
        ArrayAdapter<Deck> adapter = createAdapterUnlockedDeck();
        View view = adapter.getView(0, null, null);
        TextView deckInformationTextView = findAnyView(view, R.id.deck_information);
        assertEquals(DEFAULT_DECK_INFORMATION, deckInformationTextView.getText().toString());
    }

    @Test
    public void shouldHaveTranslationLanguagesTextWhenDeckIsPresent(){
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapterUnlockedDeck();
        View view = adapter.getView(0, null, null);
        TextView translationLanguagesTextView  = findAnyView(view, R.id.translation_languages);
        String expectedLanguages = ALPHABETICALLY_HIGH_LANGUAGE.toUpperCase() + DELIMITER + DEFAULT_TRANSLATION_LANGUAGE.toUpperCase();
        assertEquals(expectedLanguages, translationLanguagesTextView.getText().toString());
    }

    @Test
    public void shouldOpenDeckWhenTitleClicked(){
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapterUnlockedDeck();
        MyDecksActivity myDecksActivity = (MyDecksActivity) adapter.getContext();
        View view = adapter.getView(0, null, null);
        click(view, R.id.translation_card);
        assertEquals(TranslationsActivity.class.getName(), shadowOf(myDecksActivity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldNotShowMakeACopyOptionWhenDeckIsNotLocked() {
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapterUnlockedDeck();
        View view = adapter.getView(0, null, null);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.deck_card_copy);
        assertTrue(linearLayout.getVisibility() == View.GONE);
    }

    @Test
    public void shouldMakeCopyOptionVisibleWhenDeckIsLocked(){
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapterLockedDeck();
        View view = adapter.getView(0, null, null);
        LinearLayout linearLayout = (LinearLayout)view.findViewById(R.id.deck_card_copy);
        assertTrue(linearLayout.getVisibility() == View.VISIBLE);
    }

    @Test
    public void shouldHaveAClickListenerWhenCreatingDeckMenuIcon() {
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapterUnlockedDeck();
        View view = adapter.getView(0, null, null);
        View deckMenu = view.findViewById(R.id.deck_menu);
        assertNotNull(shadowOf(deckMenu).getOnClickListener());
    }

    @Test
    public void shouldShowPopupMenuWhenMenuIsClicked(){
        PopupMenu deckMenu = setupAdapterAndGetPopupMenu();
        assertNotNull(deckMenu);
    }

    @Test
     public void shouldShowDeleteButtonWhenMenuIsClicked() {
        PopupMenu popupMenu = setupAdapterAndGetPopupMenu();
        assertEquals("Delete", popupMenu.getMenu().getItem(0).toString());
    }

    @Test
    public void shouldShowShareButtonWhenMenuIsClicked() {
        PopupMenu popupMenu = setupAdapterAndGetPopupMenu();
        assertEquals("Share", popupMenu.getMenu().getItem(1).toString());
    }

    @Test
    public void shouldHaveClickListenerWhenDeleteDeckMenuItemIsClicked() {
        PopupMenu popupMenu = setupAdapterAndGetPopupMenu();
        assertNotNull(shadowOf(popupMenu).getOnMenuItemClickListener());
    }

    @Test
    public void shouldLaunchAlertDialogWhenDeleteButtonClicked(){
        PopupMenu popupMenu = setupAdapterAndGetPopupMenu();
        clickMenuItemAtIndex(popupMenu, 0);
        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        String alertDialogTitle = ((DialogTitle) alertDialog.findViewById(getAlertDialogTitleId())).getText().toString();
        assertThat(alertDialogTitle, is(DEFAULT_ALERT_DIALOG_TITLE));
    }

    @Test
    public void shouldDeleteDeckWhenDeleteDeckMenuItemIsClicked() {
        PopupMenu popupMenu = setupAdapterAndGetPopupMenu();
        clickMenuItemAtIndex(popupMenu, 0);
        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();
        verify(unlockedDeck).delete();
    }

    private void clickMenuItemAtIndex(PopupMenu popupMenu, int index) {
        shadowOf(popupMenu).getOnMenuItemClickListener().onMenuItemClick(popupMenu.getMenu().getItem(index));
    }

    @NonNull
    private PopupMenu setupAdapterAndGetPopupMenu() {
        setupMockDeck();
        ArrayAdapter<Deck> adapter = createAdapterWithMockUnlockDeck();
        View view = adapter.getView(0, null, null);
        View deckMenu = view.findViewById(R.id.deck_menu);
        deckMenu.performClick();
        return ShadowPopupMenu.getLatestPopupMenu();
    }

    private ArrayAdapter<Deck> createAdapterWithMockUnlockDeck() {
        Intent intent = new Intent();
        MyDecksActivity activity = Robolectric.buildActivity(MyDecksActivity.class).withIntent(intent).create().get();
        return new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(unlockedDeck));
    }

    private ArrayAdapter<Deck> createAdapterUnlockedDeck(){
        Intent intent = new Intent();
        MyDecksActivity activity = Robolectric.buildActivity(MyDecksActivity.class).withIntent(intent).create().get();
        Deck unlockedDeck = createUnlockedTestDeck();
        return new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(unlockedDeck));
    }

    private ArrayAdapter<Deck> createAdapterLockedDeck(){
        Intent intent = new Intent();
        MyDecksActivity activity = Robolectric.buildActivity(MyDecksActivity.class).withIntent(intent).create().get();
        return new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(createLockedTestDeck()));
    }

    private Deck createUnlockedTestDeck(){
        return new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0l, 0l, false, "");
    }

    private Deck createLockedTestDeck(){
        return new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0l, 0l, true, "");
    }

    private void setupMocks(){
        setupMockDictionaries();
        when(getDbManager().getAllDecks()).thenReturn(null);
    }

    private void setupMockDeck() {
        unlockedDeck = mock(Deck.class);
        when(unlockedDeck.getDbId()).thenReturn(5l);
        when(unlockedDeck.getDictionaries()).thenReturn(new Dictionary[]{new Dictionary(ALPHABETICALLY_HIGH_LANGUAGE), new Dictionary(DEFAULT_TRANSLATION_LANGUAGE)});
    }

    private void setupMockDictionaries() {
        when(getDbManager().getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{new Dictionary(ALPHABETICALLY_HIGH_LANGUAGE), new Dictionary(DEFAULT_TRANSLATION_LANGUAGE)});
    }

}