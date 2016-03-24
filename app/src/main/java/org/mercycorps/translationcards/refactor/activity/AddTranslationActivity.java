package org.mercycorps.translationcards.refactor.activity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import java.io.File;

/**
 * Created by karthikbalasubramanian on 3/23/16.
 */
public abstract class AddTranslationActivity extends AbstractTranslationCardsActivity {
    public static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    public static final String INTENT_KEY_DECK_ID = "Deck";
    private Bitmap currentBitmap;
    private ImageView currentBitmapView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideActionBar();
        setActivityTitle();
    }

    protected abstract void setActivityTitle();

    private void hideActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }

    public abstract void inflateView();

    public void setBitmapsForActivity() {
        //Override this if you want to set bitmaps for your activity
    }

    protected NewTranslationContext getContextFromIntent() {
        return (NewTranslationContext) getObjectFromIntent(CONTEXT_INTENT_KEY);
    }

    protected void startNextActivity(Context currentContext, Class nextActivityClass) {
        recycleBitmap(); //// TODO: 3/24/16 Does it belong here?
        Intent nextIntent = new Intent(currentContext, nextActivityClass);
        nextIntent.putExtra(CONTEXT_INTENT_KEY, getIntent().getSerializableExtra(CONTEXT_INTENT_KEY));
        nextIntent.putExtra(INTENT_KEY_DECK_ID, getIntent().getSerializableExtra(INTENT_KEY_DECK_ID));
        startActivity(nextIntent);
    }

    protected void showToast(String message) {
        ToastHelper.showToast(getApplicationContext(), message);
    }

    protected MainApplication getMainApplication() {
        return ((MainApplication) getApplication());
    }

    protected void setBitmap(int resId, int drawableId) {
        currentBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        currentBitmapView = (ImageView) findViewById(resId);
        currentBitmapView.setImageBitmap(currentBitmap);
    }

    private void recycleBitmap() {
        if (currentBitmap != null) {
            currentBitmap.recycle();
            if (currentBitmapView != null) currentBitmapView.setImageBitmap(null);
            currentBitmap = null;
            System.gc();
        }
    }
}
