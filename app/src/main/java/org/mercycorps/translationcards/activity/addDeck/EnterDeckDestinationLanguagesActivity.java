package org.mercycorps.translationcards.activity.addDeck;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;

import butterknife.OnClick;

/**
 * Created by njimenez on 4/12/16.
 */
public class EnterDeckDestinationLanguagesActivity extends AddDeckActivity{
    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_destination_languages);
    }

    @Override
    public void initStates(){

    }

    @OnClick(R.id.enter_destination_next_label)
    public void nextButtonClicked(){
        startNextActivity(EnterDeckDestinationLanguagesActivity.this, MyDecksActivity.class);
    }

    @OnClick(R.id.enter_destination_back_arrow)
    public void backButtonClicked(){
        startNextActivity(EnterDeckDestinationLanguagesActivity.this, EnterDeckTitleActivity.class);
    }
}
