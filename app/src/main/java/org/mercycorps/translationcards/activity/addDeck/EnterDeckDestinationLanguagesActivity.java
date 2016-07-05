package org.mercycorps.translationcards.activity.addDeck;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;

public class EnterDeckDestinationLanguagesActivity extends AddDeckActivity {
    public static final int REQUEST_CODE = 0;
    private NewDeckContext newDeckContext;
    @Bind(R.id.enter_destination_next_label)
    LinearLayout nextButton;
    @Bind(R.id.enter_destination_next_text)
    TextView nextButtonText;
    @Bind(R.id.enter_destination_next_image)
    ImageView nextButtonImage;
    @Bind(R.id.language_chip_grid)
    GridView languageChipGridView;
    private DestinationLanguagesAdapter languageChipAdapter;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_deck_destination_languages);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_deck_destination_image, R.drawable.enter_phrase_image);
    }

    @Override
    public void initStates() {
        newDeckContext = getContextFromIntent();
        languageChipAdapter = new DestinationLanguagesAdapter(this, new ArrayList<>(newDeckContext.getDestinationLanguages()));
        languageChipGridView.setAdapter(languageChipAdapter);
        updateNextButtonState();
    }

    @OnClick(R.id.enter_destination_next_label)
    public void nextButtonClicked() {
        if (!nextButton.isClickable()) {
            return;
        }
        updateContextWithLanguagesInput();

        startNextActivity(EnterDeckDestinationLanguagesActivity.this, EnterAuthorActivity.class);
    }

    @OnClick(R.id.enter_destination_back_arrow)
    public void backButtonClicked() {
        updateContextWithLanguagesInput();
        startNextActivity(this, EnterDeckSourceLanguageActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String selectedLanguage = data.getStringExtra(DestinationLanguageSelectorActivity.SELECTED_LANGUAGE_KEY);
        languageChipAdapter.add(selectedLanguage);
        updateNextButtonState();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.add_language_chip)
    public void showLanguageSelector() {
        startActivityForResult(new Intent(this, DestinationLanguageSelectorActivity.class), REQUEST_CODE);
    }

    private void updateContextWithLanguagesInput() {
        newDeckContext.addDestinationLanguages(languageChipAdapter.getLanguages());
    }

    protected void updateNextButtonState() {
        boolean hasLanguages = !languageChipAdapter.getLanguages().isEmpty();
        int backgroundResource = hasLanguages ? R.drawable.forward_arrow : R.drawable.forward_arrow_disabled;
        int buttonColor = hasLanguages ? R.color.primaryTextColor : R.color.textDisabled;
        nextButton.setClickable(hasLanguages);
        nextButtonText.setTextColor(ContextCompat.getColor(this, buttonColor));
        nextButtonImage.setBackgroundResource(backgroundResource);
    }
}
