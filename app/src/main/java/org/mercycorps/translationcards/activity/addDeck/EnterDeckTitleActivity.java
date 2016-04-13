package org.mercycorps.translationcards.activity.addDeck;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterDeckTitleActivity extends AddDeckActivity {
    @Bind(R.id.deck_title_input)TextView deckTitleInput;
    @Bind(R.id.enter_deck_title_next_label)LinearLayout nextButton;

    @Override
    public void initStates(){
        setDeckTitle();
    }

    private void setDeckTitle() {
        deckTitleInput.setText(getContextFromIntent().getDeck().getLabel());
    }

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_set_title);
    }

    @OnClick(R.id.enter_title_back)
    protected void enterTitleBackButtonClicked(){
        startNextActivity(EnterDeckTitleActivity.this, GetStartedDeckActivity.class);
    }

    @OnClick(R.id.enter_deck_title_next_label)
    protected void enterTitleNextButtonClicked(){
        if(!nextButton.isClickable())return;
        startNextActivity(EnterDeckTitleActivity.this, EnterDeckDestinationLanguagesActivity.class);
    }

    @OnTextChanged(R.id.deck_title_input)
    protected void deckTitleInputTextChanged(){
        String deckTitle = deckTitleInput.getText().toString();
        nextButton.setClickable(!deckTitle.isEmpty());
    }
}
