package org.mercycorps.translationcards.activity.addDeck.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckDestinationLanguagesActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.activity.addDeck.ReviewAndSaveDeckActivity;

import static org.mercycorps.translationcards.activity.addDeck.presenter.EnterAuthorPresenter.EnterAuthorView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class EnterAuthorPresenterTest {

    private EnterAuthorView activity;
    private EnterAuthorPresenter presenter;
    private NewDeckContext newDeckContext;
    private static final String AN_AUTHOR = "An Author";
    private static final String EMPTY_DECK_AUTHOR = "";

    @Before
    public void setUp() throws Exception {
        activity = mock(EnterAuthorView.class);
        newDeckContext = mock(NewDeckContext.class);
        presenter = new EnterAuthorPresenter(activity, newDeckContext);
    }

    @Test
    public void shouldInflateBitmaps() {
        presenter.inflateBitmaps();

        verify(activity).setActivityBitmap(R.id.deck_author_image, R.drawable.enter_phrase_image);
    }

    @Test
    public void shouldUpdateViewWithAuthor() {
        when(newDeckContext.getAuthor()).thenReturn(AN_AUTHOR);

        presenter.initDeckAuthorInput();

        verify(activity).updateDeckAuthorInput(AN_AUTHOR);
    }

    @Test
    public void shouldUpdateContextWithAuthorAndStartNextActivityWhenNextButtonIsClicked() {
        when(newDeckContext.getAuthor()).thenReturn(AN_AUTHOR);

        presenter.nextButtonClicked();

        verify(activity).startActivityWithClass(ReviewAndSaveDeckActivity.class);
    }

    @Test
    public void shouldNotUpdateContextOrStartActivityWhenNextButtonIsClickedWithAnEmptyAuthor() {
        when(newDeckContext.getAuthor()).thenReturn(EMPTY_DECK_AUTHOR);

        presenter.nextButtonClicked();

        verifyZeroInteractions(activity);
    }

    @Test
    public void shouldSaveDeckAuthorToContextAndStartPreviousActivityWhenBackButtonIsClicked() {
        presenter.backButtonClicked();

        verify(activity).startActivityWithClass(EnterDeckDestinationLanguagesActivity.class);
    }

    @Test
    public void shouldDisableNextButtonWhenNoDeckAuthorEntered() {
        presenter.deckAuthorInputChanged(EMPTY_DECK_AUTHOR);

        verify(activity).updateNextButtonClickable(false, R.color.textDisabled, R.drawable.forward_arrow_disabled);
    }

    @Test
    public void shouldEnableNextButtonWhenDeckAuthorEntered() {
        presenter.deckAuthorInputChanged(AN_AUTHOR);

        verify(activity).updateNextButtonClickable(true, R.color.primaryTextColor, R.drawable.forward_arrow);
    }

    @Test
    public void shouldUpdateNewDeckContextWhenDeckAuthorInputChanges() {
        presenter.deckAuthorInputChanged(AN_AUTHOR);

        verify(newDeckContext).setAuthor(AN_AUTHOR);
    }
}