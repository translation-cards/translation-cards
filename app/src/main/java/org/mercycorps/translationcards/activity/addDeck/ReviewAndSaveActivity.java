package org.mercycorps.translationcards.activity.addDeck;


import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.view.DeckItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static org.mercycorps.translationcards.ui.LanguageDisplayUtil.getDestLanguageListDisplay;

public class ReviewAndSaveActivity extends AddDeckActivity {
    @Bind(R.id.translation_languages) TextView translationLanguagesTextView;
    @Bind(R.id.deck_item) DeckItem deckItem;
    private NewDeckContext newDeckContext;

    @Override
    public void inflateView() {
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
        DeckService deckService = ((MainApplication) getApplication()).getDeckService();
        deckService.save(newDeckContext.getDeck(), newDeckContext.getLanguagesInput());
        startNextActivity(this, MyDecksActivity.class);
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
        String languagesInput = newDeckContext.getLanguagesInput();
        if (languagesInput != null) {
            String[] languagesList = languagesInput.split(",");
            List<Dictionary> dictionaries = new ArrayList<>();
            for (String language : languagesList) {
                dictionaries.add(new Dictionary(language.trim()));
            }
            translationLanguagesTextView.setText(getDestLanguageListDisplay(dictionaries));
        }
    }
}
