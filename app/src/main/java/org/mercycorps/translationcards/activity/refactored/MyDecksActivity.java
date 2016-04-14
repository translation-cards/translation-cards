package org.mercycorps.translationcards.activity.refactored;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.DeckCreationActivity;
import org.mercycorps.translationcards.activity.ImportActivity;
import org.mercycorps.translationcards.data.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by njimenez on 3/31/16.
 */
public class MyDecksActivity extends AbstractTranslationCardsActivity {

    private static final int REQUEST_CODE_IMPORT_FILE_PICKER = 1;
    private static final int REQUEST_CODE_IMPORT_FILE = 2;
    private static final int REQUEST_CODE_CREATE_DECK = 3;

    @Bind(R.id.my_decks_list)
    ListView myDeckListView;

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
        initListFooter(decks);
        updateDecksView(decks);
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

    private void updateFooterView(List<Deck> decks) {
        updateFooterDisplay(decks);
        updateFooterPosition(decks);
    }

    private void updateFooterDisplay(List<Deck> decks) {
        int visibilityForEmptyMyDecks = decks.isEmpty() ? View.VISIBLE : View.GONE;
        int visibilityForFeedbackButton = decks.isEmpty() ? View.GONE : View.VISIBLE;
        findViewById(R.id.empty_my_decks_title).setVisibility(visibilityForEmptyMyDecks);
        findViewById(R.id.empty_my_decks_message).setVisibility(visibilityForEmptyMyDecks);
        findViewById(R.id.feedback_button).setVisibility(visibilityForFeedbackButton);
    }

    private void initListFooter(List<Deck> decks) {
        inflateListFooter();
        setFooterClickListeners();
        updateFooterDisplay(decks);
        updateFooterPosition(decks);
    }

    private void updateFooterPosition(List<Deck> decks) {
        int isCentered = decks.isEmpty() ? RelativeLayout.TRUE : 0;
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myDeckListView.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT, isCentered);
        myDeckListView.setLayoutParams(params);
    }

    private void inflateListFooter() {
        View footerView = getLayoutInflater().inflate(R.layout.my_decks_footer, myDeckListView, false);
        myDeckListView.addFooterView(footerView);
    }

    private void setFooterClickListeners() {
        findViewById(R.id.import_deck_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importFromFile();
            }
        });

        findViewById(R.id.create_deck_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent = new Intent(MyDecksActivity.this, DeckCreationActivity.class);
                startActivityForResult(createIntent, REQUEST_CODE_CREATE_DECK);
            }
        });

        findViewById(R.id.feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));

            }
        });
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
        updateFooterView(decks);
    }

}
