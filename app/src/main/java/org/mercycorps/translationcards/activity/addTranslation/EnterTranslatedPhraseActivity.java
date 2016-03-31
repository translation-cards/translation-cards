package org.mercycorps.translationcards.activity.addTranslation;

import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterTranslatedPhraseActivity extends AddTranslationActivity {
    @Bind(R.id.translated_phrase_field)TextView translatedPhraseTextView;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_enter_translated_phrase);
    }

    public void initStates(){
        updateTranslatedPhraseTextField();
    }

    private void updateTranslatedPhraseTextField() {
        translatedPhraseTextView.setText(getContextFromIntent().getTranslation().getTranslatedText());
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

    private void updateContextWithTranslatedText() {
        String translatedText  = translatedPhraseTextView.getText().toString();
        getContextFromIntent().setTranslatedText(translatedText);
    }

    @OnClick(R.id.enter_translated_phrase_back_label)
    protected void enterTranslatedPhraseBackLabelClicked() {
        updateContextWithTranslatedText();
        startNextActivity(EnterTranslatedPhraseActivity.this, EnterSourcePhraseActivity.class);
    }
}
