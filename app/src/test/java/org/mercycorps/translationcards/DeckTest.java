package org.mercycorps.translationcards;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class DeckTest {
    @Test
    public void getCreationDate_shouldFormatCreationDate() {
        long aDate = 1454946439262L;
        Deck deck = new Deck("", "", "", -1, aDate);

        assertThat(deck.getCreationDateString(), is("02/08/16"));
    }
}