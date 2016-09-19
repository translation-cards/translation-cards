package org.mercycorps.translationcards.addDeck.activity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.presenter.EnterDeckSourceLanguagePresenter;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckSourceLanguageActivity extends AddDeckActivity implements EnterDeckSourceLanguagePresenter.EnterDeckSourceLanguageView {
    @Bind(R.id.deck_source_language_view)
    TextView sourceLanguageView;

    private EnterDeckSourceLanguagePresenter presenter;

    @Override
    public void setBitmapsForActivity() {
        presenter = new EnterDeckSourceLanguagePresenter(this, getContextFromIntent());
        presenter.inflateBitmaps();
    }

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_source_language);
    }

    @Override
    public void initStates() {
        presenter.initSourceLanguage();
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

    @OnClick(R.id.deck_source_language_next_label)
    protected void nextButtonClicked() {
        presenter.nextButtonClicked();
    }

    @OnClick(R.id.deck_source_language_back_arrow)
    public void backButtonClicked() {
        presenter.backButtonClicked();
    }

    @OnClick(R.id.deck_source_language_view)
    public void sourceLanguageClicked() {
        presenter.sourceLanguageClicked();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.newSourceLanguageSelected(resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    // EnterDeckSourceLanguageView Implementation
    @Override
    public void updateSourceLanguage(String sourceLanguage) {
        sourceLanguageView.setText(sourceLanguage);
    }
}

