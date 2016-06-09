package org.mercycorps.translationcards.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by agarrard and natashaJimenez on 6/9/16.
 */

public class DeckServiceTest {

    private DeckService deckService;
    @Mock
    private Deck deck;
    @Mock
    private DbManager dbManager;

    @Before
    public void setup() {
        initMocks(this);
        when(dbManager.getAllDecks()).thenReturn(new Deck[]{deck});

        deckService = new DeckService(dbManager);
    }

    @Test
    public void shouldSaveDeckToDBWhenSaveIsCalled() {
        deckService.save(deck);

        verify(dbManager).addDeck(deck.getLabel(), deck.getAuthor(), deck.getTimestamp(), deck.getExternalId(), "", deck.isLocked(), deck.getSourceLanguageIso());
    }

}