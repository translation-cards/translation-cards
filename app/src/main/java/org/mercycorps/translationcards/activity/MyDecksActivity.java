package org.mercycorps.translationcards.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.activity.AddDeckActivity;
import org.mercycorps.translationcards.addDeck.activity.GetStartedDeckActivity;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by njimenez on 3/31/16.
 */
public class MyDecksActivity extends AbstractTranslationCardsActivity {

    private static final int REQUEST_CODE_IMPORT_FILE_PICKER = 1;
    private static final int REQUEST_CODE_IMPORT_FILE = 2;
    private static final int REQUEST_CODE_CREATE_DECK = 3;

    @Bind(R.id.my_decks_list)ListView myDeckListView;
    @Bind(R.id.empty_my_decks_title)TextView emptyMyDecksTitle;
    @Bind(R.id.empty_my_decks_message)TextView emptyMyDecksMessage;
    @Bind(R.id.feedback_button)RelativeLayout feedbackButton;

    private static final String FEEDBACK_URL =
            "https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/" +
                    "viewform?entry.1158658650=1.1.0";

    @Inject DeckRepository deckRepository;
    @Inject DeckService deckService;
    @Inject DictionaryService dictionaryService;

    @Override
    public void inflateView() {
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);
        setContentView(R.layout.activity_my_decks);
        inflateListHeader();
        inflateListFooter();
    }

    @Override
    public void initStates() {
        setActionBarTitle();
        List<Deck> decks = getDecks();
        initListFooter(decks);
        updateDecksView(decks);
    }

    @Override
    protected void onResume() {
        refreshMyDecksList();
        super.onResume();
    }

    private void inflateListHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.my_decks_header, myDeckListView, false);
        ((ListView) findViewById(R.id.my_decks_list)).addHeaderView(headerView);
    }

    private void inflateListFooter() {
        View footerView = getLayoutInflater().inflate(R.layout.my_decks_footer, myDeckListView, false);
        ((ListView) findViewById(R.id.my_decks_list)).addFooterView(footerView);
    }

    private void setActionBarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_decks);
        }
    }

    private void updateDecksView(List<Deck> decks) {
        MyDeckAdapter listAdapter = new MyDeckAdapter(MyDecksActivity.this, decks, deckService, dictionaryService, deckRepository);
        ListView decksListView = (ListView) findViewById(R.id.my_decks_list);
        decksListView.setAdapter(listAdapter);
    }

    private void updateFooterView(List<Deck> decks) {
        updateFooterDisplay(decks);
        updateListViewCentered(myDeckListView, decks.isEmpty());
    }

    private void updateFooterDisplay(List<Deck> decks) {
        int visibilityForEmptyMyDecks = decks.isEmpty() ? View.VISIBLE : View.GONE;
        int visibilityForFeedbackButton = decks.isEmpty() ? View.GONE : View.VISIBLE;
        emptyMyDecksTitle.setVisibility(visibilityForEmptyMyDecks);
        emptyMyDecksMessage.setVisibility(visibilityForEmptyMyDecks);
        feedbackButton.setVisibility(visibilityForFeedbackButton);
    }

    private void initListFooter(List<Deck> decks) {
        setFooterClickListeners();
        updateFooterDisplay(decks);
        updateListViewCentered(myDeckListView, decks.isEmpty());
    }

    @OnClick(R.id.import_deck_button)
    public void importDeckButtonClicked() {
        importFromFile();
    }

    @OnClick(R.id.create_deck_button)
    public void createDeckButtonClicked() {
        Intent createIntent = new Intent(MyDecksActivity.this, GetStartedDeckActivity.class);
        createIntent.putExtra(AddDeckActivity.INTENT_KEY_DECK, new NewDeckContext());
        startActivityForResult(createIntent, REQUEST_CODE_CREATE_DECK);
    }

    @OnClick(R.id.feedback_button)
    public void feedbackButtonClicked() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));
    }

    private void setFooterClickListeners() {
        findViewById(R.id.my_decks_footer).setOnClickListener(null);
    }

    private void importFromFile() {
        // First try an intent specially for the Samsung file browser, as described here:
        // http://stackoverflow.com/a/17949893
        // Note that this means that, if a user has the Samsung file browser and another file
        // browser, they will not get a choice; we'll just send them to the Samsung browser.
        Intent samsungIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        samsungIntent.addCategory(Intent.CATEGORY_DEFAULT);
        if (getPackageManager().resolveActivity(samsungIntent, 0) != null) {
            startActivityForResult(samsungIntent, REQUEST_CODE_IMPORT_FILE_PICKER);
        } else {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("file/*");
            startActivityForResult(fileIntent, REQUEST_CODE_IMPORT_FILE_PICKER);
        }

    }

    private List<Deck> getDecks() {
        Deck[] decks = deckRepository.getAllDecks();
        if (decks == null) return new ArrayList<>();
        return Arrays.asList(decks);
    }

    @Override
    public void setBitmapsForActivity() {
        //// TODO: 3/31/16 We dont set bitmaps for decks. Refactor this to be in AddTranslationActivity
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_IMPORT_FILE_PICKER:
                if (resultCode != RESULT_OK) {
                    return;
                }
                Intent intent = new Intent(this, ImportActivity.class);
                intent.setData(data.getData());
                startActivityForResult(intent, REQUEST_CODE_IMPORT_FILE);
                break;
            case REQUEST_CODE_IMPORT_FILE:
                if (resultCode == RESULT_OK) {
                    refreshMyDecksList();
                }
                break;
            case REQUEST_CODE_CREATE_DECK:
                if (resultCode == RESULT_OK) {
                    refreshMyDecksList();
                }
                break;
        }
    }

    public void refreshMyDecksList() {
        List<Deck> decks = getDecks();
        updateDecksView(decks);
        updateFooterView(decks);
    }

}
