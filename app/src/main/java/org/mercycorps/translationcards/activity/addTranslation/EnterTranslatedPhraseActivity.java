package org.mercycorps.translationcards.activity.addTranslation;

import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterTranslatedPhraseActivity extends AddTranslationActivity {
    @Bind(R.id.translated_phrase_field)TextView translatedPhraseTextView;
    @Bind(R.id.translated_phrase_input_language_label)TextView translatedPhraseInputLanguageLabel;
    @Bind(R.id.recording_label_next_text)TextView skipLabel;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_enter_translated_phrase);
    }

    @Override
    public void initStates(){
        updateInputLanguageLabel();
        updateTranslatedPhraseTextField();
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_translated_phrase_image, R.drawable.enter_phrase_image);
    }

        @OnClick(R.id.enter_translated_phrase_next_label)
    protected void enterTranslatedTextNextLabelClicked(){
        updateContextWithTranslatedText();
        startNextActivity(EnterTranslatedPhraseActivity.this, RecordAudioActivity.class);
    }

    @OnClick(R.id.enter_translated_phrase_back_label)
    protected void enterTranslatedPhraseBackLabelClicked() {
        updateContextWithTranslatedText();
        startNextActivity(EnterTranslatedPhraseActivity.this, EnterSourcePhraseActivity.class);
    }

    @OnTextChanged(R.id.translated_phrase_field)
    protected void changeSkipLabelToNextLabelWhenTextIsEnter(){
        String inputText = translatedPhraseTextView.getText().toString();
        Integer labelTextResourceId = inputText.isEmpty() ? R.string.navigation_button_skip : R.string.navigation_button_next;
        skipLabel.setText(labelTextResourceId);
    }

    private void updateInputLanguageLabel() {
        String inputLanguageLabel = getContextFromIntent().getDictionary().getLabel().toUpperCase();
        translatedPhraseInputLanguageLabel.setText(String.format(getString(R.string.translated_phrase_input_language_label), inputLanguageLabel));
    }

    private void updateTranslatedPhraseTextField() {
        translatedPhraseTextView.setText(getContextFromIntent().getTranslation().getTranslatedText());
    }

    private void updateContextWithTranslatedText() {
        String translatedText  = translatedPhraseTextView.getText().toString();
        getContextFromIntent().setTranslatedText(translatedText);
    }


}
