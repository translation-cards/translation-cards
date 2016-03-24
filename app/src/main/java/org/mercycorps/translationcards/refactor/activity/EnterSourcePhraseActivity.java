package org.mercycorps.translationcards.refactor.activity;

import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.TranslationsActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterSourcePhraseActivity extends AddTranslationActivity {

    @Bind(R.id.source_phrase_field)TextView sourcePhraseTextView;
    @Bind(R.id.source_phrase_title)TextView sourcePhraseTitle;

    @Override
    protected void setActivityTitle() {
        sourcePhraseTitle.setText(String.format(getString(R.string.enter_source_phrase_activity_titile), getContextFromIntent().getDictionary().getLabel()));
    }

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_enter_source_phrase);
    }

    @Override
    protected void initStates() {
        setSourcePhrase();
    }

    private void setSourcePhrase() {
        sourcePhraseTextView.setText(getContextFromIntent().getTranslation().getLabel());
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_source_phrase_image, R.drawable.enter_phrase_image);
    }

    @OnClick(R.id.cancel_add_translation_activity)
    public void cancelButtonClick() {
        startNextActivity(EnterSourcePhraseActivity.this, TranslationsActivity.class);
    }

    @OnClick(R.id.enter_translated_phrase_save_label)
    public void enterTranslatedPhraseSaveLabelClick(){
        String userEnteredSourcePhrase = sourcePhraseTextView.getText().toString();
        if (userEnteredSourcePhrase.isEmpty()) return;
        getContextFromIntent().setSourceText(userEnteredSourcePhrase);
        startNextActivity(EnterSourcePhraseActivity.this, RecordAudioActivity.class);
    }

}
