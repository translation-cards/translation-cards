package org.mercycorps.translationcards.activity.addTranslation;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.TranslationsActivity;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.media.DecoratedMediaManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.mercycorps.translationcards.uiHelper.ToastHelper.showToast;

public class SummaryActivity extends AddTranslationActivity {
    private static final String TAG = "SummaryActivity";
    @Bind(R.id.origin_translation_text)TextView sourceTextView;
    @Bind(R.id.translated_text)TextView translatedTextView;
    @Bind(R.id.translation_child)LinearLayout translationChildLayout;
    @Bind(R.id.translation_grandchild)LinearLayout translationGrandchildLayout;
    @Bind(R.id.summary_title)TextView summaryTitle;
    @Bind(R.id.summary_detail)TextView summaryDetail;
    @Bind(R.id.summary_progress_bar)ProgressBar progressBar;
    @Bind(R.id.indicator_icon)ImageView indicatorIcon;

    @Override
    protected void setActivityTitle() {
        updateTextInTextView(summaryTitle, String.format(getString(R.string.summary_title), getContextFromIntent().getDictionary().getLabel()));
        updateTextInTextView(summaryDetail, String.format(getString(R.string.summary_detail), getContextFromIntent().getDictionary().getLabel()));
    }

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_summary);
    }

    @Override
    public void initStates(){
        setTranslationCardChildrenVisibility();
        updateTextInTextView(sourceTextView, getContextFromIntent().getTranslation().getLabel());
        updateTranslatedTextView();
        indicatorIcon.setBackgroundResource(R.drawable.collapse_arrow);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.summary_image, R.drawable.summary_image);
    }

    private void setTranslationCardChildrenVisibility() {
        translationChildLayout.setVisibility(View.VISIBLE);
        translationGrandchildLayout.setVisibility(View.GONE);
    }

    static final ButterKnife.Setter<View, Boolean> INVISIBLE = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };

    private void updateTranslatedTextView() {
        String translatedText = getContextFromIntent().getTranslation().getTranslatedText();
        if (translatedText.isEmpty()) {
            translatedTextView.setHint(String.format("Add %s translation", getContextFromIntent().getDictionary().getLabel()));
        } else {
            updateTextInTextView(translatedTextView, translatedText);
        }
    }

    private void updateTextInTextView(TextView textView, String textToBeUpdated){
        textView.setText(textToBeUpdated);
    }

    @OnClick(R.id.save_translation_button)
    protected void summaryDoneClicked() {
        saveTranslation();
        stopMediaManager();
        startNextActivity(SummaryActivity.this, TranslationsActivity.class);
    }

    private void stopMediaManager() {
        DecoratedMediaManager mediaManager = getDecoratedMediaManager();
        if (mediaManager.isPlaying()) {
            mediaManager.stop();
        }
    }

    @OnClick(R.id.summary_translation_card)
    protected void translationCardClicked() {
        try {
            DecoratedMediaManager mediaManager = getDecoratedMediaManager();
            if(mediaManager.isPlaying()) mediaManager.stop();
            else mediaManager.play(getContextFromIntent().getTranslation().getFilename(), progressBar);
        } catch (AudioFileException e) {
            showToast(getString(R.string.could_not_play_audio_message));
            Log.d(TAG, getString(R.string.could_not_play_audio_message));
        }
    }

    @OnClick(R.id.go_to_enter_translated_phrase_activity)
    protected void summaryBackClicked(){
        stopMediaManager();
        startNextActivity(SummaryActivity.this, EnterTranslatedPhraseActivity.class);
    }

    @OnClick(R.id.translation_indicator_layout)
    protected void indicatorLayoutClicked() {
        int visibility = isTranslationChildVisible() ? View.GONE : View.VISIBLE;
        int backgroundResource = isTranslationChildVisible() ? R.drawable.expand_arrow : R.drawable.collapse_arrow;
        translationChildLayout.setVisibility(visibility);
        indicatorIcon.setBackgroundResource(backgroundResource);
    }

    private boolean isTranslationChildVisible() {
        return translationChildLayout.getVisibility() == View.VISIBLE;
    }

    private DecoratedMediaManager getDecoratedMediaManager(){
        return getMainApplication().getDecoratedMediaManager();
    }
    private void saveTranslation() {
        getMainApplication().getDbManager().saveTranslationContext(getContextFromIntent());
    }
}