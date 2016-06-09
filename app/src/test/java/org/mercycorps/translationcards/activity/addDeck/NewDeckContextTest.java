package org.mercycorps.translationcards.activity.addDeck;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.service.DeckService;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class NewDeckContextTest {

    @Mock
    private Deck deck;
    private DeckService deckService = ((TestMainApplication) RuntimeEnvironment.application).getDeckService();
    @Mock
    private Dictionary dictionary;
    private NewDeckContext newDeckContext;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        newDeckContext = new NewDeckContext(deck, "", false);
    }

    @Test
    public void shouldSaveDeckWhenContextObjectIsSaved() {
        newDeckContext.save();

        verify(deckService).save(deck);
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
        when(deckService.save(deck)).thenReturn(deckId);
        newDeckContext.save();

        verify(dictionary).setDeckId(deckId);
    }
}