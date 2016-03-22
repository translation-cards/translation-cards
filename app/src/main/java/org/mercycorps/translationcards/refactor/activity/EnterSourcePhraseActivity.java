package org.mercycorps.translationcards.refactor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.NewTranslationContext;

public class EnterSourcePhraseActivity extends AppCompatActivity {

    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_source_phrase);
        setClickListeners();
    }

    private void setClickListeners() {
        findViewById(R.id.enter_source_phrase_next_label).setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLabelNextClick();
            }
        }));
    }

    private void handleLabelNextClick() {
        String userEnteredText = ((TextView) findViewById(R.id.source_phrase_field)).getText().toString();
        if (userEnteredText.isEmpty()) return;
        Intent intent = new Intent(EnterSourcePhraseActivity.this, RecordAudioActivity.class);
        intent.putExtra(CONTEXT_INTENT_KEY, createTranslationContext(userEnteredText));
        startActivity(intent);
    }

    private NewTranslationContext createTranslationContext(String userEnteredText){
        NewTranslationContext newTranslationContext = (NewTranslationContext) getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);
        newTranslationContext.setSourceText(userEnteredText);
        return newTranslationContext;
    }
}
