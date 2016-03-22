package org.mercycorps.translationcards.refactor.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.mercycorps.translationcards.R;

public class GetStartedActivity extends AppCompatActivity {

    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);
        setupClickListeners();
    }

    private void setupClickListeners(){
        Button startButton = (Button) findViewById(R.id.get_started_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nextIntent = new Intent(GetStartedActivity.this, EnterSourcePhraseActivity.class);
                nextIntent.putExtra(CONTEXT_INTENT_KEY, getIntent().getSerializableExtra(CONTEXT_INTENT_KEY));
                startActivity(nextIntent);
            }
        });
    }
}
