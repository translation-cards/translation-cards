package org.mercycorps.translationcards.activity.addDeck;

import android.support.v4.content.ContextCompat;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterAuthorActivity extends AddDeckActivity{

    @Bind(R.id.deck_author_next_label)LinearLayout nextButton;
    @Bind(R.id.deck_author_next_text)TextView nextButtonText;
    @Bind(R.id.deck_author_next_image)ImageView nextButtonImage;
    @Bind(R.id.deck_author_input) EditText deckAuthorInput;


    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_author);
    }

    @Override
     public void setBitmapsForActivity() {
        setBitmap(R.id.deck_author_image, R.drawable.enter_phrase_image);
    }

    @Override
    public void initStates() {
        fillAuthorField();
    }

    private void fillAuthorField() {
        deckAuthorInput.setText(getContextFromIntent().getAuthor());
    }

    @OnClick(R.id.deck_author_next_label)
    protected void nextButtonClicked() {
        if(!nextButton.isClickable())return;
        saveAuthorToContext();
        startNextActivity(this, ReviewAndSaveActivity.class);
    }

    private void saveAuthorToContext() {
        getContextFromIntent().setAuthor(deckAuthorInput.getText().toString());
    }

    @OnClick(R.id.deck_author_back)
    public void backButtonClicked(){
        saveAuthorToContext();
        startNextActivity(this, EnterDeckDestinationLanguagesActivity.class);
    }

    @OnTextChanged(R.id.deck_author_input)
    protected void deckAuthorInputTextChanged(){
        nextButton.setClickable(!isDeckAuthorEmpty());
        updateNextButtonColor();
    }

    private void updateNextButtonColor() {
        Integer textColor = isDeckAuthorEmpty() ? R.color.textDisabled : R.color.primaryTextColor;
        Integer nextArrow = isDeckAuthorEmpty() ? R.drawable.forward_arrow_40p : R.drawable.forward_arrow;
        nextButtonText.setTextColor(ContextCompat.getColor(this, textColor));
        nextButtonImage.setBackgroundResource(nextArrow);
    }

    private boolean isDeckAuthorEmpty() {
        return deckAuthorInput.getText().toString().isEmpty();
    }
}
