package org.mercycorps.translationcards.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.DbManager;

import java.util.Date;

public class DeckCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_deck_creation);
        final EditText deckNameField = (EditText) findViewById(R.id.field_deck_name);
        final EditText targetLanguagesField = (EditText) findViewById(R.id.field_target_languages);
        findViewById(R.id.button_add_deck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbManager dbm = new DbManager(DeckCreationActivity.this);
                long deckId = dbm.addDeck(
                        deckNameField.getText().toString(), // label
                        getString(R.string.misc_self), // publisher
                        (new Date()).getTime(), // timestamp
                        null, // no external ID
                        null, // hash not relevant
                        false, // not locked
                        "en"); // for now, we assume English (certainly not the least of this
                               // screen's limitations)
                String[] languages = targetLanguagesField.getText().toString().split(",");
                for (int i = 0; i < languages.length; i++) {
                    dbm.addDictionary(languages[i], i, deckId);
                }
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
