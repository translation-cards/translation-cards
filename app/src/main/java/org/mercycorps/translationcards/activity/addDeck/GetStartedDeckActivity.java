package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.R;

import butterknife.OnClick;

public class GetStartedDeckActivity extends AddDeckActivity {

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_get_started);
    }

    @OnClick(R.id.deck_get_started_button)
    protected void getStartedButtonClicked() {
        startNextActivity(GetStartedDeckActivity.this, EnterDeckTitleActivity.class);
    }
}
