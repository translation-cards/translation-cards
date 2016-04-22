package org.mercycorps.translationcards.activity.addDeck;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NewDeckContextTest {

    private Deck deck;
    private Dictionary dictionary;
    private NewDeckContext newDeckContext;

    @Before
    public void setUp() throws Exception {
        deck = mock(Deck.class);
        dictionary = mock(Dictionary.class);
        newDeckContext = new NewDeckContext(deck, Collections.singletonList(dictionary));
    }

    @Test
    public void shouldSaveDeckWhenContextObjectIsSaved() {
        newDeckContext.save();

        verify(deck).save();
    }

    @Test
    public void shouldSaveDictionariesWhenContextObjectIsSaved() {
        newDeckContext.save();

        verify(dictionary).save(0);
    }

    @Test
    public void shouldAddDeckIdToDictionaryWhenContextObjectIsSaved() {
        long deckId = 1;
        when(deck.save()).thenReturn(deckId);
        newDeckContext.save();

        verify(dictionary).setDeckId(deckId);
    }
}