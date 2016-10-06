package org.mercycorps.translationcards.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.myDecks.Router;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeckItem extends LinearLayout implements DeckItemPresenter.DeckItemView {
    public static final String DELETE_DECK = "Delete";
    public static final String SHARE_DECK = "Share";

    @Bind(R.id.deck_name)
    TextView deckNameTextView;
    @Bind(R.id.deck_information)
    TextView deckInformationTextView;
    @Bind(R.id.lock_icon)
    FrameLayout lockIcon;
    @Bind(R.id.no_lock_icon_padding)
    View noLockIconPadding;
    @Bind(R.id.translation_languages)
    TextView destinationLanguagesTextView;
    @Bind(R.id.deck_menu)
    FrameLayout deckMenu;
    @Bind(R.id.origin_language)
    TextView sourceLanguageTextView;
    @Bind(R.id.deck_card)
    View deckCard;


    @Inject DeckService deckService;
    @Inject DictionaryService dictionaryService;
    @Inject Router router;

    private PopupMenu popupMenu;
    private DeckMenuListener menuListener;
    private Deck deck;
    private DeckItemPresenter presenter;

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
        MainApplication application = (MainApplication)getContext().getApplicationContext();
        application.getBaseComponent().inject(this);
        presenter = new DeckItemPresenter(this, dictionaryService, deckService);
    }

    @OnClick(R.id.deck_card)
    public void deckClicked() {
        presenter.deckClicked(deck);
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        presenter.setDeck(deck);
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

    // DeckItemView Implementation
    @Override
    public void launchTranslationsActivity() {
        router.launchTranslationsActivity(getContext());
    }

    @Override
    public void setDeckTitle(String deckTitle) {
        deckNameTextView.setText(deckTitle);
    }

    @Override
    public void setDeckInformation(String deckInformation) {
        deckInformationTextView.setText(deckInformation);
    }

    @Override
    public void setDeckSourceLanguage(String sourceLanguage) {
        sourceLanguageTextView.setText(sourceLanguage);
    }

    @Override
    public void displayLockIcon() {
        lockIcon.setVisibility(View.VISIBLE);
        noLockIconPadding.setVisibility(View.GONE);
    }

    @Override
    public void hideLockIcon() {
        lockIcon.setVisibility(View.GONE);
        noLockIconPadding.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDeckDestinationLanguages(String destinationLanguages) {
        destinationLanguagesTextView.setText(destinationLanguages);
    }
}
