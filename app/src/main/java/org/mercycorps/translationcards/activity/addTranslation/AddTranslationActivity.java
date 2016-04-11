package org.mercycorps.translationcards.activity.addTranslation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

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
    }

    @Override
    protected void onDestroy() {
        recycleBitmap();
        super.onDestroy();
    }

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
        finish(); //Calling finish is going to call onDestroy() on Activity
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

    protected AudioPlayerManager getAudioPlayerManager(){
        return getMainApplication().getAudioPlayerManager();
    }

    protected AudioRecorderManager getAudioRecorderManager(){
        return getMainApplication().getAudioRecorderManager();
    }

    protected void setBitmap(int resId, int drawableId) {
        recycleBitmap();
        currentBitmap = BitmapFactory.decodeResource(getResources(), drawableId);
        currentBitmapView = (ImageView) findViewById(resId);
        currentBitmapView.setImageBitmap(currentBitmap);
    }

    protected void recycleBitmap() {
        if (currentBitmap != null) {
            currentBitmap.recycle();
            if (currentBitmapView != null) currentBitmapView.setImageBitmap(null);
            currentBitmap = null;
            System.gc();
        }
    }
}
