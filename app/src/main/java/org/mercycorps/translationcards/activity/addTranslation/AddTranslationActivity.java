package org.mercycorps.translationcards.activity.addTranslation;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.fragment.TranslationTabsFragment;


public abstract class AddTranslationActivity extends AbstractTranslationCardsActivity {
    public static final String CONTEXT_INTENT_KEY = "AddNewTranslationContext";
    public static final String INTENT_KEY_DECK_ID = "Deck";
    private TranslationTabsFragment translationTabsFragment;

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
        nextIntent.putExtra(CONTEXT_INTENT_KEY, getIntent().getParcelableExtra(CONTEXT_INTENT_KEY));
        nextIntent.putExtra(INTENT_KEY_DECK_ID, getIntent().getParcelableExtra(INTENT_KEY_DECK_ID));
        startActivity(nextIntent);
    }

    private void hideActionBar() {
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.hide();
    }

    protected void inflateLanguageTabsFragment() {
        translationTabsFragment = new TranslationTabsFragment();
        translationTabsFragment.setCurrentTranslation(getContextFromIntent().getNewTranslations().get(0));
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putParcelable(CONTEXT_INTENT_KEY, getContextFromIntent());
        translationTabsFragment.setArguments(arguments);
        transaction.replace(R.id.language_tabs_fragment, translationTabsFragment);
        transaction.commit();
    }

    protected TranslationTabsFragment getLanguageTabsFragment() {
        return translationTabsFragment;
    }


}
