package org.mercycorps.translationcards.activity.addDeck;


import android.widget.TextView;

import org.mercycorps.translationcards.R;

import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckSourceLanguageActivity extends AddDeckActivity {
    @Bind (R.id.deck_source_language_text) TextView sourceLanguageInput;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_source_language);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.deck_source_language_image, R.drawable.enter_phrase_image);
    }

    @OnClick(R.id.deck_source_language_next_label)
    protected void nextButtonClicked() {
        updateContextWithSourceLanguage();
        startNextActivity(this, EnterDeckDestinationLanguagesActivity.class);
    }

    @OnClick(R.id.deck_source_language_back_arrow)
    public void backButtonClicked(){
        startNextActivity(this, EnterDeckTitleActivity.class);
    }

    private void updateContextWithSourceLanguage() {
        String sourceLanguageIso = getLocaleForLanguage(sourceLanguageInput.getText().toString());
        getContextFromIntent().setSourceLanguageIso(sourceLanguageIso);
    }

    private String getLocaleForLanguage(String sourceLanguage) {
        for(Locale locale : Locale.getAvailableLocales()) {
            if(locale.getDisplayLanguage().equals(sourceLanguage)) {
                return locale.getLanguage();
            }
        }
        return "";
    }

}

