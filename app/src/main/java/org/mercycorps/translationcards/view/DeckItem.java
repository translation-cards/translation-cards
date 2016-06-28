package org.mercycorps.translationcards.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Deck;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

import static org.mercycorps.translationcards.ui.LanguageDisplayUtil.getDestLanguageListDisplay;

public class DeckItem extends LinearLayout {
    public static final String DELETE_DECK = "Delete";
    public static final String SHARE_DECK = "Share";

    @Bind(R.id.deck_name) TextView deckNameTextView;
    @Bind(R.id.deck_information) TextView deckInformationTextView;
    @Bind(R.id.lock_icon) FrameLayout lockIcon;
    @Bind(R.id.translation_languages) TextView translationLanguagesTextView;
    @Bind(R.id.deck_menu) FrameLayout deckMenu;
    @Bind(R.id.origin_language) TextView originLanguageTextView;
    @Bind(R.id.deck_card) View deckCard;
    private PopupMenu popupMenu;
    private DeckMenuListener menuListener;
    private Deck deck;

    public DeckItem(Context context) {
        super(context);
        init();
    }

    public DeckItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DeckItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.deck_item, this);
        ButterKnife.bind(this);
    }

    public void setMenuListener(DeckMenuListener listener) {
        deckMenu.setVisibility(View.VISIBLE);
        createPopupMenu();
        this.menuListener = listener;
        deckMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        deckNameTextView.setText(deck.getTitle());
        deckInformationTextView.setText(deck.getDeckInformation());
        originLanguageTextView.setText(deck.getSourceLanguageName().toUpperCase());
        showLockIconIfDeckIsLocked(deck);
        translationLanguagesTextView.setText(getDestLanguageListDisplay(Arrays.asList(deck.getDictionaries())));
        deckMenu.setVisibility(View.GONE);
    }

    private void showLockIconIfDeckIsLocked(Deck deck) {
        if(!deck.isLocked()){
            lockIcon.setVisibility(View.GONE);
            deckInformationTextView.setPadding(getPaddingInPx(16), 0, getPaddingInPx(16), getPaddingInPx(20));
        }
        else{
            lockIcon.setVisibility(View.VISIBLE);
            deckInformationTextView.setPadding(getPaddingInPx(5), 0, getPaddingInPx(16), getPaddingInPx(20));
        }
    }

    private int getPaddingInPx(int padding) {
        final float scale = translationLanguagesTextView.getResources().getDisplayMetrics().density;
        return (int) (padding * scale + 0.5f);
    }

    private void createPopupMenu() {
        popupMenu = new PopupMenu(getContext(), deckMenu);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getTitle().toString()) {
                    case DELETE_DECK:
                        menuListener.onDeleteClicked(deck);
                        break;
                    case SHARE_DECK:
                        menuListener.onShareClicked(deck);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    public interface DeckMenuListener {
        void onShareClicked(Deck deck);

        void onDeleteClicked(Deck deck);
    }

    @Override
    public void setOnClickListener(OnClickListener listener) {
        super.setOnClickListener(null);
        deckCard.setOnClickListener(listener);
    }
}
