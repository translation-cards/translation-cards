package org.mercycorps.translationcards.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Deck;

import java.util.Arrays;

import butterknife.Bind;

import static org.mercycorps.translationcards.ui.LanguageDisplayUtil.getDestLanguageListDisplay;

public class DeckCard extends LinearLayout {

    @Bind(R.id.deck_name)TextView deckName;
    @Bind(R.id.deck_information)TextView deckInformation;
    @Bind(R.id.lock_icon)FrameLayout lockIcon;
    @Bind(R.id.translation_languages)TextView translationLanguagesTextView;
    @Bind(R.id.deck_menu)FrameLayout deckMenu;
    @Bind(R.id.origin_language) TextView originLanguage;

    public DeckCard(Context context) {
        super(context);
        init();
    }

    public DeckCard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeckCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.deck_card, this);
    }

    public void enableMenu() {

    }

    public void setPopupMenu(PopupMenu menu) {}

    public void setDeck(Deck deck) {
        deckName.setText(deck.getTitle());
        deckInformation.setText(deck.getDeckInformation());
        originLanguage.setText(deck.getSourceLanguageName().toUpperCase());
        showLockIconIfDeckIsLocked(deck);
        translationLanguagesTextView.setText(getDestLanguageListDisplay(Arrays.asList(deck.getDictionaries())));
        deckMenu.setVisibility(View.GONE);
    }

    private void showLockIconIfDeckIsLocked(Deck deck) {
        int deckVisible = (deck.isLocked()) ? View.VISIBLE : View.GONE;
        lockIcon.setVisibility(deckVisible);
        deckInformation.setPadding(getPaddingInPx(16), 0, getPaddingInPx(16), getPaddingInPx(20));
    }

    private int getPaddingInPx(int padding) {
        final float scale = translationLanguagesTextView.getResources().getDisplayMetrics().density;
        return (int) (padding* scale + 0.5f);
    }

    public interface DeckMenuListener {
        public void onShareClicked();

        public void onDeleteClicked();
    }

}
