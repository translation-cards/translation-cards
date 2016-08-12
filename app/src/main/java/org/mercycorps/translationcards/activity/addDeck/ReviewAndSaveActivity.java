package org.mercycorps.translationcards.activity.addDeck;


import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.ui.LanguageDisplayUtil;
import org.mercycorps.translationcards.view.DeckItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class ReviewAndSaveActivity extends AddDeckActivity {
    @Bind(R.id.translation_languages)
    TextView translationLanguagesTextView;
    @Bind(R.id.deck_item)
    DeckItem deckItem;
    private NewDeckContext newDeckContext;

    @Inject DeckService deckService;

    @Override
    public void inflateView() {
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);
        setContentView(R.layout.activity_deck_review_and_save);
    }

    @Override
    public void initStates() {
        newDeckContext = getContextFromIntent();
        Deck deck = newDeckContext.getDeck();
        deckItem.setDeck(deck);
        fillLanguagesListTextView();
    }

    @OnClick(R.id.deck_review_and_save_button)
    protected void saveButtonClicked() {
        deckService.save(newDeckContext.getDeck(), newDeckContext.getDestinationLanguages());
        finish();
    }

    @OnClick(R.id.deck_review_and_save_back)
    public void backButtonClicked() {
        startNextActivity(this, EnterAuthorActivity.class);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_source_language_image, R.drawable.enter_source_language_image);
    }

    /* Until the deck is saved, no language dictionaries are associated with a deck.
     * So we must fill this field from the NewDeckContext's languagesInput String
     */
    private void fillLanguagesListTextView() {
        List<String> destinationLanguages = new ArrayList<>(newDeckContext.getDestinationLanguages());
        Collections.sort(destinationLanguages);
        String formattedLanguages = LanguageDisplayUtil.getDestLanguagesFromStringsForDisplay(destinationLanguages);
        translationLanguagesTextView.setText(formattedLanguages);
    }
}
