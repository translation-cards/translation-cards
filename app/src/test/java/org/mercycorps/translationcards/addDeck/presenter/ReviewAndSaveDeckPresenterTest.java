package org.mercycorps.translationcards.addDeck.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.activity.EnterAuthorActivity;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.presenter.ReviewAndSaveDeckPresenter;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;

import java.util.HashSet;

import static org.mercycorps.translationcards.addDeck.presenter.ReviewAndSaveDeckPresenter.ReviewAndSaveDeckView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReviewAndSaveDeckPresenterTest {

    private static final String A_LANGUAGE = "a language";
    private ReviewAndSaveDeckView view;
    private ReviewAndSaveDeckPresenter presenter;
    private NewDeckContext newDeckContext;
    private DeckRepository deckRepository;

    @Before
    public void setUp() throws Exception {
        view = mock(ReviewAndSaveDeckView.class);
        newDeckContext = mock(NewDeckContext.class);
        deckRepository = mock(DeckRepository.class);
        presenter = new ReviewAndSaveDeckPresenter(view, newDeckContext, deckRepository);
    }

    @Test
    public void shouldStartEnterAuthorActivityWhenBackButtonIsClicked() {
        presenter.backButtonClicked();

        verify(view).startActivityWithClass(EnterAuthorActivity.class);
    }

    @Test
    public void shouldInflateBitmapsForActivity() {
        presenter.inflateBitmap();

        verify(view).setActivityBitmap(R.id.enter_source_language_image, R.drawable.enter_source_language_image);
    }

    @Test
    public void shouldSaveDeckWhenSaveButtonIsClicked() {
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add(A_LANGUAGE);
        }};
        Deck aDeck = mock(Deck.class);
        when(newDeckContext.getDeck()).thenReturn(aDeck);
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);

        presenter.saveButtonClicked();

        verify(deckRepository).saveDeck(aDeck, destinationLanguages);
        verify(view).finishAddDeckFlow();
    }

    @Test
    public void shouldSetDeckItemWhenViewIsInitialized() {
        Deck aDeck = mock(Deck.class);
        when(newDeckContext.getDeck()).thenReturn(aDeck);

        presenter.initializeView();

        verify(view).setDeckItem(aDeck);
    }

    @Test
    public void shouldFormatAndFillDestinationLanguagesWhenViewIsInitialized() {
        final String someLanguage = "some other language";
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add(someLanguage);
            add(A_LANGUAGE);
        }};
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);

        presenter.initializeView();

        verify(view).fillLanguageList(A_LANGUAGE.toUpperCase() + "  " + someLanguage.toUpperCase());
    }
}