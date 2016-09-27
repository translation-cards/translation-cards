package org.mercycorps.translationcards.addDeck.activity;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.presenter.EnterDeckTitlePresenter;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterDeckTitleActivity extends AddDeckActivity implements EnterDeckTitlePresenter.EnterDeckTitleView {
    @Bind(R.id.deck_title_input)TextView deckTitleInput;
    @Bind(R.id.enter_deck_title_next_label)LinearLayout nextButton;
    @Bind(R.id.enter_deck_title_next_text)TextView nextButtonText;
    @Bind(R.id.enter_deck_title_next_image)ImageView nextButtonImage;
    private EnterDeckTitlePresenter presenter;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_enter_deck_title);
    }

    @Override
    public void setBitmapsForActivity() {
        presenter = new EnterDeckTitlePresenter(this, getContextFromIntent());
        presenter.inflateBitmaps();
    }

    @Override
    public void initStates(){
        presenter.updateDeckTitleInput();
    }

    @OnClick(R.id.enter_deck_title_back)
    protected void enterTitleBackButtonClicked() {
        presenter.backButtonClicked();
    }

    @OnClick(R.id.enter_deck_title_next_label)
    protected void enterTitleNextButtonClicked(){
        presenter.nextButtonClicked();
    }

    @OnTextChanged(R.id.deck_title_input)
    protected void deckTitleInputTextChanged(){
        presenter.deckTitleInputChanged(deckTitleInput.getText().toString());
    }

    // EnterDeckTitleView Implementation
    @Override
    public void updateNextButton(boolean buttonClickable, int buttonTextColor, int buttonArrow) {
        nextButton.setClickable(buttonClickable);
        nextButtonText.setTextColor(ContextCompat.getColor(this, buttonTextColor));
        nextButtonImage.setBackgroundResource(buttonArrow);
    }

    @Override
    public void setDeckTitleText(String deckTitle) {
        deckTitleInput.setText(deckTitle);
    }
}
