package org.mercycorps.translationcards.activity.addDeck;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.service.LanguageService;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterDeckDestinationLanguagesActivity extends AddDeckActivity {
    private static final String LANGUAGE_DELIMITER = ",";
    private static final String INPUT_DELIMITER = ", ";
    private NewDeckContext newDeckContext;
    @Bind(R.id.enter_deck_destination_input)
    AutoCompleteTextView destinationLanguageInput;
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
        newDeckContext = getContextFromIntent();
        LanguageService languageService = ((MainApplication) getApplication()).getLanguageService();
        ArrayAdapter<String> languagesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                languageService.getLanguageNames()
        );

        destinationLanguageInput.setAdapter(languagesAdapter);
        addEditorListener();
        fillDestinationLanguageField();
    }

    private void addEditorListener() {
        TextView.OnEditorActionListener onEditorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(EditorInfo.IME_ACTION_DONE == actionId){
                    addDestinationLanguage();
                }
                return false;
            }
        };
        destinationLanguageInput.setOnEditorActionListener(onEditorActionListener);
    }

    private void addDestinationLanguage() {
        String destinationLanguage = destinationLanguageInput.getText().toString();
        newDeckContext.addDestinationLanguage(destinationLanguage);
    }

    private void fillDestinationLanguageField() {
        destinationLanguageInput.setText(newDeckContext.getLanguagesInput());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        String selectedLanguage = data.getStringExtra(DestinationLanguageSelectorActivity.SELECTED_LANGUAGE_KEY);
        newDeckContext.addDestinationLanguage(selectedLanguage);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.select_language_button)
    public void showLanguageSelector() {
        startActivityForResult(new Intent(this, DestinationLanguageSelectorActivity.class), 0);
    }

    private void updateContextWithLanguagesInput() {
        getContextFromIntent().updateLanguagesInput(destinationLanguageInput.getText().toString());
    }

    @OnTextChanged(R.id.enter_deck_destination_input)
    public void destinationInputTextChanged() {
        String destinationLanguages = destinationLanguageInput.getText().toString();
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
