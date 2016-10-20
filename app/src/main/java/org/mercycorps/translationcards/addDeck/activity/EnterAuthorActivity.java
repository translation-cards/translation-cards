package org.mercycorps.translationcards.addDeck.activity;

import android.widget.EditText;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.presenter.EnterAuthorPresenter;
import org.mercycorps.translationcards.view.NextButton;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterAuthorActivity extends AddDeckActivity implements EnterAuthorPresenter.EnterAuthorView {
    @Bind(R.id.next_button)NextButton nextButton;
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

    @OnClick(R.id.next_button)
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
    public void enableNextButton() {
        nextButton.enable();
    }

    @Override
    public void disableNextButton() {
        nextButton.disable();
    }
}
