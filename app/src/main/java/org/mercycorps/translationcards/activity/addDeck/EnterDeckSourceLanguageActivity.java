package org.mercycorps.translationcards.activity.addDeck;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.service.LanguageService;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckSourceLanguageActivity extends AddDeckActivity {
    public static final int REQUEST_CODE = 0;
    @Bind(R.id.deck_source_language_view)
    TextView sourceLanguageView;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_source_language);
    }

    @Override
    public void initStates() {
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);
        fillSourceLanguageField();
        formatLanguageButton();
    }

    private void formatLanguageButton() {
        BitmapDrawable img = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_mode_edit_white_24dp);
        if (img != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(img.getBitmap(), densityPixelsToPixels(20), densityPixelsToPixels(20), false);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), scaledBitmap);
            bitmapDrawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            sourceLanguageView.setCompoundDrawablesWithIntrinsicBounds(bitmapDrawable, null, null, null);
            sourceLanguageView.setCompoundDrawablePadding(densityPixelsToPixels(5));
        }
    }

    private void fillSourceLanguageField() {
        sourceLanguageView.setText(LanguageService.getTitleCaseName(getContextFromIntent().getSourceLanguage()));
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.deck_source_language_image, R.drawable.enter_phrase_image);
    }

    @OnClick(R.id.deck_source_language_next_label)
    protected void nextButtonClicked() {
        updateContextWithSourceLanguage();
        startNextActivity(this, EnterDeckDestinationLanguagesActivity.class);
    }

    @OnClick(R.id.deck_source_language_back_arrow)
    public void backButtonClicked() {
        updateContextWithSourceLanguage();
        startNextActivity(this, EnterDeckTitleActivity.class);
    }

    @OnClick(R.id.deck_source_language_view)
    public void sourceLanguageClicked() {
        startActivityForResult(new Intent(this, LanguageSelectorActivity.class), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            String selectedLanguage = data.getStringExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY);
            if (selectedLanguage != null) {
                sourceLanguageView.setText(selectedLanguage);
                updateContextWithSourceLanguage();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateContextWithSourceLanguage() {
        getContextFromIntent().setSourceLanguage(sourceLanguageView.getText().toString());
    }
}

