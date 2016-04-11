package org.mercycorps.translationcards.activity.addDeck;

import android.content.Context;
import android.content.Intent;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;

public abstract class AddDeckActivity extends AbstractTranslationCardsActivity {
    public static final String INTENT_KEY_DECK = "Deck";

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
}
