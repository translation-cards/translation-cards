package org.mercycorps.translationcards.activity.addDeck;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;

public abstract class AddDeckActivity extends AbstractTranslationCardsActivity {
    public static final String INTENT_KEY_DECK = "Deck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
    }

    @Override
    public abstract void inflateView();

    @Override
    public void setBitmapsForActivity() {
    }

    protected void startNextActivity(Context currentContext, Class nextActivityClass) {
        finish();
        Intent nextIntent = new Intent(currentContext, nextActivityClass);
        nextIntent.putExtra(INTENT_KEY_DECK, getIntent().getSerializableExtra(INTENT_KEY_DECK));
        startActivity(nextIntent);
    }

    private void hideActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }

    protected NewDeckContext getContextFromIntent(){
        return (NewDeckContext) getObjectFromIntent(INTENT_KEY_DECK);
    }
}
