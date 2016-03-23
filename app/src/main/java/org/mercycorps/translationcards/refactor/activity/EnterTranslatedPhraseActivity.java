package org.mercycorps.translationcards.refactor.activity;

import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterTranslatedPhraseActivity extends AbstractTranslationCardsActivity {
    @Bind(R.id.translated_phrase_field)TextView translatedPhraseTextView;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_enter_translated_phrase);
    }

    @OnClick(R.id.enter_translated_phrase_save_label)
    public void enterTranslatedTextNextLabelClicked(){
        String translatedText  = translatedPhraseTextView.getText().toString();
        getContextFromIntent().setTranslatedText(translatedText);
        saveTranslation();
        startNextActivity(EnterTranslatedPhraseActivity.this, SummaryActivity.class);
    }

    private void saveTranslation() {
        ((MainApplication) getApplication()).getDbManager().saveTranslationContext(getContextFromIntent());
    }
}
