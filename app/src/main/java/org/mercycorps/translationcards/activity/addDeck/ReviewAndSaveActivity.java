package org.mercycorps.translationcards.activity.addDeck;


import android.view.View;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class ReviewAndSaveActivity extends AddDeckActivity {
    @Bind(R.id.deck_name)TextView deckName;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_review_and_save);
    }

    @Override
    public void initStates() {
        deckName.setText(getContextFromIntent().getDeckLabel());

    }

    @OnClick(R.id.deck_review_and_save_button)
    protected void saveButtonClicked() {
        getContextFromIntent().save();
        startNextActivity(this, MyDecksActivity.class);
    }

    @OnClick(R.id.deck_review_and_save_back)
    public void backButtonClicked(){
        startNextActivity(this, EnterAuthorActivity.class);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_source_language_image, R.drawable.enter_source_language_image);
    }
}
