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

public class EnterDeckDestinationLanguagesActivity extends AddDeckActivity {
    private static final String LANGUAGE_DELIMITER = ",";
    private static final String INPUT_DELIMITER = ", ";
    @Bind(R.id.enter_deck_destination_input)
    EditText input;
    @Bind(R.id.enter_destination_next_label)
    LinearLayout nextButton;
    @Bind(R.id.enter_destination_next_text)
    TextView nextButtonText;
    @Bind(R.id.enter_destination_next_image)
    ImageView nextButtonImage;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_destination_languages);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_deck_destination_image, R.drawable.enter_phrase_image);
    }

    @Override
    public void initStates() {
        fillDestinationLanguageField();
    }

    private void fillDestinationLanguageField() {
        input.setText(getContextFromIntent().getLanguagesInput());
    }

    @OnClick(R.id.enter_destination_next_label)
    public void nextButtonClicked() {
        if (!nextButton.isClickable()) {
            return;
        }
        updateContextWithLanguagesInput();

        startNextActivity(EnterDeckDestinationLanguagesActivity.this, EnterAuthorActivity.class);
    }

    @OnClick(R.id.enter_destination_back_arrow)
    public void backButtonClicked() {
        updateContextWithLanguagesInput();
        startNextActivity(this, EnterDeckSourceLanguageActivity.class);
    }

    private void updateContextWithLanguagesInput() {
        getContextFromIntent().updateLanguagesInput(input.getText().toString());
    }

    @OnTextChanged(R.id.enter_deck_destination_input)
    public void destinationInputTextChanged() {
        String destinationLanguages = input.getText().toString();
        nextButton.setClickable(!destinationLanguages.isEmpty());
        updateNextButtonState(destinationLanguages);
    }

    protected void updateNextButtonState(String destinationLanguages) {
        int backgroundResource = destinationLanguages.isEmpty() ? R.drawable.forward_arrow_disabled : R.drawable.forward_arrow;
        int buttonColor = destinationLanguages.isEmpty() ? R.color.textDisabled : R.color.primaryTextColor;
        nextButtonText.setTextColor(ContextCompat.getColor(this, buttonColor));
        nextButtonImage.setBackgroundResource(backgroundResource);
    }
}
