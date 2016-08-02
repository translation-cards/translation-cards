package org.mercycorps.translationcards.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.porting.JsonKeys;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DictionaryTest {

    @Test
    public void shouldReturnMapOfAudioPathsToAssetBoolean() {
        Translation firstTranslation = new Translation("", false, "/filename1", 1L, "");
        Translation secondTranslation = new Translation("", true, "/filename2", 1L, "");
        Dictionary dictionary = new Dictionary("", "", new Translation[]{firstTranslation, secondTranslation}, 1L, 1L);

        Map<String, Boolean> audioPaths = dictionary.getAudioPaths();

        assertEquals(2, audioPaths.size());
        assertTrue(audioPaths.containsKey("/filename1"));
        assertTrue(audioPaths.containsKey("/filename2"));
        assertFalse(audioPaths.get("/filename1"));
        assertTrue(audioPaths.get("/filename2"));
    }

    @Test
    public void shouldNotReturnTranslationPathsWithNoAudio() {
        Translation firstTranslation = new Translation("", false, "/filename1", 1L, "");
        Translation secondTranslation = new Translation("", false, "", 1L, "");
        Dictionary dictionary = new Dictionary("", "", new Translation[]{firstTranslation, secondTranslation}, 1L, 1L);

        Map<String, Boolean> audioPaths = dictionary.getAudioPaths();

        assertEquals(1, audioPaths.size());
        assertTrue(audioPaths.containsKey("/filename1"));
    }

    @Test
    public void shouldReturnJSONObjectWithDictionaryMetadata() throws JSONException {
        Dictionary dictionary = new Dictionary("destinationIso", "language", new Translation[]{}, 1L, 1L);

        JSONObject json = dictionary.toJSON();

        assertEquals(dictionary.getDestLanguageIso(), json.getString(JsonKeys.DICTIONARY_DEST_ISO_CODE));
    }

    @Test
    public void shouldWriteTranslationsToJSON() throws JSONException {
        Translation firstTranslation = mock(Translation.class);
        Translation secondTranslation = mock(Translation.class);
        Dictionary dictionary = new Dictionary("", "", new Translation[]{firstTranslation, secondTranslation}, 1L, 1L);
        JSONObject firstTranslationAsJSON = new JSONObject();
        JSONObject secondTranslationAsJSON = new JSONObject();
        when(firstTranslation.toJSON()).thenReturn(firstTranslationAsJSON);
        when(secondTranslation.toJSON()).thenReturn(secondTranslationAsJSON);

        JSONObject jsonObject = dictionary.toJSON();

        JSONArray cardsArray = jsonObject.getJSONArray(JsonKeys.CARDS);
        assertEquals(2, cardsArray.length());
        assertEquals(firstTranslationAsJSON, cardsArray.getJSONObject(0));
        assertEquals(secondTranslationAsJSON, cardsArray.getJSONObject(1));
    }
}