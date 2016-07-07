package org.mercycorps.translationcards.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.porting.LanguagesImportUtility;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class LanguageServiceTest {

    private LanguageService languageService;
    private LanguagesImportUtility languagesImportUtility;

    @Before
    public void setup() throws IOException {
        InputStream inputStream = RuntimeEnvironment.application.getAssets().open("language_codes.json");
        languagesImportUtility = new LanguagesImportUtility(inputStream);
        languageService = new LanguageService(languagesImportUtility);
    }

    @Test
    public void shouldReturnCorrectIsoCodeForArabic() throws Exception {
        assertEquals("fa", languageService.getIsoForLanguage("Persian"));
    }

    @Test
    public void shouldReturnCorrectIsoCodeForFarsi() throws Exception {
        assertEquals("fa", languageService.getIsoForLanguage("Farsi"));
    }

    @Test
    public void shouldReturnCorrectIsoCodeForUncapitalizedLanguageName() throws Exception {
        assertEquals( "fa", languageService.getIsoForLanguage("farsi"));
    }

    @Test
    public void shouldReturnInvalidLanguageFlagForEmptyString() throws Exception {
        assertEquals(LanguageService.INVALID_LANGUAGE, languageService.getIsoForLanguage(""));
    }

    @Test
    public void shouldReturnListOfAllLanguageNames() {
        List<String> languageNames = languageService.getLanguageNames();

        assertEquals(true, languageNames.contains("Farsi"));
        assertEquals(true, languageNames.contains("Arabic"));
        assertEquals(true, languageNames.contains("English"));
        assertEquals(true, languageNames.contains("Persian"));
    }

    @Test
    public void shouldReturnATitleCasedString() {
        String titleCasedString = LanguageService.getTitleCaseName("A title cased string");
        assertEquals("A Title Cased String", titleCasedString);
    }

    @Test
    public void shouldReturnEmptyStringIfNullPassedToGetTitleCaseName() {
        assertEquals("", LanguageService.getTitleCaseName(null));
    }
}