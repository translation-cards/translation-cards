package org.mercycorps.translationcards.service;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class TranslationServiceTest {

    @Mock
    TranslationRepository translationRepository;
    @Mock
    DictionaryService dictionaryService;

    Translation defaultTranslation;
    Translation noAudioTranslation;
    List<Translation> translationsFromRepository;

    TranslationService translationService;

    @Before
    public void setup() {
        initMocks(this);

        defaultTranslation = new Translation("label", true, "filename", 1L, "translated text");
        noAudioTranslation = new Translation("no audio label", true, "", 2L, "no audio translated text");
        translationsFromRepository = Arrays.asList(defaultTranslation, noAudioTranslation);

        when(translationRepository.getTranslationsForDictionary(any(Dictionary.class))).thenReturn(translationsFromRepository);

        translationService = new TranslationService(translationRepository, dictionaryService);
    }

    @Test
    public void getCurrentTranslations_shouldReturnAllCardsByDefault() {
        List<Translation> translations = translationService.getCurrentTranslations();

        assertEquals(translationsFromRepository, translations);
    }

    @Test
    public void deleteTranslation_shouldDeleteTranslationsBySourcePhraseFromCurrentDictionaries() {
        Dictionary dictionary = new Dictionary("", "", new Translation[]{defaultTranslation}, 0L);
        List<Dictionary> dictionaries = Collections.singletonList(dictionary);
        when(dictionaryService.getDictionariesForCurrentDeck()).thenReturn(dictionaries);

        translationService.deleteTranslation("label");

        verify(translationRepository).deleteTranslation(1L);
    }

    @Test
    public void allTranslationsShouldBeMinimizedByDefault() {
        for(int index = 0; index < translationService.getCurrentTranslations().size(); index++) {
            assertEquals(false, translationService.cardIsExpanded(index));
        }
    }

    @Test
    public void expandCard_shouldExpandACard() {
        translationService.expandCard(1);
        assertEquals(true, translationService.cardIsExpanded(1));
    }

    @Test
    public void minimizeCard_shouldMinimizeACard() {
        translationService.expandCard(1);
        translationService.minimizeCard(1);
        assertEquals(false, translationService.cardIsExpanded(1));
    }
}