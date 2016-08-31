package org.mercycorps.translationcards.model;

import android.os.Parcel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.porting.JsonKeys;
import org.mercycorps.translationcards.service.LanguageService;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DeckTest {
    private static final String NO_VALUE = "";
    private Deck deck;
    public static final String EXPORTED_DECK_NAME = "exportedDeckName";
    private LanguageService languageService;

    @Before
    public void setUp() throws Exception {
        languageService = mock(LanguageService.class);
        Dictionary[] dictionaries = {new Dictionary(NO_VALUE)};
        deck = new Deck(NO_VALUE, "author", NO_VALUE, -1, 1454946439262L, false, NO_VALUE, dictionaries);
    }

    @Test
    public void getCreationDate_shouldFormatCreationDate() {
        assertThat(deck.getCreationDateString(), is("02/08/16"));
    }

    @Test
    public void getDeckInformation_shouldFormatDeckInformation() {
        assertThat(deck.getDeckInformation(), is("author, 02/08/16"));
    }

    @Test
    public void shouldWriteDeckMetadataToJson() throws JSONException {
        JSONObject jsonObject = deck.toJSON(EXPORTED_DECK_NAME);

        assertEquals(EXPORTED_DECK_NAME, jsonObject.getString(JsonKeys.DECK_LABEL));
        assertEquals(deck.getAuthor(), jsonObject.getString(JsonKeys.PUBLISHER));
        assertEquals(deck.getExternalId(), jsonObject.getString(JsonKeys.EXTERNAL_ID));
        assertEquals(deck.getTimestamp(), jsonObject.getLong(JsonKeys.TIMESTAMP));
        assertEquals(deck.getSourceLanguageIso(), jsonObject.getString(JsonKeys.SOURCE_LANGUAGE));
        assertEquals(deck.isLocked(), jsonObject.getBoolean(JsonKeys.LOCKED));
    }

    @Test
    public void shouldReturnDestinationLanguagesAsString() throws Exception {
        Dictionary[] dictionaries = new Dictionary[]{
                new Dictionary("en", "English", null, 1L),
                new Dictionary("fa", "Farsi", null, 2L)
        };
        Deck deck = new Deck(NO_VALUE, "author", NO_VALUE, -1, 1454946439262L, false, NO_VALUE, dictionaries);

        String destinationLanguagesForDisplay = deck.getDestinationLanguagesForDisplay();

        assertEquals("ENGLISH  FARSI", destinationLanguagesForDisplay);
    }

    @Test
    public void shouldWriteOneDictionaryToJson() throws JSONException {
        Dictionary dictionary = mock(Dictionary.class);
        Dictionary[] dictionaries = {dictionary};
        JSONObject dictionaryJSON = new JSONObject();
        when(dictionary.toJSON()).thenReturn(dictionaryJSON);
        Deck deck = new Deck(NO_VALUE, "author", NO_VALUE, -1, 1454946439262L, false, NO_VALUE, dictionaries);

        JSONObject jsonObject = deck.toJSON(EXPORTED_DECK_NAME);

        assertEquals(dictionaryJSON, jsonObject.getJSONArray(JsonKeys.DICTIONARIES).get(0));
    }

    @Test
    public void shouldWriteMultipleDictionariesToJson() throws JSONException {
        Dictionary firstDictionary = mock(Dictionary.class);
        Dictionary secondDictionary = mock(Dictionary.class);
        Dictionary[] dictionaries = {firstDictionary, secondDictionary};
        JSONObject firstDictionaryJSON = new JSONObject();
        JSONObject secondDictionaryJSON = new JSONObject();
        when(firstDictionary.toJSON()).thenReturn(firstDictionaryJSON);
        when(secondDictionary.toJSON()).thenReturn(secondDictionaryJSON);
        Deck deck = new Deck(NO_VALUE, "author", NO_VALUE, -1, 1454946439262L, false, NO_VALUE, dictionaries);

        JSONObject jsonObject = deck.toJSON(EXPORTED_DECK_NAME);

        JSONArray dictionariesArray = jsonObject.getJSONArray(JsonKeys.DICTIONARIES);
        assertEquals(2, dictionariesArray.length());
        assertEquals(firstDictionaryJSON, dictionariesArray.get(0));
        assertEquals(secondDictionaryJSON, dictionariesArray.get(1));
    }

    @Test
    public void shouldReturnAudioPathMapForAllDictionaries() throws Exception {
        Dictionary firstDictionary = mock(Dictionary.class);
        Dictionary secondDictionary = mock(Dictionary.class);
        Dictionary[] dictionaries = {firstDictionary, secondDictionary};
        Map<String, Boolean> firstDictionaryMap = new HashMap<>();
        firstDictionaryMap.put("firstPath", false);
        firstDictionaryMap.put("secondPath", false);
        Map<String, Boolean> secondDictionaryMap = new HashMap<>();
        secondDictionaryMap.put("thirdPath", false);
        secondDictionaryMap.put("fourthPath", false);
        when(firstDictionary.getAudioPaths()).thenReturn(firstDictionaryMap);
        when(secondDictionary.getAudioPaths()).thenReturn(secondDictionaryMap);
        Deck deck = new Deck(NO_VALUE, "author", NO_VALUE, -1, 1454946439262L, false, NO_VALUE, dictionaries);

        Map<String, Boolean> audioFilePaths = deck.getAudioFilePaths();

        assertEquals(4, audioFilePaths.size());
        assertTrue(audioFilePaths.keySet().containsAll(firstDictionary.getAudioPaths().keySet()));
        assertTrue(audioFilePaths.keySet().containsAll(secondDictionary.getAudioPaths().keySet()));
    }

    @Test
    public void shouldInflateFromParcel() throws Exception {
        Parcel parcel = Parcel.obtain();

        deck.writeToParcel(parcel, 1);
        parcel.setDataPosition(0); //data position must be reset before reading from parcel
        Deck fromParcel = Deck.CREATOR.createFromParcel(parcel);

        assertEquals(deck.getTitle(), fromParcel.getTitle());
        assertEquals(deck.getAuthor(), fromParcel.getAuthor());
        assertEquals(deck.getExternalId(), fromParcel.getExternalId());
        assertEquals(deck.getDbId(), fromParcel.getDbId());
        assertEquals(deck.getTimestamp(), fromParcel.getTimestamp());
        assertEquals(deck.isLocked(), fromParcel.isLocked());
        assertEquals(deck.getSourceLanguageName(), fromParcel.getSourceLanguageName());
        assertEquals(deck.getAudioFilePaths(), fromParcel.getAudioFilePaths());
    }
}