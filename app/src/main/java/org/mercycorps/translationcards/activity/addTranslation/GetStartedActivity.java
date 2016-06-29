package org.mercycorps.translationcards.activity.addTranslation;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;

import butterknife.OnClick;

public class GetStartedActivity extends AddTranslationActivity {

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_card_get_started);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.get_started_image, R.drawable.get_started_image);
    }

    @OnClick(R.id.get_started_button)
    public void getStartedButtonClick() {
            startNextActivity(GetStartedActivity.this, EnterSourcePhraseActivity.class);
    }

    @OnClick(R.id.get_started_back)
    public void getStartedBackButtonClicked() {
        startNextActivity(GetStartedActivity.this, TranslationsActivity.class);
    }

}
