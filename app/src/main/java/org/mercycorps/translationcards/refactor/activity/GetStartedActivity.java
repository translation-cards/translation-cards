package org.mercycorps.translationcards.refactor.activity;

import android.widget.ImageButton;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.TranslationsActivity;

import butterknife.Bind;
import butterknife.OnClick;

public class GetStartedActivity extends AddTranslationActivity {
    @Bind(R.id.get_started_title)TextView getStartedTitle;

    @Override
    protected void setActivityTitle() {
        getStartedTitle.setText(String.format(getString(R.string.get_started_title), getContextFromIntent().getDictionary().getLabel()));
    }

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_get_started);
    }

    @Override
    public void setBitmapsForActivity(){
        setBitmap(R.id.get_started_image, R.drawable.get_started_image);
    }

    @OnClick(R.id.get_started_button)
    public void getStartedButtonClick(){
        startNextActivity(GetStartedActivity.this, EnterSourcePhraseActivity.class);
    }

    @OnClick(R.id.get_started_back)
    public void getStartedBackButtonClicked(){
        startNextActivity(GetStartedActivity.this, TranslationsActivity.class);
    }

}
