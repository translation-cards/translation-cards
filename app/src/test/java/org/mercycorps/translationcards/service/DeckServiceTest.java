package org.mercycorps.translationcards.service;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.DeckRepository;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by agarrard and natashaJimenez on 6/9/16.
 */

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DeckServiceTest {

    private DeckService deckService;
    @Mock
    private Deck deck;
    @Mock
    private DbManager dbManager;

    private LanguageService languageService = ((TestMainApplication) RuntimeEnvironment.application).getLanguageService();
    private DeckRepository deckRepository = ((TestMainApplication) RuntimeEnvironment.application).getDeckRepository();

    @Before
    public void setup() {
        initMocks(this);
        deckService = new DeckService(dbManager, languageService, Arrays.asList(deck), deckRepository);
    }

    @Test
    public void shouldSaveDeckToDBWhenSaveIsCalled() {
        deckService.save(deck, "Arabic, Farsi");

        verify(deckRepository).addDeck(deck.getTitle(), deck.getAuthor(), deck.getTimestamp(), deck.getExternalId(), "", deck.isLocked(), deck.getSourceLanguageIso());
    }


    @Ignore
    @Test
    public void shouldSaveDictionariesWhenContextObjectIsSaved() {
        deckService.save(deck, "");

//        verify(dictionary).save(0);
    }

    @Ignore
    @Test
    public void shouldAddDeckIdToDictionaryWhenContextObjectIsSaved() {
        long deckId = 1;

        deckService.save(deck, "");

//        verify(dictionary).setDeckId(deckId);
    }

}