package org.mercycorps.translationcards.viewModel;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EnterDeckTitleViewModelTest {

    private static final String NO_DECK_TITLE = "";
    private static final String A_DECK_TITLE = "a deck title";
    private NewDeckContext newDeckContext;
    private EnterDeckTitleViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        newDeckContext = mock(NewDeckContext.class);
        viewModel = new EnterDeckTitleViewModel(newDeckContext);
    }

    @Test
    public void shouldGetDeckTitleFromContextObject() {
        viewModel.getDeckTitle();

        verify(newDeckContext).getDeckTitle();
    }

    @Test
    public void shouldUpdateNewDeckContextWhenSavingDeckTitle() {
        viewModel.saveDeckTitle(A_DECK_TITLE);

        verify(newDeckContext).setDeckTitle(A_DECK_TITLE);
    }

    @Test
    public void nextButtonShouldBeClickableWhenThereIsADeckTitle() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);

        boolean result = viewModel.isNextButtonClickable();

        assertTrue(result);
    }

    @Test
    public void nextButtonShouldNotBeClickableWhenThereIsNotADeckTitle() {
        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);

        boolean result = viewModel.isNextButtonClickable();

        assertFalse(result);
    }

    @Test
    public void shouldReturnTextDisabledColorWhenNoDeckTitleIsSaved() {
        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);

        assertEquals(R.color.textDisabled, viewModel.getTextColor());
    }

    @Test
    public void shouldReturnPrimaryTextColorWhenDeckTitleExists() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);

        assertEquals(R.color.primaryTextColor, viewModel.getTextColor());
    }

    @Test
    public void shouldReturnDisabledNextArrowWhenNoDeckTitleExists() {
        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);

        assertEquals(R.drawable.forward_arrow_disabled, viewModel.getNextArrow());
    }

    @Test
    public void shouldReturnNextArrowWhenDeckTitleExists() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);

        assertEquals(R.drawable.forward_arrow, viewModel.getNextArrow());
    }
}