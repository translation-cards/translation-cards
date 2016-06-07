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
        assertEquals(languageService.getIsoForLanguage("Persian"), "fas");
    }
}