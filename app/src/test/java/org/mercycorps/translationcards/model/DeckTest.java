package org.mercycorps.translationcards.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.porting.JsonKeys;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DeckTest {
    private Deck deck;
    public static final String EXPORTED_DECK_NAME = "exportedDeckName";
    private DictionaryRepository dictionaryRepository;

    @Before
    public void setUp() throws Exception {
        deck = new Deck("", "author", "", -1, 1454946439262L, false, new Language("", ""));
        TestMainApplication mainApplication = (TestMainApplication) RuntimeEnvironment.application;
        dictionaryRepository = mainApplication.getDictionaryRepository();
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{new Dictionary("")});
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
    public void shouldReturnDestinationLanguagesAsString() throws Exception {
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{
                new Dictionary("en", "English", null, 1L, 1L),
                new Dictionary("fa", "Farsi", null, 2L, 1L)
        });
        String destinationLanguagesForDisplay = deck.getDestinationLanguagesForDisplay();

        assertEquals("ENGLISH  FARSI", destinationLanguagesForDisplay);
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
    public void shouldWriteOneDictionaryToJson() throws JSONException {
        Dictionary dictionary = mock(Dictionary.class);
        Dictionary[] dictionaries = {dictionary};
        JSONObject dictionaryJSON = new JSONObject();
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(dictionaries);
        when(dictionary.toJSON()).thenReturn(dictionaryJSON);

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
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(dictionaries);

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
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(dictionaries);

        Map<String, Boolean> audioFilePaths = deck.getAudioFilePaths();

        assertEquals(4, audioFilePaths.size());
        assertTrue(audioFilePaths.keySet().containsAll(firstDictionary.getAudioPaths().keySet()));
        assertTrue(audioFilePaths.keySet().containsAll(secondDictionary.getAudioPaths().keySet()));
    }

    @Test
    public void shouldRetrieveIsoCodeForDictionariesWithEmptyIsoCode() throws JSONException {
        Dictionary emptyIsoDictionary = new Dictionary("", "English", new Translation[0], 789L, 123L);
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{emptyIsoDictionary});
        JSONObject jsonObject = deck.toJSON("");


        JSONArray dictionariesArray = jsonObject.getJSONArray(JsonKeys.DICTIONARIES);
        JSONObject actualDictionary = dictionariesArray.getJSONObject(0);

        assertEquals(1, dictionariesArray.length());
        assertEquals("en", actualDictionary.getString(JsonKeys.DICTIONARY_DEST_ISO_CODE));
    }

    @Test
    public void shouldRetrieveIsoCodeForDictionariesWithNullIsoCode() throws JSONException {
        Dictionary nullIsoDictionary = new Dictionary(null, "English", new Translation[0], 789L, 123L);
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(new Dictionary[]{nullIsoDictionary});
        JSONObject jsonObject = deck.toJSON("");

        JSONArray dictionariesArray = jsonObject.getJSONArray(JsonKeys.DICTIONARIES);
        JSONObject actualDictionary = dictionariesArray.getJSONObject(0);

        assertEquals(1, dictionariesArray.length());
        assertEquals("en", actualDictionary.getString(JsonKeys.DICTIONARY_DEST_ISO_CODE));
    }
}