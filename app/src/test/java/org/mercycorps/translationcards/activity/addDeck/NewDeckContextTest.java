package org.mercycorps.translationcards.activity.addDeck;

import org.junit.Test;

import static org.junit.Assert.*;

public class NewDeckContextTest {

    @Test
    public void shouldConvertDestinationLanguageSetToCommaSeparatedString() {
        NewDeckContext newDeckContext = new NewDeckContext(null, false);
        newDeckContext.addDestinationLanguage("French");
        newDeckContext.addDestinationLanguage("Chinese");
        newDeckContext.addDestinationLanguage("Spanish");

        assertEquals("French, Chinese, Spanish", newDeckContext.getLanguagesInput());
    }

}