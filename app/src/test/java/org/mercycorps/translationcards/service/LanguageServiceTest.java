package org.mercycorps.translationcards.service;

import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class LanguageServiceTest {

    @Test
    public void shouldReturnCorrectIsoCodeForArabic() throws Exception {
        LanguageService languageService = new LanguageService();
        assertEquals("fas", languageService.getIsoForLanguage("Persian"));
    }

    @Test
    public void shouldReturnCorrectIsoCodeForFarsi() throws Exception {
        LanguageService languageService = new LanguageService();
        assertEquals("fas", languageService.getIsoForLanguage("Farsi"));
    }

    @Test
    public void shouldReturnCorrectIsoCodeForUncapitalizedLanguageName() throws Exception {
        LanguageService languageService = new LanguageService();
        assertEquals( "fas", languageService.getIsoForLanguage("farsi"));
    }
}