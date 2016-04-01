package org.mercycorps.translationcards.activity.refactored;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.*;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.when;

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

    @Test
    public void shouldNotBeNull() throws Exception {
        ArrayAdapter adapter = createAdapter();
        assertNotNull(adapter);
    }

    @Test
    public void shouldHaveValidDeckNameWhenDeckIsPresent() throws Exception{
        ArrayAdapter<Deck> adapter = createAdapter();
        View view = adapter.getView(0, null, null);
        TextView deckNameTextView = (TextView) view.findViewById(R.id.deck_name);
        assertEquals(DEFAULT_DECK_NAME, deckNameTextView.getText().toString());
    }

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


    private ArrayAdapter<Deck> createAdapter(){
        Intent intent = new Intent();
        Activity activity = Robolectric.buildActivity(MyDecksActivity.class).withIntent(intent).create().get();
        return new MyDeckAdapter(activity, R.layout.deck_item, R.id.deck_name, Arrays.asList(createMockDeck()));
    }

    private Deck createMockDeck(){
        return new Deck(DEFAULT_DECK_NAME, DEFAULT_PUBLISHER, "", 0l, 0l, false);
    }

    private void setupMocks(){
        when(getDbManager().getTranslationLanguagesForDeck(anyLong())).thenReturn(DEFAULT_TRANSLATION_LANGUAGES);
    }

}