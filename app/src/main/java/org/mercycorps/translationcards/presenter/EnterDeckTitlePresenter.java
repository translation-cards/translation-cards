package org.mercycorps.translationcards.presenter;

import android.app.Activity;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.activity.addDeck.GetStartedDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

public class EnterDeckTitlePresenter {
    private DeckPresenterView activity;
    private NewDeckContext newDeckContext;

    public EnterDeckTitlePresenter(DeckPresenterView activity, NewDeckContext newDeckContext) {
        this.activity = activity;
        this.newDeckContext = newDeckContext;
    }

    public void updateDeckTitleInput() {
        activity.setDeckTitleText(newDeckContext.getDeckTitle());
    }

    public void inflateBitmaps() {
        activity.setActivityBitmap(R.id.enter_deck_title_image, R.drawable.enter_phrase_image);
    }

    public void backButtonClicked() {
        activity.startActivityWithClass(GetStartedDeckActivity.class);
    }

    public void nextButtonClicked() {
        if (isDeckTitleEmpty()) return;
        activity.startActivityWithClass(EnterDeckSourceLanguageActivity.class);
    }

    public void deckTitleInputChanged(String deckTitle) {
        newDeckContext.setDeckTitle(deckTitle);
        updateNextButton();
    }

    private void updateNextButton() {
        activity.updateNextButton(isNextButtonClickable(), nextButtonTextColor(), nextButtonArrow());
    }

    private boolean isNextButtonClickable() {
        return !isDeckTitleEmpty();
    }

    private int nextButtonTextColor() {
        return isDeckTitleEmpty() ? R.color.textDisabled : R.color.primaryTextColor;
    }

    private int nextButtonArrow() {
        return isDeckTitleEmpty() ? R.drawable.forward_arrow_disabled : R.drawable.forward_arrow;
    }

    private boolean isDeckTitleEmpty() {
        return newDeckContext.getDeckTitle().isEmpty();
    }

    public interface DeckPresenterView {
        void setDeckTitleText(String deckTitle);
        void updateNextButton(boolean buttonClickable, int buttonTextColor, int buttonArrow);
        void startActivityWithClass(Class<? extends Activity> activityClass);
        void setActivityBitmap(int resId, int drawableId);
    }
}