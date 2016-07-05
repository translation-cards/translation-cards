package org.mercycorps.translationcards.service;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


public class LanguageServiceTest {

    private LanguageService languageService;

    @Before
    public void setup() {
        languageService = new LanguageService();
    }

    @Test
    public void shouldReturnCorrectIsoCodeForArabic() throws Exception {
        assertEquals("fas", languageService.getIsoForLanguage("Persian"));
    }

    @Test
    public void shouldReturnCorrectIsoCodeForFarsi() throws Exception {
        assertEquals("fas", languageService.getIsoForLanguage("Farsi"));
    }

    @Test
    public void shouldReturnCorrectIsoCodeForUncapitalizedLanguageName() throws Exception {
        assertEquals( "fas", languageService.getIsoForLanguage("farsi"));
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
        assertEquals(true, languageNames.contains("Southern Sotho"));
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