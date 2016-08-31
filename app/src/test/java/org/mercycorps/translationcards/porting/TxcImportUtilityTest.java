package org.mercycorps.translationcards.porting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.LanguageService;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TxcImportUtilityTest {

    private static final String DECK_LABEL = "label";
    private static final String PUBLISHER = "A Publisher";
    private static final String EXTERNAL_ID = "external";
    private static final long TIMESTAMP = 123456L;
    private static final String SOURCE_LANGUAGE = "fr";
    private static final String HASH = "someHash";
    public static final String DICTIONARIES_AS_JSON = "[{\"iso_code\":\"ar\",\"cards\":[{\"dest_txt\":\"ar translation 1\",\"card_label\":\"Do you understand this language?\",\"dest_audio\":\"GQ16-ar.mp3\"},{\"dest_txt\":\"ar translation 2\",\"card_label\":\"Can I talk to you, using this mobile application (App)?\",\"dest_audio\":\"BGQ1-ar.mp3\"}]},{\"iso_code\":\"ps\",\"cards\":[{\"dest_txt\":\"ps translation 1\",\"card_label\":\"Do you understand this language?\",\"dest_audio\":\"ps_file7.mp3\"},{\"dest_txt\":\"ps translation 2\",\"card_label\":\"Can I talk to you, using this mobile application (App)?\",\"dest_audio\":\"ps_file4.mp3\"}]},{\"iso_code\":\"fa\",\"cards\":[{\"dest_txt\":\"fa translation 1\",\"card_label\":\"Do you understand this language?\",\"dest_audio\":\"GQ16-fa.mp3\"},{\"dest_txt\":\"fa translation 2\",\"card_label\":\"Can I talk to you, using this mobile application (App)?\",\"dest_audio\":\"BGQ1-fa.mp3\"}]}]";
    public static final String DICTIONARY_JSON_NO_ISO_CODE = "[{\"iso_code\":\"\",\"cards\":[{\"dest_txt\":\"ar translation 1\",\"card_label\":\"Do you understand this language?\",\"dest_audio\":\"GQ16-ar.mp3\"},{\"dest_txt\":\"ar translation 2\",\"card_label\":\"Can I talk to you, using this mobile application (App)?\",\"dest_audio\":\"BGQ1-ar.mp3\"}]}]";
    public static final String DICTIONARY_JSON_EXTENDED_ISO_CODE = "[{\"iso_code\":\"ar_Arabic\",\"cards\":[{\"dest_txt\":\"ar translation 1\",\"card_label\":\"Do you understand this language?\",\"dest_audio\":\"GQ16-ar.mp3\"},{\"dest_txt\":\"ar translation 2\",\"card_label\":\"Can I talk to you, using this mobile application (App)?\",\"dest_audio\":\"BGQ1-ar.mp3\"}]}]";
    public static final String DICTIONARY_JSON_MISSING_AUDIO_FILENAME = "[{\"iso_code\":\"ar\",\"cards\":[{\"dest_txt\":\"ar translation 1\",\"card_label\":\"Do you understand this language?\",\"dest_audio\":\"GQ16-ar.mp3\"},{\"dest_txt\":\"ar translation 2\",\"card_label\":\"Can I talk to you, using this mobile application (App)?\",\"dest_audio\":\"\"}]}]";
    private TxcImportUtility txcImportUtility;
    private JSONObject jsonObjectToLoad;
    private File mockFile;
    private TranslationRepository mockTranslationRepository;

    @Before
    public void setUp() throws Exception {
        LanguageService mockLanguageService = mock(LanguageService.class);
        mockTranslationRepository = mock(TranslationRepository.class);
        txcImportUtility = new TxcImportUtility(
                mockLanguageService,
                mock(DeckRepository.class),
                mockTranslationRepository,
                mock(DictionaryRepository.class)
        );
        jsonObjectToLoad = new JSONObject();

        when(mockLanguageService.getLanguageDisplayName("ar")).thenReturn("Arabic");
        when(mockLanguageService.getLanguageDisplayName("ps")).thenReturn("Pashto");
        when(mockLanguageService.getLanguageDisplayName("fa")).thenReturn("Farsi");
        when(mockLanguageService.getLanguageDisplayName(LanguageService.INVALID_ISO_CODE))
                .thenReturn(LanguageService.INVALID_LANGUAGE_NAME);
        when(mockLanguageService.getIsoForLanguage("Arabic")).thenReturn("ar");
        when(mockLanguageService.getIsoForLanguage("Pashto")).thenReturn("ps");
        when(mockLanguageService.getIsoForLanguage("Farsi")).thenReturn("fa");
        mockFile = mock(File.class);
    }

    @Test
    public void shouldBuildImportSpecWithDefaultsAndDeckLabel() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertTrue(importSpec.dictionaries.isEmpty());
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);
    }

    @Test
    public void shouldBuildImportSpecWithPublisherWhenPresent() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        jsonObjectToLoad.put(JsonKeys.PUBLISHER, PUBLISHER);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals(PUBLISHER, importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertTrue(importSpec.dictionaries.isEmpty());
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);
    }

    @Test
    public void shouldBuildImportSpecWithExternalIdWhenPresent() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        jsonObjectToLoad.put(JsonKeys.EXTERNAL_ID, EXTERNAL_ID);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals(EXTERNAL_ID, importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertTrue(importSpec.dictionaries.isEmpty());
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);
    }

    @Test
    public void shouldBuildImportSpecWithTimestampWhenPresent() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        jsonObjectToLoad.put(JsonKeys.TIMESTAMP, TIMESTAMP);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(TIMESTAMP, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertTrue(importSpec.dictionaries.isEmpty());
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);
    }

    @Test
    public void shouldBuildImportSpecWithSourceLanguageWhenPresent() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        jsonObjectToLoad.put(JsonKeys.SOURCE_LANGUAGE, SOURCE_LANGUAGE);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("French", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertTrue(importSpec.dictionaries.isEmpty());
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);
    }

    @Test
    public void shouldBuildImportSpecWithLockSet() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        jsonObjectToLoad.put(JsonKeys.LOCKED, true);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertTrue(importSpec.locked);
        assertTrue(importSpec.dictionaries.isEmpty());
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);
    }

    @Test
    public void shouldBuildImportSpecWithDictionariesWhenPresent() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        JSONArray currentDictionaries = new JSONArray(DICTIONARIES_AS_JSON);
        jsonObjectToLoad.put(JsonKeys.DICTIONARIES, currentDictionaries);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertEquals(3, importSpec.dictionaries.size());
        assertEquals("ar", importSpec.dictionaries.get(0).isoCode);
        assertEquals("Arabic", importSpec.dictionaries.get(0).language);
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);

        for (TxcImportUtility.ImportSpecDictionary specDictionary : importSpec.dictionaries) {
            assertEquals(2, specDictionary.cards.size());
        }
    }

    @Test
    public void shouldBuildImportSpecWithDictionariesWithNoDestinationIsoCode() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        JSONArray currentDictionaries = new JSONArray(DICTIONARY_JSON_NO_ISO_CODE);
        jsonObjectToLoad.put(JsonKeys.DICTIONARIES, currentDictionaries);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertEquals(1, importSpec.dictionaries.size());
        assertEquals(LanguageService.INVALID_ISO_CODE, importSpec.dictionaries.get(0).isoCode);
        assertEquals(LanguageService.INVALID_LANGUAGE_NAME, importSpec.dictionaries.get(0).language);
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);

        for (TxcImportUtility.ImportSpecDictionary specDictionary : importSpec.dictionaries) {
            assertEquals(2, specDictionary.cards.size());
        }
    }

    @Test
    public void shouldBuildImportSpecForDictionariesWithExtendedIsoCodes() throws ImportException, JSONException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        JSONArray currentDictionaries = new JSONArray(DICTIONARY_JSON_EXTENDED_ISO_CODE);
        jsonObjectToLoad.put(JsonKeys.DICTIONARIES, currentDictionaries);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(mockFile, HASH, jsonObjectToLoad);

        assertEquals(DECK_LABEL, importSpec.label);
        assertEquals("", importSpec.publisher);
        assertEquals("", importSpec.externalId);
        assertEquals(-1L, importSpec.timestamp);
        assertEquals("English", importSpec.srcLanguage);
        assertFalse(importSpec.locked);
        assertEquals(1, importSpec.dictionaries.size());
        assertEquals("ar", importSpec.dictionaries.get(0).isoCode);
        assertEquals("Arabic", importSpec.dictionaries.get(0).language);
        assertEquals(mockFile, importSpec.dir);
        assertEquals(HASH, importSpec.hash);

        for (TxcImportUtility.ImportSpecDictionary specDictionary : importSpec.dictionaries) {
            assertEquals(2, specDictionary.cards.size());
        }
    }

    @Test
    public void shouldAddTranslationFromImportSpecToRepository() throws JSONException, ImportException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        JSONArray currentDictionaries = new JSONArray(DICTIONARIES_AS_JSON);
        jsonObjectToLoad.put(JsonKeys.DICTIONARIES, currentDictionaries);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(new File(""), HASH, jsonObjectToLoad);

        txcImportUtility.loadData(importSpec);
        TxcImportUtility.ImportSpecDictionary specDictionary = importSpec.dictionaries.get(0);
        TxcImportUtility.ImportSpecCard importSpecCard =
                specDictionary.cards.get(specDictionary.cards.size() - 1);

        String specAudioFilePath = new File(importSpec.dir, importSpecCard.filename).getAbsolutePath();
        verify(mockTranslationRepository).addTranslation(
                0L,
                importSpecCard.label,
                false,
                specAudioFilePath,
                specDictionary.cards.size() - 1,
                importSpecCard.translatedText
        );
    }

    @Test
    public void shouldAddTranslationWithNoAudioFromImportSpecToRepository() throws JSONException, ImportException {
        jsonObjectToLoad.put(JsonKeys.DECK_LABEL, DECK_LABEL);
        JSONArray currentDictionaries = new JSONArray(DICTIONARY_JSON_MISSING_AUDIO_FILENAME);
        jsonObjectToLoad.put(JsonKeys.DICTIONARIES, currentDictionaries);
        TxcImportUtility.ImportSpec importSpec =
                txcImportUtility.buildImportSpec(new File(""), HASH, jsonObjectToLoad);

        txcImportUtility.loadData(importSpec);
        TxcImportUtility.ImportSpecDictionary specDictionary = importSpec.dictionaries.get(0);
        TxcImportUtility.ImportSpecCard importSpecCard = specDictionary.cards.get(specDictionary.cards.size() - 1);

        verify(mockTranslationRepository).addTranslation(
                0L,
                importSpecCard.label,
                false,
                "",
                specDictionary.cards.size() - 1,
                importSpecCard.translatedText
        );
    }
}