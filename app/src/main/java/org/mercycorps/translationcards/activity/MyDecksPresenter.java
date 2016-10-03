package org.mercycorps.translationcards.activity;

import android.widget.RelativeLayout;

import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;

import java.util.List;

public class MyDecksPresenter {
    private static final int IS_NOT_CENTERED = 0;
    private static final int IS_CENTERED = RelativeLayout.TRUE;
    private MyDecksView view;
    private DeckRepository deckRepository;
    private List<Deck> decks;

    public MyDecksPresenter(MyDecksView view, DeckRepository deckRepository) {
        this.view = view;
        this.deckRepository = deckRepository;
        decks = deckRepository.getAllDecks();
    }

    public void refreshListFooter() {
        if (decks.isEmpty()) {
            view.emptyViewState();
            view.updateMyDeckListCentered(IS_CENTERED);
        } else {
            view.nonEmptyViewState();
            view.updateMyDeckListCentered(IS_NOT_CENTERED);
        }
    }

    public void refreshMyDecksList() {
        decks = deckRepository.getAllDecks();
        view.updateMyDecksList(decks);
        refreshListFooter();
    }

    public List<Deck> getDecks() {
        return decks;
    }

    interface MyDecksView {
        void emptyViewState();
        void nonEmptyViewState();
        void updateMyDeckListCentered(int isCentered);
        void updateMyDecksList(List<Deck> decks);
    }
}
