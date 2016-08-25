package org.mercycorps.translationcards.viewModel;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.AddDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.activity.addDeck.GetStartedDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

public class EnterDeckTitleViewModel {
    private NewDeckContext newDeckContext;

    public EnterDeckTitleViewModel(NewDeckContext newDeckContext) {
        this.newDeckContext = newDeckContext;
    }

    public String getDeckTitle() {
        return newDeckContext.getDeckTitle();
    }

    public void saveDeckTitle(String deckTitle) {
        newDeckContext.setDeckTitle(deckTitle);
    }

    public boolean isNextButtonClickable() {
        return !isDeckTitleEmpty();
    }

    public int getTextColor() {
        return isDeckTitleEmpty() ? R.color.textDisabled : R.color.primaryTextColor;
    }

    public int getNextArrow() {
        return isDeckTitleEmpty() ? R.drawable.forward_arrow_disabled : R.drawable.forward_arrow;
    }

    public void backButtonClicked(AddDeckActivity activity) {
        activity.startNextActivity(activity, GetStartedDeckActivity.class);
    }

    public void nextButtonClicked(AddDeckActivity activity) {
        if (isDeckTitleEmpty()) return;
        activity.startNextActivity(activity, EnterDeckSourceLanguageActivity.class);
    }

    private boolean isDeckTitleEmpty() {
        return newDeckContext.getDeckTitle().isEmpty();
    }
}