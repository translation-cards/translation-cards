package org.mercycorps.translationcards.activity.addDeck;


import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.repository.LanguageRepository;
import org.mercycorps.translationcards.service.LanguageService;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckSourceLanguageActivity extends AddDeckActivity {
    private static final String DEFAULT_LIST_ITEM_HEIGHT = "64.0";
    private static final int MAX_ROW_COUNT = 3;
    @Bind(R.id.deck_source_language_view)
    TextView sourceLanguageView;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_source_language);
    }

    @Override
    public void initStates() {
        fillSourceLanguageField();
    }

    private void fillSourceLanguageField() {
        sourceLanguageView.setText(LanguageService.getTitleCaseName(getContextFromIntent().getSourceLanguage()));
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
    public void backButtonClicked() {
        updateContextWithSourceLanguage();
        startNextActivity(this, EnterDeckTitleActivity.class);
    }

    @OnClick(R.id.deck_source_language_view)
    public void sourceLanguageClicked() {
        updateContextWithSourceLanguage();
        startNextActivity(this, EnterDeckTitleActivity.class);
    }

    private void updateContextWithSourceLanguage() {
        Language language = new LanguageRepository().withName(sourceLanguageView.getText().toString());
        getContextFromIntent().setSourceLanguage(language);
    }
}

