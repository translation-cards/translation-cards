package org.mercycorps.translationcards.addDeck.presenter;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckTitleActivity;

public class GetStartedDeckPresenter {
    private AddDeckView activity;

    public GetStartedDeckPresenter(AddDeckView activity) {
        this.activity = activity;
    }

    public void inflateBitmaps() {
        activity.setActivityBitmap(R.id.deck_get_started_image, R.drawable.get_started_image);
    }

    public void getStartedButtonClicked() {
        activity.startActivityWithClass(EnterDeckTitleActivity.class);
    }

    public void backButtonClicked() {
        activity.startActivityWithClass(MyDecksActivity.class);
    }
}
