package org.mercycorps.translationcards.viewModel;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckTitleActivity;
import org.mercycorps.translationcards.activity.addDeck.GetStartedDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

public class EnterDeckTitlePresenter {
    private EnterDeckTitleActivity activity;
    private NewDeckContext newDeckContext;
    private final TextView deckTitleInput;
    private final LinearLayout nextButton;
    private final TextView nextButtonText;
    private final ImageView nextButtonImage;

    public EnterDeckTitlePresenter(EnterDeckTitleActivity activity, NewDeckContext newDeckContext, TextView deckTitleInput, LinearLayout nextButton, TextView nextButtonText, ImageView nextButtonImage) {
        this.activity = activity;
        this.newDeckContext = newDeckContext;
        this.deckTitleInput = deckTitleInput;
        this.nextButton = nextButton;
        this.nextButtonText = nextButtonText;
        this.nextButtonImage = nextButtonImage;
    }

    public void updateDeckTitleInput() {
        deckTitleInput.setText(newDeckContext.getDeckTitle());
    }

    public void inflateBitmaps() {
        activity.setBitmap(R.id.enter_deck_title_image, R.drawable.enter_phrase_image);
    }

    public void backButtonClicked() {
        activity.startNextActivity(activity, GetStartedDeckActivity.class);
    }

    public void nextButtonClicked() {
        if (isDeckTitleEmpty()) return;
        activity.startNextActivity(activity, EnterDeckSourceLanguageActivity.class);
    }

    public void deckTitleInputChanged() {
        newDeckContext.setDeckTitle(deckTitleInput.getText().toString());
        nextButton.setClickable(isNextButtonClickable());
        nextButtonText.setTextColor(ContextCompat.getColor(activity, getTextColor()));
        nextButtonImage.setBackgroundResource(getNextArrow());
    }

    private boolean isNextButtonClickable() {
        return !isDeckTitleEmpty();
    }

    private int getTextColor() {
        return isDeckTitleEmpty() ? R.color.textDisabled : R.color.primaryTextColor;
    }

    private int getNextArrow() {
        return isDeckTitleEmpty() ? R.drawable.forward_arrow_disabled : R.drawable.forward_arrow;
    }

    private boolean isDeckTitleEmpty() {
        return newDeckContext.getDeckTitle().isEmpty();
    }
}