package org.mercycorps.translationcards.addDeck.presenter;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.activity.EnterAuthorActivity;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.addDeck.activity.LanguageSelectorActivity;
import org.mercycorps.translationcards.addDeck.NewDeckContext;

public class EnterDeckDestinationPresenter {
    private static final int REQUEST_CODE = 0;

    private final EnterDeckDestinationView view;
    private final NewDeckContext contextFromIntent;

    public EnterDeckDestinationPresenter(EnterDeckDestinationView view, NewDeckContext contextFromIntent) {
        this.view = view;
        this.contextFromIntent = contextFromIntent;
    }

    public void setBitmaps() {
        view.setActivityBitmap(R.id.enter_deck_destination_image, R.drawable.enter_phrase_image);
    }


    private void updateNextButtonState() {
        boolean hasDestinationLanguages = !contextFromIntent.getDestinationLanguages().isEmpty();
        if (hasDestinationLanguages) {
            view.enableNextButton();
        } else {
            view.disableNextButton();
        }
    }

    private void populateLanguageChips() {
        view.removeAllLanguageChips();
        for (String language : contextFromIntent.getDestinationLanguages()) {
            view.addLanguageChip(language);
        }
    }

    public void deleteLanguage(String language) {
        contextFromIntent.getDestinationLanguages().remove(language);
        refreshView();
    }

    public void newLanguageSelected(String selectedLanguage) {
        if (selectedLanguage != null) {
            contextFromIntent.addDestinationLanguage(selectedLanguage);
            view.addLanguageChip(selectedLanguage);
            updateNextButtonState();
        }
    }

    public void nextButtonClicked() {
        if (!contextFromIntent.getDestinationLanguages().isEmpty()) {
            view.startActivityWithClass(EnterAuthorActivity.class);
        }
    }

    public void backButtonClicked() {
        view.startActivityWithClass(EnterDeckSourceLanguageActivity.class);
    }

    public void refreshView() {
        updateNextButtonState();
        populateLanguageChips();
    }

    public void addNewDestinationLanguageClicked() {
        view.startActivityForResult(LanguageSelectorActivity.class, REQUEST_CODE);
    }

    public interface EnterDeckDestinationView extends AddDeckView {
        void enableNextButton();
        void disableNextButton();
        void addLanguageChip(String language);
        void removeAllLanguageChips();
    }
}
