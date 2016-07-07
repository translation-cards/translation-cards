package org.mercycorps.translationcards.porting;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LanguagesImportUtilityTest {

    private LanguagesImportUtility languagesImportUtility;
    private Map<String, List<String>> expectedValues;

    @Before
    public void setup() throws IOException{
        expectedValues = new HashMap<>();
        expectedValues.put("aa", Arrays.asList("Afar", "One", "Two"));
        expectedValues.put("ab", Arrays.asList("Abkhazian", "Three"));
        expectedValues.put("es", Arrays.asList("Spanish"));

        InputStream inputStream = RuntimeEnvironment.application.getAssets().open("test_language_codes.json");
        languagesImportUtility = new LanguagesImportUtility(inputStream);
    }

    @Test
    public void shouldParseFileCorrectly() throws IOException {
        Map<String, List<String>> actualValues = languagesImportUtility.loadLanguageMapFromFile();

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void shouldRemoveCharactersAfterTwoLetterIsoCode() throws IOException {
        Map<String, List<String>> actualValues = languagesImportUtility.loadLanguageMapFromFile();

        assertEquals(expectedValues, actualValues);
    }

}