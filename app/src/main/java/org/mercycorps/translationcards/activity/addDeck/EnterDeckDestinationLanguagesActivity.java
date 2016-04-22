package org.mercycorps.translationcards.activity.addDeck;

import android.widget.EditText;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckDestinationLanguagesActivity extends AddDeckActivity{
    private static final String LANGUAGE_DELIMITER = ",";
    @Bind(R.id.enter_deck_destination_input) EditText input;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_destination_languages);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_deck_destination_image, R.drawable.enter_phrase_image);
    }

    @Override
    public void initStates(){

    }

    @OnClick(R.id.enter_destination_next_label)
    public void nextButtonClicked(){
        addDictionariesToNewDeckContext();
        getContextFromIntent().save();
        startNextActivity(EnterDeckDestinationLanguagesActivity.this, MyDecksActivity.class);
    }

    protected void addDictionariesToNewDeckContext() {
        String[] languages = input.getText().toString().toLowerCase().split(LANGUAGE_DELIMITER);
        for (String language : languages) {
            getContextFromIntent().addLanguage(language.trim());
        }
    }

    @OnClick(R.id.enter_destination_back_arrow)
    public void backButtonClicked() {
        addDictionariesToNewDeckContext();
        startNextActivity(EnterDeckDestinationLanguagesActivity.this, EnterDeckTitleActivity.class);
    }
}
