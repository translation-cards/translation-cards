package org.mercycorps.translationcards.model;

import org.junit.Test;
import org.mercycorps.translationcards.data.Deck;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Test for Deck
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
public class DeckTest {
    @Test
    public void getCreationDate_shouldFormatCreationDate() {
        long aDate = 1454946439262L;
        Deck deck = new Deck("", "", "", -1, aDate, false);

        assertThat(deck.getCreationDateString(), is("02/08/16"));
    }
}