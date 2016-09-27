package org.mercycorps.translationcards.addDeck.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.presenter.EnterDeckDestinationPresenter;
import org.mercycorps.translationcards.view.Chip;
import org.mercycorps.translationcards.view.NextButton;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckDestinationLanguagesActivity extends AddDeckActivity implements EnterDeckDestinationPresenter.EnterDeckDestinationView {
    private EnterDeckDestinationPresenter presenter;

    @Bind(R.id.next_button)NextButton nextButton;
    @Bind(R.id.language_chip_flexbox)FlexboxLayout flexboxLayout;
    @Bind(R.id.add_language_button)TextView addLanguageButton;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_destination_languages);
        presenter = new EnterDeckDestinationPresenter(this, getContextFromIntent());
    }

    @Override
    public void setBitmapsForActivity() {
        presenter.setBitmaps();
    }

    @Override
    public void initStates() {
        formatAddLanguageButton();
        presenter.refreshView();
    }

    @OnClick(R.id.next_button)
    public void nextButtonClicked() {
        presenter.nextButtonClicked();
    }

    @OnClick(R.id.enter_destination_back_arrow)
    public void backButtonClicked() {
        presenter.backButtonClicked();
    }

    @OnClick(R.id.add_language_button)
    public void showLanguageSelector() {
        presenter.addNewDestinationLanguageClicked();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String selectedLanguage = data.getStringExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY);
            presenter.newLanguageSelected(selectedLanguage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void enableNextButton() {
        nextButton.enable();
    }

    @Override
    public void disableNextButton() {
        nextButton.disable();
    }

    @Override
    public void addLanguageChip(final String language) {
        Chip chip = new Chip(this);
        chip.setText(language);
        chip.setOnDeleteListener(new Chip.OnDeleteListener() {
            @Override
            public void delete() {
                presenter.deleteLanguage(language);
            }
        });
        flexboxLayout.addView(chip);
    }

    @Override
    public void removeAllLanguageChips() {
        flexboxLayout.removeAllViews();
    }

    private void formatAddLanguageButton() {
        BitmapDrawable img = (BitmapDrawable) ContextCompat.getDrawable(this, R.drawable.ic_control_point_white_24dp);
        if (img != null) {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(img.getBitmap(), densityPixelsToPixels(20), densityPixelsToPixels(20), false);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), scaledBitmap);
            bitmapDrawable.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
            addLanguageButton.setCompoundDrawablesWithIntrinsicBounds(bitmapDrawable, null, null, null);
            addLanguageButton.setCompoundDrawablePadding(densityPixelsToPixels(5));
        }
    }
}
