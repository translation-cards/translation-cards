package org.mercycorps.translationcards;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String FEEDBACK_URL =
            "https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/" +
                    "viewform?entry.1158658650=0.2.1";
    private DbManager dbManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFeedbackButton();
        dbManager = new DbManager(this);
        initCollectionsList();
    }

    private void initCollectionsList() {
        ListView collectionsList = (ListView) findViewById(R.id.collections_list);
        List<Deck> decks = dbManager.getAllCollections();
        ArrayList<String> listItems = new ArrayList<>();
        ArrayAdapter<String> listAdapter = new CollectionsListAdapter(this,
                R.layout.collection_item, R.id.collection_name, listItems, collectionsList);
        collectionsList.setAdapter(listAdapter);

        for (Deck deck : decks) {
            listAdapter.add(deck.getLabel());
        }

    }

    private void initFeedbackButton() {
        ListView collectionsList = (ListView) findViewById(R.id.collections_list);

        collectionsList.addFooterView(getLayoutInflater()
                .inflate(R.layout.collections_footer, collectionsList, false));
        findViewById(R.id.feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));
            }
        });
    }

    private class CollectionsListAdapter extends ArrayAdapter<String> {

        public CollectionsListAdapter(Context context, int resource, int textViewResourceId,
                                      List<String> objects, ListView collectionsList) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView (int position, View convertView, ViewGroup parent){
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.collection_item, parent, false);
                convertView.findViewById(R.id.collection_name).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, TranslationsActivity.class);
                        MainActivity.this.startActivity(intent);
                    }
                });
            }

            TextView collectionName = (TextView) convertView.findViewById(R.id.collection_name);
            collectionName.setText(getItem(position));
            return convertView;
        }

    }
}
