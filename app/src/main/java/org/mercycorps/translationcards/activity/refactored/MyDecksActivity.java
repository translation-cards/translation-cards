package org.mercycorps.translationcards.activity.refactored;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.DeckCreationActivity;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by njimenez on 3/31/16.
 */
public class MyDecksActivity extends AbstractTranslationCardsActivity {
    @Bind(R.id.empty_myDecks_title)TextView emptyMyDecksTitle;
    @Bind(R.id.empty_myDecks_message)TextView emptyMyDecksMessage;

    @Bind({ R.id.empty_myDecks_title, R.id.empty_myDecks_message})
    List<TextView> emptyTitleAndMessage;

    private static final int REQUEST_CODE_IMPORT_FILE_PICKER = 1;
    private static final int REQUEST_CODE_CREATE_DECK = 3;

    private static final String FEEDBACK_URL =
            "https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/" +
                    "viewform?entry.1158658650=0.3.2";
    @Override
    public void inflateView() {
        setContentView(R.layout.activity_my_decks);
    }

    public void initStates(){
        updateEmptyTitleAndMessage();
        updateDecksView();
    }

    private void updateDecksView() {
        List<Deck> decks = getDecks();
        ArrayAdapter<Deck> listAdapter = new MyDeckAdapter(this, R.layout.deck_item, R.id.deck_name, decks);
        ListView decksListView = (ListView) findViewById(R.id.decks_list);
        decksListView.setAdapter(listAdapter);
    }

    private void updateEmptyTitleAndMessage(){
        ButterKnife.apply(emptyTitleAndMessage, VISIBILITY, getVisibility());
    }

    private List<Deck> getDecks(){
        Deck[] decks = getDbManager().getAllDecks();
        if(decks == null) return new ArrayList<>();
        return Arrays.asList(decks);
    }

    @Override
    public void setBitmapsForActivity() {
        //// TODO: 3/31/16 We dont set bitmaps for decks. Refactor this to be in AddTranslationActivity
    }

    @OnClick(R.id.feedback_button)
    public void feedbackButtonClicked(){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));
    }

    @OnClick(R.id.import_deck_button)
    public void importButtonClicked(){
        Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        fileIntent.setType("file/*");
        startActivityForResult(fileIntent, REQUEST_CODE_IMPORT_FILE_PICKER);
    }

    @OnClick(R.id.create_deck_button)
    public void createDeckButtonClicked(){
        Intent createIntent = new Intent(MyDecksActivity.this, DeckCreationActivity.class);
        startActivityForResult(createIntent, REQUEST_CODE_CREATE_DECK);
    }
    private int getVisibility(){
        return getDecks().isEmpty() ? View.VISIBLE : View.GONE;
    }

}
