package org.mercycorps.translationcards.activity.addDeck;


import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;

import butterknife.OnClick;

public class ReviewAndSaveActivity extends AddDeckActivity {
    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_review_and_save);
    }

    @OnClick(R.id.deck_review_and_save_button)
    protected void saveButtonClicked() {
        getContextFromIntent().save();
        startNextActivity(this, MyDecksActivity.class);
    }

    @OnClick(R.id.deck_review_and_save_back)
    public void backButtonClicked(){
        startNextActivity(this, AuthorAndLockActivity.class);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.summary_image, R.drawable.summary_image);
    }
}
