package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.presenter.GetStartedDeckPresenter;

import butterknife.OnClick;

public class GetStartedDeckActivity extends AddDeckActivity {

    private GetStartedDeckPresenter presenter;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_get_started);
    }

    @Override
    public void setBitmapsForActivity() {
        presenter = new GetStartedDeckPresenter(this);
        presenter.inflateBitmaps();
    }

    @OnClick(R.id.deck_get_started_button)
    protected void getStartedButtonClicked() {
        presenter.getStartedButtonClicked();
    }

    @OnClick(R.id.deck_get_started_back)
    public void getStartedBackButtonClicked(){
        presenter.backButtonClicked();
    }
}
