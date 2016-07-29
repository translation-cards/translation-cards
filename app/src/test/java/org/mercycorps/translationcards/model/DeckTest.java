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

import java.util.Arrays;
import java.util.List;

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
    public void shouldReturnAudioPathsForAllDictionaries() throws Exception {
        Dictionary firstDictionary = mock(Dictionary.class);
        Dictionary secondDictionary = mock(Dictionary.class);
        Dictionary[] dictionaries = {firstDictionary, secondDictionary};
        when(firstDictionary.getAudioPaths()).thenReturn(Arrays.asList("firstPath", "secondPath"));
        when(secondDictionary.getAudioPaths()).thenReturn(Arrays.asList("thirdPath", "fourthPath"));
        when(dictionaryRepository.getAllDictionariesForDeck(anyLong())).thenReturn(dictionaries);

        List<String> audioFilePaths = deck.getAudioFilePaths();

        assertEquals(4, audioFilePaths.size());
        assertTrue(audioFilePaths.containsAll(firstDictionary.getAudioPaths()));
        assertTrue(audioFilePaths.containsAll(secondDictionary.getAudioPaths()));
    }
}