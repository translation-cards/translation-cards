package org.mercycorps.translationcards.refactor.activity;

import android.widget.TextView;

import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterTranslatedPhraseActivity extends AddTranslationActivity {
    @Bind(R.id.translated_phrase_field)TextView translatedPhraseTextView;
    @Bind(R.id.translated_phrase_title)TextView translatedPhraseTitleView;

    @Override
    protected void setActivityTitle() {
        translatedPhraseTitleView.setText(String.format(getString(R.string.enter_translated_phrase_title),
                getContextFromIntent().getDictionary().getLabel()));
    }

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

    @OnClick(R.id.enter_translated_phrase_save_label)
    protected void enterTranslatedTextNextLabelClicked(){
        String translatedText  = translatedPhraseTextView.getText().toString();
        getContextFromIntent().setTranslatedText(translatedText);
        startNextActivity(EnterTranslatedPhraseActivity.this, SummaryActivity.class);
    }

    @OnClick(R.id.go_to_record_audio_label)
    protected void goToRecordAudioLabelClicked() {
        startNextActivity(EnterTranslatedPhraseActivity.this, RecordAudioActivity.class);
    }
}
