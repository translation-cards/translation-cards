package org.mercycorps.translationcards.activity.addDeck;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class NewDeckContextTest {

    private Deck deck;
    private Dictionary dictionary;
    private NewDeckContext newDeckContext;

    @Before
    public void setUp() throws Exception {
        deck = mock(Deck.class);
        dictionary = mock(Dictionary.class);
        newDeckContext = new NewDeckContext(deck, "", false);
    }

    @Test
    public void shouldSaveDeckWhenContextObjectIsSaved() {
        newDeckContext.save();

        verify(deck).save();
    }

    @Ignore
    @Test
    public void shouldSaveDictionariesWhenContextObjectIsSaved() {
        newDeckContext.save();

        verify(dictionary).save(0);
    }

    @Ignore
    @Test
    public void shouldAddDeckIdToDictionaryWhenContextObjectIsSaved() {
        long deckId = 1;
        when(deck.save()).thenReturn(deckId);
        newDeckContext.save();

        verify(dictionary).setDeckId(deckId);
    }
}