package org.mercycorps.translationcards.activity.addDeck;


import android.os.Bundle;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class ReviewAndSaveActivity extends AddDeckActivity {
    @Bind(R.id.deck_name)TextView deckNameTextView;
    @Bind(R.id.deck_information)TextView deckInfoTextView;
    @Bind(R.id.translation_languages)TextView deckLanguagesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NewDeckContext newDeckContext = getContextFromIntent();
        deckNameTextView.setText(newDeckContext.getDeckLabel());
        deckInfoTextView.setText(newDeckContext.getAuthor());
        deckLanguagesTextView.setText(newDeckContext.getLanguagesInput());
    }

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
        startNextActivity(this, EnterAuthorActivity.class);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.summary_image, R.drawable.summary_image);
    }
}
