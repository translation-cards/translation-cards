package org.mercycorps.translationcards.viewModel;

import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckTitleActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EnterDeckTitlePresenterTest {

    private static final String NO_DECK_TITLE = "";
    private static final String A_DECK_TITLE = "a deck title";
    private NewDeckContext newDeckContext;
    private EnterDeckTitlePresenter presenter;
    private TextView deckTitleInput;

    @Before
    public void setUp() throws Exception {
        newDeckContext = mock(NewDeckContext.class);
        deckTitleInput = mock(TextView.class);
        presenter = new EnterDeckTitlePresenter(mock(EnterDeckTitleActivity.class), newDeckContext, deckTitleInput);
    }

    @Test
    public void shouldUpdateDeckTitleInput() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);
        
        presenter.updateDeckTitleInput();

        verify(deckTitleInput).setText(A_DECK_TITLE);
    }


    //    All this stuff is now private in our presenter because it hides the view logic it does, just updated the objects passed to it...
//
//    @Test
//    public void nextButtonShouldBeClickableWhenThereIsADeckTitle() {
//        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);
//
//        boolean result = viewModel.isNextButtonClickable();
//
//        assertTrue(result);
//    }
//
//    @Test
//    public void nextButtonShouldNotBeClickableWhenThereIsNotADeckTitle() {
//        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);
//
//        boolean result = viewModel.isNextButtonClickable();
//
//        assertFalse(result);
//    }
//
//    @Test
//    public void shouldReturnTextDisabledColorWhenNoDeckTitleIsSaved() {
//        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);
//
//        assertEquals(R.color.textDisabled, viewModel.getTextColor());
//    }
//
//    @Test
//    public void shouldReturnPrimaryTextColorWhenDeckTitleExists() {
//        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);
//
//        assertEquals(R.color.primaryTextColor, viewModel.getTextColor());
//    }
//
//    @Test
//    public void shouldReturnDisabledNextArrowWhenNoDeckTitleExists() {
//        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);
//
//        assertEquals(R.drawable.forward_arrow_disabled, viewModel.getNextArrow());
//    }
//
//    @Test
//    public void shouldReturnNextArrowWhenDeckTitleExists() {
//        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);
//
//        assertEquals(R.drawable.forward_arrow, viewModel.getNextArrow());
//    }
}