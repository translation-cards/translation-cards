package org.mercycorps.translationcards.activity.addDeck;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.viewModel.EnterDeckTitleViewModel;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterDeckTitleActivity extends AddDeckActivity {
    @Bind(R.id.deck_title_input)TextView deckTitleInput;
    @Bind(R.id.enter_deck_title_next_label)LinearLayout nextButton;
    @Bind(R.id.enter_deck_title_next_text)TextView nextButtonText;
    @Bind(R.id.enter_deck_title_next_image)ImageView nextButtonImage;
    private EnterDeckTitleViewModel viewModel;

    @Override
    public void inflateView() {
        viewModel = new EnterDeckTitleViewModel(getContextFromIntent());
        setContentView(R.layout.activity_enter_deck_title);
    }

    @Override
    public void initStates(){
        deckTitleInput.setText(viewModel.getDeckTitle());
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_deck_title_image, R.drawable.enter_phrase_image);
    }

    @OnClick(R.id.enter_deck_title_back)
    protected void enterTitleBackButtonClicked() {
        viewModel.backButtonClicked(this);
    }

    @OnClick(R.id.enter_deck_title_next_label)
    protected void enterTitleNextButtonClicked(){
        viewModel.nextButtonClicked(this);
    }

    @OnTextChanged(R.id.deck_title_input)
    protected void deckTitleInputTextChanged(){
        viewModel.saveDeckTitle(deckTitleInput.getText().toString());
        nextButton.setClickable(viewModel.isNextButtonClickable());
        nextButtonText.setTextColor(ContextCompat.getColor(this, viewModel.getTextColor()));
        nextButtonImage.setBackgroundResource(viewModel.getNextArrow());
    }

}
