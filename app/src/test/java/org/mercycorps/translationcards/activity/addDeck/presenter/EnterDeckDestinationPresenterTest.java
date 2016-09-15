package org.mercycorps.translationcards.activity.addDeck.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.activity.addDeck.presenter.EnterDeckDestinationPresenter.EnterDeckDestinationView;

import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EnterDeckDestinationPresenterTest {
    private EnterDeckDestinationPresenter presenter;
    private EnterDeckDestinationView view;
    private NewDeckContext newDeckContext;

    @Before
    public void setup() {
        view = mock(EnterDeckDestinationView.class);
        newDeckContext = mock(NewDeckContext.class);
        presenter = new EnterDeckDestinationPresenter(view, newDeckContext);
    }
    @Test
    public void shouldSetBitMap() {
        presenter.setBitmaps();

        verify(view).setActivityBitmap(R.id.enter_deck_destination_image, R.drawable.enter_phrase_image);
    }

    @Test
    public void shouldSetNextButtonClickableWhenDestinationLanguagesExist() {
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add("Arabic");
        }};
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);

        presenter.updateNextButtonState();

        verify(view).updateNextButton(true, R.color.primaryTextColor, R.drawable.forward_arrow);
    }

    @Test
    public void shouldSetNextButtonToNotClickableWhenNoDestinationLanguagesArePresent() {
        when(newDeckContext.getDestinationLanguages()).thenReturn(new HashSet<String>());

        presenter.updateNextButtonState();

        verify(view).updateNextButton(false, R.color.textDisabled, R.drawable.forward_arrow_disabled);
    }
}