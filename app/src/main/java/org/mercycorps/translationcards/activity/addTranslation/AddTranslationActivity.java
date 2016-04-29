package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.fragment.TranslationTabsFragment;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import java.util.ArrayList;


public abstract class AddTranslationActivity extends AbstractTranslationCardsActivity {
    public static final String CONTEXT_INTENT_KEY = "AddNewTranslationContext";
    public static final String INTENT_KEY_DECK_ID = "Deck";

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

    public abstract void inflateView();

    public void setBitmapsForActivity() {
        //Override this if you want to set bitmaps for your activity
    }

    protected AddNewTranslationContext getContextFromIntent() {
        return (AddNewTranslationContext) getObjectFromIntent(CONTEXT_INTENT_KEY);
    }

    protected void startNextActivity(Context currentContext, Class nextActivityClass) {
        finish();
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

    private void hideActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }

    protected void inflateLanguageTabsFragment() {
        Fragment translationTabsFragment = new TranslationTabsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putSerializable(CONTEXT_INTENT_KEY, getContextFromIntent());
        translationTabsFragment.setArguments(arguments);
        transaction.replace(R.id.language_tabs_fragment, translationTabsFragment);
        transaction.commit();
    }
}
