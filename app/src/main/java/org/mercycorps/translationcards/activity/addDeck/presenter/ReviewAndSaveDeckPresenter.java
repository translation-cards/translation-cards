package org.mercycorps.translationcards.activity.addDeck.presenter;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.EnterAuthorActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReviewAndSaveDeckPresenter {
    private static final String DELIMITER = "  ";

    private ReviewAndSaveDeckView view;
    private NewDeckContext contextFromIntent;
    private DeckRepository deckRepository;

    public ReviewAndSaveDeckPresenter(ReviewAndSaveDeckView view, NewDeckContext contextFromIntent, DeckRepository deckRepository) {
        this.view = view;
        this.contextFromIntent = contextFromIntent;
        this.deckRepository = deckRepository;
    }

    public void backButtonClicked() {
        view.startActivityWithClass(EnterAuthorActivity.class);
    }

    public void inflateBitmap() {
        view.setActivityBitmap(R.id.enter_source_language_image, R.drawable.enter_source_language_image);
    }

    public void saveButtonClicked() {
        deckRepository.saveDeck(contextFromIntent.getDeck(), contextFromIntent.getDestinationLanguages());
        view.finishAddDeckFlow();
    }

    public void initializeView() {
        view.setDeckItem(contextFromIntent.getDeck());
        fillLanguagesList();
    }

    private void fillLanguagesList() {
        List<String> destinationLanguages = new ArrayList<>(contextFromIntent.getDestinationLanguages());
        Collections.sort(destinationLanguages);
        view.fillLanguageList(formatDestinationLangauges(destinationLanguages));
    }

    private String formatDestinationLangauges(List<String> destinationLanguages) {
        boolean firstTime = true;
        StringBuilder formattedLanguages = new StringBuilder("");
        for (String destinationLanguage : destinationLanguages) {
            if (firstTime) {
                firstTime = false;
            } else {
                formattedLanguages.append(DELIMITER);
            }
            formattedLanguages.append(destinationLanguage.toUpperCase());
        }
        return formattedLanguages.toString();
    }

    public interface ReviewAndSaveDeckView extends AddDeckView {
        void finishAddDeckFlow();
        void setDeckItem(Deck deck);
        void fillLanguageList(String formattedLanguages);
    }
}
