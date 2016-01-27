package org.mercycorps.translationcards;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class CollectionsActivity extends AppCompatActivity {
    private static final String FEEDBACK_URL =
            "https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/" +
                    "viewform?entry.1158658650=0.2.1";
    private DbManager dbManager;
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        initFeedbackButton();
        dbManager = new DbManager(this);
        initCollectionsList();
    }

    private void initCollectionsList() {
        ListView collectionsList = (ListView) findViewById(R.id.collections_list);
        List<Deck> decks = dbManager.getAllCollections();
        ArrayList<String> listItems = new ArrayList<>();
        listAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        collectionsList.setAdapter(listAdapter);

        for (Deck deck : decks) {
            listAdapter.add(deck.getLabel());
        }

    }

    private void initFeedbackButton() {
        ListView collectionsList = (ListView) findViewById(R.id.collections_list);

        collectionsList.addFooterView(getLayoutInflater()
                .inflate(R.layout.main_list_footer, collectionsList, false));
        findViewById(R.id.main_feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));
            }
        });
    }
}
