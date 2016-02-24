package org.mercycorps.translationcards;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Activity for the main screen, with a list of Decks that contain translation cards
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
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
                convertView.findViewById(R.id.translation_card).setOnClickListener(new View.OnClickListener() {

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

            final Deck deck = getItem(position);
            String deckInformation = deck.getPublisher() + ", " + deck.getCreationDateString();
            TextView deckInformationView = (TextView) convertView.findViewById(R.id.deck_information);
            deckInformationView.setText(deckInformation);

            TextView translationLanguagesView =
                    (TextView) convertView.findViewById(R.id.translation_languages);
            translationLanguagesView.setText(dbManager.getTranslationLanguagesForDeck(deck.getDbId()));

            View deckCopyView = convertView.findViewById(R.id.deck_card_expansion_copy);
            if (deck.isLocked()) {
                deckCopyView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final EditText deckNameField = new EditText(DecksActivity.this);
                        new AlertDialog.Builder(DecksActivity.this)
                                .setTitle(R.string.deck_copy_dialog_title)
                                .setView(deckNameField)
                                .setPositiveButton(R.string.misc_ok,
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        copyDeck(deck, deckNameField.getText().toString());
                                        initDecks();
                                    }
                                })
                                .setNegativeButton(R.string.misc_cancel,
                                        new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {}
                                })
                                .show();
                    }
                });
            } else {
                deckCopyView.setVisibility(View.GONE);
            }

            return convertView;
        }

    }

    private void copyDeck(Deck deck, String deckName) {
        File targetDir = new File(new File(getFilesDir(), "recordings"),
                String.format("copy-%d", (new Random()).nextInt()));
        targetDir.mkdirs();
        DbManager dbm = new DbManager(this);
        long deckId = dbm.addDeck(deckName, getString(R.string.data_self_publisher), (new Date()).getTime(), null, null, false);
        Dictionary[] dictionaries = dbm.getAllDictionariesForDeck(deck.getDbId());
        int dictionaryIndex = dictionaries.length - 1;
        try {
            for (Dictionary dictionary : dictionaries) {
                long dictionaryId = dbm.addDictionary(
                        dictionary.getLabel(), dictionaryIndex, deckId);
                dictionaryIndex--;
                int translationDbIndex = dictionary.getTranslationCount() - 1;
                for (int i = 0; i < dictionary.getTranslationCount(); i++) {
                    Dictionary.Translation translation = dictionary.getTranslation(i);
                    String filename = translation.getFilename();
                    if (!translation.getIsAsset()) {
                        File srcFile = new File(filename);
                        File targetFile = new File(targetDir,
                                String.format("%s-%d",
                                        srcFile.getName(), (new Random()).nextInt()));
                        copyFile(srcFile, targetFile);
                        filename = targetFile.getAbsolutePath();
                    }
                    dbm.addTranslation(dictionaryId, translation.getLabel(), translation.getIsAsset(),
                            filename, translationDbIndex, translation.getTranslatedText());
                    translationDbIndex--;
                }
            }
        } catch (IOException e) {
            dbm.deleteDeck(deckId);
        }
    }

    private void copyFile(File src, File target) throws IOException {
        InputStream inStream = new FileInputStream(src);
        OutputStream outStream = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int read;
        while ((read = inStream.read(buf)) > 0) {
            outStream.write(buf, 0, read);
        }
        inStream.close();
        outStream.close();
    }
}
