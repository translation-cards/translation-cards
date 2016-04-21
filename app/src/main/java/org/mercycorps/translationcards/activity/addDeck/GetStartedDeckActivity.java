package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;

import butterknife.OnClick;

public class GetStartedDeckActivity extends AddDeckActivity {

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_get_started);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.deck_get_started_image, R.drawable.get_started_image);
    }

    @OnClick(R.id.deck_get_started_button)
    protected void getStartedButtonClicked() {
        startNextActivity(GetStartedDeckActivity.this, EnterDeckTitleActivity.class);
    }

    @OnClick(R.id.deck_get_started_back)
    public void getStartedBackButtonClicked(){
        startNextActivity(GetStartedDeckActivity.this, MyDecksActivity.class);
    }
}
