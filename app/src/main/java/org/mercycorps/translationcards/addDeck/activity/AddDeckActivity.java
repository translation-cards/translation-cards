package org.mercycorps.translationcards.addDeck.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.presenter.AddDeckView;

public abstract class AddDeckActivity extends AbstractTranslationCardsActivity implements AddDeckView {
    public static final String INTENT_KEY_DECK = "Deck";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
    }

    @Override
    public void setBitmapsForActivity() {
    }

    @Override
    public abstract void inflateView();

    public void startNextActivity(Context currentContext, Class nextActivityClass) {
        finish();
        Intent nextIntent = new Intent(currentContext, nextActivityClass);
        nextIntent.putExtra(INTENT_KEY_DECK, getIntent().getParcelableExtra(INTENT_KEY_DECK));
        startActivity(nextIntent);
    }

    private void hideActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }

    protected NewDeckContext getContextFromIntent(){
        return (NewDeckContext) getObjectFromIntent(INTENT_KEY_DECK);
    }

    // AddDeckView Implementation
    @Override
    public void setActivityBitmap(int resId, int drawableId) {
        setBitmap(resId, drawableId);
    }

    @Override
    public void startActivityWithClass(Class<? extends Activity> nextActivityClass) {
        startNextActivity(this, nextActivityClass);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {
        startActivityForResult(new Intent(this, activityClass), requestCode);
    }
}
