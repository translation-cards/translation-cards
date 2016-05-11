package org.mercycorps.translationcards.activity.addDeck;


import org.mercycorps.translationcards.R;

import butterknife.OnClick;

public class EnterDeckSourceLanguageActivity extends AddDeckActivity {
    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_source_language);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.deck_source_language_image, R.drawable.enter_phrase_image);
    }

    @OnClick(R.id.deck_source_language_next_label)
    protected void nextButtonClicked() {
        startNextActivity(this, EnterDeckDestinationLanguagesActivity.class);
    }

    @OnClick(R.id.deck_source_language_back_arrow)
    public void backButtonClicked(){
        startNextActivity(this, EnterDeckTitleActivity.class);
    }

}
