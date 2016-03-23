package org.mercycorps.translationcards.refactor.activity;

import android.view.View;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.TranslationsActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class SummaryActivity extends AbstractTranslationCardsActivity {
    @Bind(R.id.origin_translation_text)TextView sourceTextView;
    @Bind(R.id.translated_text)TextView translatedTextView;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_summary);
    }

    @Override
    public void initStates(){
        setVisibility(R.id.translation_child, View.VISIBLE);
        updateTextInTextView(sourceTextView, getContextFromIntent().getTranslation().getLabel());
        updateTranslatedTextView();
    }

    private void setVisibility(int layout, int visibility) {
        findViewById(layout).setVisibility(visibility);
    }

    private void updateTranslatedTextView() {
        String translatedText = getContextFromIntent().getTranslation().getTranslatedText();
        if (translatedText.isEmpty()) {
            translatedTextView.setHint(String.format("Add %s translation", getContextFromIntent().getDictionary().getLabel()));
        } else {
            updateTextInTextView(translatedTextView, translatedText);
        }
    }

    private void updateTextInTextView(TextView textView, String textToBeUpdated){
        textView.setText(textToBeUpdated);
    }

    @OnClick(R.id.summary_done)
    protected void summaryDoneClicked() {
        startNextActivity(SummaryActivity.this, TranslationsActivity.class);
    }
}
