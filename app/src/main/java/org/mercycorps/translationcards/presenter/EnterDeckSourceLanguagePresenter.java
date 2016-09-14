package org.mercycorps.translationcards.presenter;

import android.app.Activity;
import android.content.Intent;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckDestinationLanguagesActivity;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckTitleActivity;
import org.mercycorps.translationcards.activity.addDeck.LanguageSelectorActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;

public class EnterDeckSourceLanguagePresenter {
    private final int REQUEST_CODE = 0;
    private EnterDeckSourceLanguageView activity;
    private NewDeckContext newDeckContext;

    public EnterDeckSourceLanguagePresenter(EnterDeckSourceLanguageView activity, NewDeckContext newDeckContext) {
        this.activity = activity;
        this.newDeckContext = newDeckContext;
    }

    public void inflateBitmaps() {
        activity.setActivityBitmap(R.id.deck_source_language_image, R.drawable.enter_phrase_image);
    }

    public void newSourceLanguageSelected(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String selectedLanguage = data.getStringExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY);
            if (selectedLanguage != null) {
                newDeckContext.setSourceLanguage(selectedLanguage);
                activity.updateSourceLanguage(selectedLanguage);
            }
        }
    }

    public void initSourceLanguage() {
        activity.updateSourceLanguage(newDeckContext.getSourceLanguage());
    }

    public void nextButtonClicked() {
        activity.startActivityWithClass(EnterDeckDestinationLanguagesActivity.class);
    }

    public void backButtonClicked() {
        activity.startActivityWithClass(EnterDeckTitleActivity.class);
    }

    public void sourceLanguageClicked() {
        activity.startActivityForResult(LanguageSelectorActivity.class, REQUEST_CODE);
    }

    public interface EnterDeckSourceLanguageView {
        void setActivityBitmap(int resId, int drawableId);
        void startActivityWithClass(Class<? extends Activity> activityClass);
        void updateSourceLanguage(String sourceLanguage);
        void startActivityForResult(Class<? extends Activity> activityClass, int requestCode);
    }
}
