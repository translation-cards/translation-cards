package org.mercycorps.translationcards.activity.addDeck;

import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.presenter.EnterAuthorPresenter;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterAuthorActivity extends AddDeckActivity implements EnterAuthorPresenter.EnterAuthorView {

    @Bind(R.id.deck_author_next_label)LinearLayout nextButton;
    @Bind(R.id.deck_author_next_text)TextView nextButtonText;
    @Bind(R.id.deck_author_next_image)ImageView nextButtonImage;
    @Bind(R.id.deck_author_input) EditText deckAuthorInput;
    private EnterAuthorPresenter presenter;


    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_author);
    }

    @Override
     public void setBitmapsForActivity() {
        presenter = new EnterAuthorPresenter(this, getContextFromIntent());
        presenter.inflateBitmaps();
    }

    @Override
    public void initStates() {
        presenter.initDeckAuthorInput();
    }

    @OnClick(R.id.deck_author_next_label)
    protected void nextButtonClicked() {
        presenter.nextButtonClicked();
    }

    @OnClick(R.id.deck_author_back)
    public void backButtonClicked(){
        presenter.backButtonClicked();
    }

    @OnTextChanged(R.id.deck_author_input)
    protected void deckAuthorInputTextChanged() {
        presenter.deckAuthorInputChanged(deckAuthorInput.getText().toString());
    }

    // EnterAuthorView Implementation
    @Override
    public void updateDeckAuthorInput(String deckAuthor) {
        deckAuthorInput.setText(deckAuthor);
    }

    @Override
    public void updateNextButtonClickable(boolean nextButtonClickable, int nextButtonColor, int nextButtonArrow) {
        nextButton.setClickable(nextButtonClickable);
        nextButtonText.setTextColor(ContextCompat.getColor(this, nextButtonColor));
        nextButtonImage.setBackgroundResource(nextButtonArrow);
    }
}
