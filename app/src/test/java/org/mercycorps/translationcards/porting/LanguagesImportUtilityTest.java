package org.mercycorps.translationcards.porting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.TestMainApplication;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LanguagesImportUtilityTest {

    @Test
    public void shouldParseFileCorrectly() throws IOException {
        Map<String, List<String>> expectedValues = new HashMap<>();
        expectedValues.put("aa", Arrays.asList("Afar", "One", "Two"));
        expectedValues.put("ab", Arrays.asList("Abkhazian", "Three"));


        InputStream inputStream = RuntimeEnvironment.application.getAssets().open("test_language_codes.json");
        LanguagesImportUtility languagesImportUtility = new LanguagesImportUtility(inputStream);

        Map<String, List<String>> actualValues = languagesImportUtility.loadLanguageMapFromFile();

        assertEquals(expectedValues, actualValues);
    }

}