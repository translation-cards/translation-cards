package org.mercycorps.translationcards.porting;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LanguagesImportUtilityTest {

    private static LanguagesImportUtility languagesImportUtility;
    private Map<String, List<String>> expectedValues;

    @BeforeClass
    public static void oneTimeSetup() throws IOException {
        File inputFile = new File("./src/test/assets/test_language_codes.json");
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        languagesImportUtility = new LanguagesImportUtility(fileInputStream);
        fileInputStream.close();
    }

    @Before
    public void setup() throws IOException {
        expectedValues = new HashMap<>();
        expectedValues.put("aa", Arrays.asList("Afar", "One", "Two"));
        expectedValues.put("ab", Arrays.asList("Abkhazian", "Three"));
        expectedValues.put("es", Arrays.asList("Spanish"));
    }

    @Test
    public void shouldParseFileCorrectly() throws IOException {
        Map<String, List<String>> actualValues = languagesImportUtility.getLanguageMap();

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void shouldRemoveCharactersAfterTwoLetterIsoCode() throws IOException {
        Map<String, List<String>> actualValues = languagesImportUtility.getLanguageMap();

        assertEquals(expectedValues, actualValues);
    }

    @Test
    public void shouldReturnEmptyMapIfInputStreamIsNull() {
        LanguagesImportUtility emptyLanguagesImportUtility = new LanguagesImportUtility(null);

        assertEquals(Collections.EMPTY_MAP, emptyLanguagesImportUtility.getLanguageMap());
    }
}