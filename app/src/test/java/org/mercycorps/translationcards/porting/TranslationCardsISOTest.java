package org.mercycorps.translationcards.porting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationCardsISOTest {

    @Test
    public void shouldFetchFarsiLanguageNameForFarsiISOCode() {
        assertEquals("Farsi", TranslationCardsISO.getLanguageDisplayName("fa", ""));
    }

    @Test
    public void shouldFetchPersianLanguageNameForFAIsoCodeAndPersianLanguageSpecifier() {
        assertEquals("Persian", TranslationCardsISO.getLanguageDisplayName("fa", "persian"));
    }

    @Test
    public void shouldFetchFAIsoCodeForPersianLanguageName() {
        assertEquals("fa", TranslationCardsISO.getISOCodeForLanguage("Persian"));
    }

    @Test
    public void shouldFetchFAIsoCodeForFarsiLanguageName() {
        assertEquals("fa", TranslationCardsISO.getISOCodeForLanguage("Farsi"));
    }

    @Test
    public void shouldGetYIIsoCodeForYiddishLanguageName() {
        assertEquals("yi", TranslationCardsISO.getISOCodeForLanguage("Yiddish"));
    }
}