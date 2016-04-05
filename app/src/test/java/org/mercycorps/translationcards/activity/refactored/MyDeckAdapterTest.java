package org.mercycorps.translationcards.activity.refactored;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.widget.DialogTitle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import junit.framework.TestCase;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.TranslationsActivity;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowDialog;

import java.util.Arrays;
import java.util.Collections;

import static java.util.Collections.singletonList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findAnyView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getAlertDialogTitleId;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getDbManager;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by karthikbalasubramanian on 4/1/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDeckAdapterTest extends TestCase {

    private static final String DEFAULT_DECK_NAME = "DefaultDeckName";
    private static final String DEFAULT_PUBLISHER = "DefaultPublisher";
    private static final String DEFAULT_FORMATTED_DATE = "12/31/69";
    private static final String DEFAULT_DECK_INFORMATION = DEFAULT_PUBLISHER+ ", "+DEFAULT_FORMATTED_DATE;
    private static final String DEFAULT_TRANSLATION_LANGUAGES = "DefaultTranslationLanguages";
    public static final String DEFAULT_ALERT_DIALOG_TITLE= "Are you sure you want to delete this deck?";

    @Test
    public void shouldHaveValidDeckNameWhenDeckIsPresent() throws Exception{
        ArrayAdapter<Deck> adapter = createAdapter();
        View view = adapter.getView(0, null, null);
        TextView deckNameTextView = (TextView) view.findViewById(R.id.deck_name);
        assertEquals(DEFAULT_DECK_NAME, deckNameTextView.getText().toString());
    }

    @Ignore
    @Test
    public void shouldHaveDeckInformationWhenDeckIsPresent() throws Exception {
        ArrayAdapter<Deck> adapter = createAdapter();
        View view = adapter.getView(0, null, null);
        TextView deckInformationTextView = findAnyView(view, R.id.deck_information);
        assertEquals(DEFAULT_DECK_INFORMATION, deckInformationTextView.getText().toString());
    }

    @Test
    public void shouldHaveTranslationLanguagesTextWhenDeckIsPresent(){
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapter();
        View view = adapter.getView(0, null, null);
        TextView translationLanguagesTextView  = findAnyView(view, R.id.translation_languages);
        assertEquals(DEFAULT_TRANSLATION_LANGUAGES, translationLanguagesTextView.getText().toString());
    }

    @Test
    public void shouldLaunchAlertDialogWhenDeleteButtonClicked(){
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapter();
        View view = adapter.getView(0, null, null);
        click(view, R.id.deck_card_expansion_delete);
        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        String alertDialogTitle = ((DialogTitle) alertDialog.findViewById(getAlertDialogTitleId())).getText().toString();
        assertThat(alertDialogTitle, is(DEFAULT_ALERT_DIALOG_TITLE));
    }

    @Test
    public void shouldCallDbManagerDeleteWhenDeleteButtonIsClicked(){
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapter();
        MyDecksActivity myDecksActivity = (MyDecksActivity) adapter.getContext();
        View view = adapter.getView(0, null, null);
        click(view, R.id.deck_card_expansion_delete);
        AlertDialog alertDialog = ((AlertDialog) ShadowDialog.getLatestDialog());
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).performClick();
        verify(getDbManager()).deleteDeck(anyLong());
        ListView listView = findAnyView(myDecksActivity, R.id.decks_list);
        assertTrue(listView.getAdapter().getCount() == 0);
    }

    @Test
    public void shouldOpenDeckWhenTitleClicked(){
        setupMocks();
        ArrayAdapter<Deck> adapter = createAdapter();
        MyDecksActivity myDecksActivity = (MyDecksActivity) adapter.getContext();
        View view = adapter.getView(0, null, null);
        click(view, R.id.translation_card);
        assertEquals(TranslationsActivity.class.getName(), shadowOf(myDecksActivity).getNextStartedActivity().getComponent().getClassName());
    }

    private ArrayAdapter<Deck> createAdapter(){
        Intent intent = new Intent();
        MyDecksActivity activity = Robolectric.buildActivity(MyDecksActivity.class).withIntent(intent).create().get();
        return new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, singletonList(createTestDeck()));
    }

    private Deck createTestDeck(){
        return new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0l, 0l, false);
    }

    private void setupMocks(){
        when(getDbManager().getTranslationLanguagesForDeck(anyLong())).thenReturn(DEFAULT_TRANSLATION_LANGUAGES);
        when(getDbManager().getAllDecks()).thenReturn(null);
    }

}