package org.mercycorps.translationcards.activity.addDeck.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckTitleActivity;
import org.mercycorps.translationcards.activity.addDeck.GetStartedDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EnterDeckTitlePresenterTest {

    private static final String NO_DECK_TITLE = "";
    private static final String A_DECK_TITLE = "a deck title";
    private NewDeckContext newDeckContext;
    private EnterDeckTitlePresenter presenter;
    private EnterDeckTitleActivity activity;

    @Before
    public void setUp() throws Exception {
        newDeckContext = mock(NewDeckContext.class);
        activity = mock(EnterDeckTitleActivity.class);
        presenter = new EnterDeckTitlePresenter(activity, newDeckContext);
    }

    @Test
    public void shouldUpdateDeckTitleInput() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);
        
        presenter.updateDeckTitleInput();

        verify(activity).setDeckTitleText(A_DECK_TITLE);
    }

    @Test
    public void shouldInflateEnterDeckTitleImage() {
        presenter.inflateBitmaps();

        verify(activity).setActivityBitmap(R.id.enter_deck_title_image, R.drawable.enter_phrase_image);
    }

    @Test
    public void shouldStartGetStartedDeckActivityWhenBackButtonClicked() {
        presenter.backButtonClicked();

        verify(activity).startActivityWithClass(GetStartedDeckActivity.class);
    }

    @Test
    public void shouldStartEnterDeckSourceLanguageActivityWhenADeckTitleExists() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);

        presenter.nextButtonClicked();

        verify(activity).startActivityWithClass(EnterDeckSourceLanguageActivity.class);
    }

    @Test
    public void shouldNotStartEnterDeckSourceLanguageActivityWhenADeckTitleDoesNotExist() {
        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);

        presenter.nextButtonClicked();

        verify(activity, times(0)).startActivityWithClass(EnterDeckSourceLanguageActivity.class);
    }

    @Test
    public void shouldUpdateDeckTitleWhenDeckTextChanges() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);

        presenter.deckTitleInputChanged(A_DECK_TITLE);

        verify(newDeckContext).setDeckTitle(A_DECK_TITLE);
    }

    @Test
    public void shouldNotHaveNextButtonClickableWhenThereIsNoDeckTitle() {
        when(newDeckContext.getDeckTitle()).thenReturn(NO_DECK_TITLE);

        presenter.deckTitleInputChanged(NO_DECK_TITLE);

        verify(activity).updateNextButton(false, R.color.textDisabled, R.drawable.forward_arrow_disabled);
    }

    @Test
    public void shouldHaveNextButtonClickableWhenThereIsADeckTitle() {
        when(newDeckContext.getDeckTitle()).thenReturn(A_DECK_TITLE);

        presenter.deckTitleInputChanged(A_DECK_TITLE);

        verify(activity).updateNextButton(true, R.color.primaryTextColor, R.drawable.forward_arrow);
    }
}
