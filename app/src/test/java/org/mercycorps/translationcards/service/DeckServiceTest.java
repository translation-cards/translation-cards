package org.mercycorps.translationcards.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;

import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by agarrard and natashaJimenez on 6/9/16.
 */

public class DeckServiceTest {

    private DeckService deckService;
    private Deck deck;
    private DeckRepository deckRepository;

    @Before
    public void setup() {
        deck = mock(Deck.class);
        LanguageService languageService = mock(LanguageService.class);
        deckRepository = mock(DeckRepository.class);
        DictionaryRepository dictionaryRepository = mock(DictionaryRepository.class);
        deckService = new DeckService(languageService, Collections.singletonList(deck), deckRepository, dictionaryRepository);
    }

    @Test
    public void shouldSaveDeckToDBWhenSaveIsCalled() {
        HashSet<String> destinationLanguages = new HashSet<>();
        destinationLanguages.add("Arabic");
        destinationLanguages.add("Farsi");

        deckService.save(deck, destinationLanguages);

        verify(deckRepository).addDeck(deck.getTitle(), deck.getAuthor(), deck.getTimestamp(), deck.getExternalId(), "", deck.isLocked(), deck.getSourceLanguageIso());
    }


    @Ignore
    @Test
    public void shouldSaveDictionariesWhenContextObjectIsSaved() {
        deckService.save(deck, Collections.EMPTY_SET);

//        verify(dictionary).save(0);
    }

    @Ignore
    @Test
    public void shouldAddDeckIdToDictionaryWhenContextObjectIsSaved() {
        long deckId = 1;

        deckService.save(deck, Collections.EMPTY_SET);

//        verify(dictionary).setDeckId(deckId);
    }

}