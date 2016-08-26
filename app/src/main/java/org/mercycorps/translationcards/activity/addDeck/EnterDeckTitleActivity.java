package org.mercycorps.translationcards.activity.addDeck;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.viewModel.EnterDeckTitlePresenter;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterDeckTitleActivity extends AddDeckActivity {
    @Bind(R.id.deck_title_input)TextView deckTitleInput;
    @Bind(R.id.enter_deck_title_next_label)LinearLayout nextButton;
    @Bind(R.id.enter_deck_title_next_text)TextView nextButtonText;
    @Bind(R.id.enter_deck_title_next_image)ImageView nextButtonImage;
    private EnterDeckTitlePresenter presenter;

    @Override
    public void inflateView() {
        presenter = new EnterDeckTitlePresenter(this, getContextFromIntent(), deckTitleInput, nextButton, nextButtonText, nextButtonImage);
        setContentView(R.layout.activity_enter_deck_title);
    }

    @Override
    public void initStates(){
        presenter.updateDeckTitleInput();
    }

    @Override
    public void setBitmapsForActivity() {
        presenter.inflateBitmaps();
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
        presenter.deckTitleInputChanged();
    }

}
