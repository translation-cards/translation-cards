package org.mercycorps.translationcards.activity.addDeck;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterDeckTitleActivity extends AddDeckActivity {
    @Bind(R.id.deck_title_input)TextView deckTitleInput;
    @Bind(R.id.enter_deck_title_next_label)LinearLayout nextButton;
    @Bind(R.id.enter_deck_title_next_text)TextView nextButtonText;
    @Bind(R.id.enter_deck_title_next_image)ImageView nextButtonImage;

    @Override
    public void initStates(){
        setDeckTitle();
    }

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_enter_deck_title);
    }
    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_deck_title_image, R.drawable.enter_phrase_image);
    }

    @OnClick(R.id.enter_deck_title_back)
    protected void enterTitleBackButtonClicked(){
        updateContextWithDeckTitle();
        startNextActivity(EnterDeckTitleActivity.this, GetStartedDeckActivity.class);
    }

    @OnClick(R.id.enter_deck_title_next_label)
    protected void enterTitleNextButtonClicked(){
        if(!nextButton.isClickable())return;
        updateContextWithDeckTitle();
        startNextActivity(EnterDeckTitleActivity.this, EnterDeckSourceLanguageActivity.class);
    }

    @OnTextChanged(R.id.deck_title_input)
    protected void deckTitleInputTextChanged(){
        nextButton.setClickable(!isDeckTitleEmpty());
        updateNextButtonColor();
    }

    private void setDeckTitle() {
        deckTitleInput.setText(getContextFromIntent().getDeckLabel());
    }

    private void updateContextWithDeckTitle() {
        getContextFromIntent().setDeckTitle(deckTitleInput.getText().toString());
    }

    private void updateNextButtonColor() {
        Integer textColor = isDeckTitleEmpty() ? R.color.textDisabled : R.color.primaryTextColor;
        Integer nextArrow = isDeckTitleEmpty() ? R.drawable.forward_arrow_disabled : R.drawable.forward_arrow;
        nextButtonText.setTextColor(ContextCompat.getColor(this, textColor));
        nextButtonImage.setBackgroundResource(nextArrow);
    }

    private boolean isDeckTitleEmpty() {
        return deckTitleInput.getText().toString().isEmpty();
    }
}
