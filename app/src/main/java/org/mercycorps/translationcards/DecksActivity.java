package org.mercycorps.translationcards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DecksActivity extends AppCompatActivity {
    private static final String FEEDBACK_URL =
            "https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/" +
                    "viewform?entry.1158658650=0.2.1";
    public static final String INTENT_KEY_DECK_ID = "Deck";
    private DbManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decks);
        initFeedbackButton();
        dbManager = new DbManager(this);
        initDecks();
        getSupportActionBar().setTitle(R.string.my_decks);
        getSupportActionBar().setElevation(0);
    }

    private void initDecks() {
        ListView decksListView = (ListView) findViewById(R.id.decks_list);
        ArrayList<Deck> listItems = new ArrayList<>();
        ArrayAdapter<Deck> listAdapter = new DecksAdapter(this,
                R.layout.deck_item, R.id.deck_name, listItems, decksListView);
        decksListView.setAdapter(listAdapter);

        for (Deck deck : dbManager.getAllDecks()) {
            listAdapter.add(deck);
        }

    }

    private void initFeedbackButton() {
        ListView decksListView = (ListView) findViewById(R.id.decks_list);

        decksListView.addFooterView(getLayoutInflater()
                .inflate(R.layout.decks_footer, decksListView, false));
        findViewById(R.id.feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));
            }
        });
    }

    private class DecksAdapter extends ArrayAdapter<Deck> {

        private ListView decks;

        public DecksAdapter(Context context, int resource, int textViewResourceId,
                            List<Deck> objects, ListView decks) {
            super(context, resource, textViewResourceId, objects);
            this.decks = decks;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.deck_item, parent, false);
                convertView.findViewById(R.id.deck_card).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent decksIntent = new Intent(DecksActivity.this, TranslationsActivity.class);
                        decksIntent.putExtra(INTENT_KEY_DECK_ID, getItem(position));
                        DecksActivity.this.startActivity(decksIntent);
                    }
                });
            }

            TextView deckName = (TextView) convertView.findViewById(R.id.deck_name);
            deckName.setText(getItem(position).getLabel());

            Deck deck = getItem(position);
            String deckInformation = deck.getPublisher() + ", " + deck.getCreationDateString();
            TextView deckInformationView = (TextView) convertView.findViewById(R.id.deck_information);
            deckInformationView.setText(deckInformation);

            TextView translationLanguagesView =
                    (TextView) convertView.findViewById(R.id.translation_languages);
            translationLanguagesView.setText(dbManager.getTranslationLanguagesForDeck(deck.getDbId()));
            return convertView;
        }

    }
}
