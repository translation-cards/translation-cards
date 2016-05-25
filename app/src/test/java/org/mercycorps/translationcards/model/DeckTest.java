package org.mercycorps.translationcards.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.data.Deck;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DeckTest {
    private Deck deck;

    @Before
    public void setUp() throws Exception {
        deck = new Deck("", "", "", -1, 1454946439262L, false, "");
    }

    @Test
    public void getCreationDate_shouldFormatCreationDate() {
        assertThat(deck.getCreationDateString(), is("02/08/16"));
    }

    @Test
    public void shouldDeleteDeckFromDBWhenDeleteIsCalled() {
        deck.delete();

        verify(((MainApplication)MainApplication.getContextFromMainApp()).getDbManager()).deleteDeck(deck.getDbId());
    }

    @Test
    public void shouldSaveDeckToDBWhenSaveIsCalled() {
        deck.save();

        verify(((MainApplication)MainApplication.getContextFromMainApp()).getDbManager()).addDeck(deck.getLabel(), deck.getAuthor(), deck.getTimestamp(), deck.getExternalId(), "", deck.isLocked(), deck.getSourceLanguageIso());
    }
}