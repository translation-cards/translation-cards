package org.mercycorps.translationcards.viewModel;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckTitleActivity;
import org.mercycorps.translationcards.activity.addDeck.GetStartedDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

public class EnterDeckTitlePresenter {
    private DeckPresenterView activity;
    private NewDeckContext newDeckContext;
    private final TextView deckTitleInput;
    private final LinearLayout nextButton;

    public EnterDeckTitlePresenter(DeckPresenterView activity, NewDeckContext newDeckContext, TextView deckTitleInput) {
        this.activity = activity;
        this.newDeckContext = newDeckContext;
        this.deckTitleInput = deckTitleInput;
        this.nextButton = activity.getNextButtonLayout();
    }

    public void updateDeckTitleInput() {
        deckTitleInput.setText(newDeckContext.getDeckTitle());
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

    public void deckTitleInputChanged() {
        newDeckContext.setDeckTitle(deckTitleInput.getText().toString());
        nextButton.setClickable(isNextButtonClickable());
        TextView nextButtonTextView = (TextView) nextButton.findViewById(R.id.enter_deck_title_next_text);
        nextButtonTextView.setTextColor(activity.getAppColor(getTextColor()));
        nextButton.findViewById(R.id.enter_deck_title_next_image).setBackgroundResource(getNextArrow());
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

    public interface DeckPresenterView {

        void startActivityWithClass(Class<? extends Activity> activityClass);
        void setActivityBitmap(int resId, int drawableId);
        LinearLayout getNextButtonLayout();
        int getAppColor(int colorId);
    }
}