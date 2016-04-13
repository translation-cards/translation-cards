package org.mercycorps.translationcards.activity.refactored;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.DeckCreationActivity;
import org.mercycorps.translationcards.activity.ImportActivity;
import org.mercycorps.translationcards.activity.addDeck.GetStartedDeckActivity;
import org.mercycorps.translationcards.data.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by njimenez on 3/31/16.
 */
public class MyDecksActivity extends AbstractTranslationCardsActivity {

    private static final int REQUEST_CODE_IMPORT_FILE_PICKER = 1;
    private static final int REQUEST_CODE_IMPORT_FILE = 2;
    private static final int REQUEST_CODE_CREATE_DECK = 3;

    private static final String FEEDBACK_URL =
            "https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/" +
                    "viewform?entry.1158658650=0.3.2";

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_my_decks);
    }

    public void initStates() {
        setActionBarTitle();
        List<Deck> decks = getDecks();
        initializeFooter();
        updateDecksView(decks);
        updateEmptyTitleAndMessage(decks);
    }

    private void setActionBarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_decks);
        }
    }

    private void updateDecksView(List<Deck> decks) {
        ArrayAdapter<Deck> listAdapter = new MyDeckAdapter(MyDecksActivity.this, R.layout.deck_item, R.id.deck_name, decks);
        ListView decksListView = (ListView) findViewById(R.id.my_decks_list);
        decksListView.setAdapter(listAdapter);
    }

    private void initializeFooter() {
        ListView decksListView = (ListView) findViewById(R.id.my_decks_list);
        decksListView.addFooterView(getLayoutInflater()
                .inflate(R.layout.mydecks_footer, decksListView, false));

        findViewById(R.id.feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));

            }
        });

        findViewById(R.id.import_deck_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                fileIntent.setType("file/*");
                startActivityForResult(fileIntent, REQUEST_CODE_IMPORT_FILE_PICKER);
            }
        });

        findViewById(R.id.create_deck_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(MyDecksActivity.this, GetStartedDeckActivity.class);
                startActivityForResult(createIntent, REQUEST_CODE_CREATE_DECK);
            }
        });

    }

    private void updateEmptyTitleAndMessage(List<Deck> decks) {
        Integer viewVisibility = decks.isEmpty() ? View.VISIBLE : View.GONE;
        findViewById(R.id.empty_myDecks_message).setVisibility(viewVisibility);
        findViewById(R.id.empty_myDecks_title).setVisibility(viewVisibility);
    }

    private List<Deck> getDecks() {
        Deck[] decks = getDbManager().getAllDecks();
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
        updateEmptyTitleAndMessage(decks);
    }

}
