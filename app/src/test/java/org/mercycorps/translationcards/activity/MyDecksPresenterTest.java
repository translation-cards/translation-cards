package org.mercycorps.translationcards.activity;

import android.support.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;

import java.util.ArrayList;
import java.util.List;

import static org.mercycorps.translationcards.activity.MyDecksPresenter.MyDecksView;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MyDecksPresenterTest {

    private static final int IS_NOT_CENTERED = 0;
    private static final int IS_CENTERED = -1;
    private MyDecksView view;
    private DeckRepository deckRepository;
    private MyDecksPresenter myDecksPresenter;

    @Before
    public void setUp() throws Exception {
        view = mock(MyDecksView.class);
        deckRepository = mock(DeckRepository.class);
    }

    @Test
    public void shouldShowEmptyMyDecksViewWhenNoDecksArePresent() {
        MyDecksPresenter myDecksPresenter = new MyDecksPresenter(view, deckRepository);

        myDecksPresenter.refreshListFooter();

        verify(view).emptyViewState();
        verify(view).updateMyDeckListCentered(IS_CENTERED);
    }

    @Test
    public void shouldShowMyDecksViewWhenDecksArePresent() {
        ArrayList<Deck> decks = listWithOneDeck();
        when(deckRepository.getAllDecks()).thenReturn(decks);
        MyDecksPresenter myDecksPresenter = new MyDecksPresenter(view, deckRepository);

        myDecksPresenter.refreshListFooter();

        verify(view).nonEmptyViewState();
        verify(view).updateMyDeckListCentered(IS_NOT_CENTERED);
    }

    @Test
    public void shouldRefreshMyDecksList() {
        List<Deck> decks = listWithOneDeck();
        when(deckRepository.getAllDecks()).thenReturn(decks);
        MyDecksPresenter myDecksPresenter = new MyDecksPresenter(view, deckRepository);

        myDecksPresenter.refreshMyDecksList();

        verify(deckRepository, times(2)).getAllDecks();
        verify(view).updateMyDecksList(decks);
        verify(view).nonEmptyViewState();
        verify(view).updateMyDeckListCentered(IS_NOT_CENTERED);
    }


    @NonNull
    private ArrayList<Deck> listWithOneDeck() {
        return new ArrayList<Deck>() {{
            add(new Deck());
        }};
    }
}