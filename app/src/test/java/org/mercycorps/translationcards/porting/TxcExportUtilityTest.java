package org.mercycorps.translationcards.porting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.LanguageService;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TxcExportUtilityTest {

    public static final String EXPORTED_DECK_NAME = "exportedDeckName";
    private TxcExportUtility txcExportUtility;
    private Deck deck;
    private Dictionary[] dictionaries;
    private Translation firstTranslation;
    private Translation secondTranslation;
    private Dictionary dictionary;
    private LanguageService languageServiceMock;

    @Before
    public void setup() {
        languageServiceMock = mock(LanguageService.class);
        txcExportUtility = new TxcExportUtility(languageServiceMock);

        initializeStubs();
    }

    private void initializeStubs() {
        when(languageServiceMock.getIsoForLanguage("English")).thenReturn("en");
        Language language = new Language("en", "English");
        deck = new Deck("title", "author", "exernalId", 123L, 456L, false, language);
        firstTranslation = new Translation("label 1", false, "filename 1", 111L, "translatedText 1");
        secondTranslation = new Translation("label 2", false, "filename 2", 222L, "translatedText 2");
        Translation[] translations = new Translation[]{
                firstTranslation,
                secondTranslation
        };
        dictionary = new Dictionary("destLanguageIso", "language", translations, 789L, 123L);
        dictionaries = new Dictionary[]{dictionary};
    }

    @Test
    public void shouldWriteDeckMetadataToJson() throws ExportException, JSONException {
        JSONObject jsonObject = new JSONObject();
        txcExportUtility.writeDeckMetadataToJson(deck, EXPORTED_DECK_NAME, jsonObject);

        assertEquals(EXPORTED_DECK_NAME, jsonObject.getString(JsonKeys.DECK_LABEL));
        assertEquals(deck.getAuthor(), jsonObject.getString(JsonKeys.PUBLISHER));
        assertEquals(deck.getExternalId(), jsonObject.getString(JsonKeys.EXTERNAL_ID));
        assertEquals(deck.getTimestamp(), jsonObject.getLong(JsonKeys.TIMESTAMP));
        assertEquals(deck.getSourceLanguageIso(), jsonObject.getString(JsonKeys.SOURCE_LANGUAGE));
        assertEquals(deck.isLocked(), jsonObject.getBoolean(JsonKeys.LOCKED));
    }

    @Test
    public void shouldWriteDictionariesToJson() throws ExportException, JSONException {
        JSONObject jsonObject = new JSONObject();
        txcExportUtility.writeDictionariesToJson(
                dictionaries, new HashMap<String, Translation>(), jsonObject
        );

        JSONArray dictionariesArray = jsonObject.getJSONArray(JsonKeys.DICTIONARIES);
        JSONObject actualDictionary = dictionariesArray.getJSONObject(0);

        assertEquals(1, dictionariesArray.length());
        assertEquals(dictionary.getDestLanguageIso(), actualDictionary.getString(JsonKeys.DICTIONARY_DEST_ISO_CODE));
    }

    @Test
    public void shouldRetrieveIsoCodeForDictionariesWithEmptyIsoCode() throws ExportException, JSONException {
        JSONObject jsonObject = new JSONObject();
        Dictionary noIsoDictionary = new Dictionary("", "English", new Translation[0], 789L, 123L);
        txcExportUtility.writeDictionariesToJson(
                new Dictionary[]{noIsoDictionary}, new HashMap<String, Translation>(), jsonObject
        );

        JSONArray dictionariesArray = jsonObject.getJSONArray(JsonKeys.DICTIONARIES);
        JSONObject actualDictionary = dictionariesArray.getJSONObject(0);

        assertEquals(1, dictionariesArray.length());
        assertEquals("en", actualDictionary.getString(JsonKeys.DICTIONARY_DEST_ISO_CODE));
    }

    @Test
    public void shouldRetrieveIsoCodeForDictionariesWithNullIsoCode() throws ExportException, JSONException {
        JSONObject jsonObject = new JSONObject();
        Dictionary noIsoDictionary = new Dictionary(null, "English", new Translation[0], 789L, 123L);
        txcExportUtility.writeDictionariesToJson(
                new Dictionary[]{noIsoDictionary}, new HashMap<String, Translation>(), jsonObject
        );

        JSONArray dictionariesArray = jsonObject.getJSONArray(JsonKeys.DICTIONARIES);
        JSONObject actualDictionary = dictionariesArray.getJSONObject(0);

        assertEquals(1, dictionariesArray.length());
        assertEquals("en", actualDictionary.getString(JsonKeys.DICTIONARY_DEST_ISO_CODE));
    }

    @Test
    public void shouldWriteTranslationsToJson() throws ExportException, JSONException {
        JSONObject jsonObject = new JSONObject();
        HashMap<String, Translation> translationFilenames = new HashMap<>();

        txcExportUtility.writeTranslationsToJson(translationFilenames, dictionary, jsonObject);

        JSONArray cardsArray = jsonObject.getJSONArray(JsonKeys.CARDS);
        assertEquals(2, cardsArray.length());

        JSONObject firstCard = cardsArray.getJSONObject(0);
        assertEquals(firstTranslation.getLabel(), firstCard.getString(JsonKeys.CARD_LABEL));
        assertEquals(firstTranslation.getFilePath(), firstCard.getString(JsonKeys.CARD_DEST_AUDIO));
        assertEquals(firstTranslation.getTranslatedText(), firstCard.getString(JsonKeys.CARD_DEST_TEXT));

        JSONObject secondCard = cardsArray.getJSONObject(1);
        assertEquals(secondTranslation.getLabel(), secondCard.getString(JsonKeys.CARD_LABEL));
        assertEquals(secondTranslation.getFilePath(), secondCard.getString(JsonKeys.CARD_DEST_AUDIO));
        assertEquals(secondTranslation.getTranslatedText(), secondCard.getString(JsonKeys.CARD_DEST_TEXT));

        assertEquals(translationFilenames.get(firstTranslation.getFilePath()), firstTranslation);
        assertEquals(translationFilenames.get(secondTranslation.getFilePath()), secondTranslation);
    }
}