package org.mercycorps.translationcards.addDeck.activity;


import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.activity.AddDeckActivity;
import org.mercycorps.translationcards.addDeck.presenter.ReviewAndSaveDeckPresenter;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.view.DeckItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class ReviewAndSaveDeckActivity extends AddDeckActivity implements ReviewAndSaveDeckPresenter.ReviewAndSaveDeckView {
    @Bind(R.id.translation_languages)
    TextView translationLanguagesTextView;
    @Bind(R.id.deck_item)
    DeckItem deckItem;

    @Inject DeckRepository deckRepository;
    private ReviewAndSaveDeckPresenter presenter;

    @Override
    public void inflateView() {
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);
        setContentView(R.layout.activity_deck_review_and_save);
        presenter = new ReviewAndSaveDeckPresenter(this, getContextFromIntent(), deckRepository);
    }

    @Override
    public void initStates() {
        presenter.initializeView();
    }

    @OnClick(R.id.deck_review_and_save_button)
    protected void saveButtonClicked() {
        presenter.saveButtonClicked();
    }

    @OnClick(R.id.deck_review_and_save_back)
    public void backButtonClicked() {
        presenter.backButtonClicked();
    }

    // ReviewAndSaveDeckView Implementation
    @Override
    public void setBitmapsForActivity() {
        presenter.inflateBitmap();
    }

    @Override
    public void finishAddDeckFlow() {
        finish();
    }

    @Override
    public void setDeckItem(Deck deck) {
        deckItem.setDeck(deck);
    }

    @Override
    public void fillLanguageList(String formattedLanguages) {
        translationLanguagesTextView.setText(formattedLanguages);
    }
}
