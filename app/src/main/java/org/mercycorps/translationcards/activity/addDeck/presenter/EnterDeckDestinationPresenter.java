package org.mercycorps.translationcards.activity.addDeck.presenter;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

public class EnterDeckDestinationPresenter {
    private final EnterDeckDestinationView view;
    private final NewDeckContext contextFromIntent;

    public EnterDeckDestinationPresenter(EnterDeckDestinationView view, NewDeckContext contextFromIntent) {
        this.view = view;
        this.contextFromIntent = contextFromIntent;
    }

    public void setBitmaps() {
        view.setActivityBitmap(R.id.enter_deck_destination_image, R.drawable.enter_phrase_image);
    }


    public void updateNextButtonState() {
        boolean hasDestinationLanguages = !contextFromIntent.getDestinationLanguages().isEmpty();
        int backgroundResource = hasDestinationLanguages ? R.drawable.forward_arrow : R.drawable.forward_arrow_disabled;
        int buttonColor = hasDestinationLanguages ? R.color.primaryTextColor : R.color.textDisabled;
        view.updateNextButton(hasDestinationLanguages, buttonColor, backgroundResource);
    }

    public void populateFlexBox() {

    }

    public interface EnterDeckDestinationView extends AddDeckView {
        void updateNextButton(boolean clickable, int buttonColor, int backgroundResource);
    }
}
