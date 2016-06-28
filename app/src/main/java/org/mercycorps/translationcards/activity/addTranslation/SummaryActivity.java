package org.mercycorps.translationcards.activity.addTranslation;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.media.DecoratedMediaManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.mercycorps.translationcards.fragment.TranslationTabsFragment.*;

public class SummaryActivity extends AddTranslationActivity {
    private static final String TAG = "SummaryActivity";
    @Bind(R.id.origin_translation_text)TextView sourceTextView;
    @Bind(R.id.translated_text)TextView translatedTextView;
    @Bind(R.id.translation_child)LinearLayout translationChildLayout;
    @Bind(R.id.translation_grandchild)LinearLayout translationGrandchildLayout;
    @Bind(R.id.summary_detail)TextView summaryDetail;
    @Bind(R.id.summary_progress_bar)ProgressBar progressBar;
    @Bind(R.id.indicator_icon)ImageView indicatorIcon;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_summary);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.enter_source_language_image, R.drawable.enter_source_language_image);
    }

    @Override
    public void initStates(){
        inflateLanguageTabsFragment();
        setOnLanguageTabClickListener();
        setTranslationCardChildrenVisibility();
        updateSourceTextView();
        updateTranslatedTextView();
        updateSummaryTextView();
        indicatorIcon.setBackgroundResource(R.drawable.collapse_arrow);
    }


    @OnClick(R.id.save_translation_button)
    protected void summaryDoneClicked() {
        saveTranslation();
        stopMediaManager();
        startNextActivity(SummaryActivity.this, TranslationsActivity.class);
    }

    @OnClick(R.id.summary_translation_card)
    protected void translationCardClicked() {
        Translation translation = getLanguageTabsFragment().getCurrentTranslation().getTranslation();
        try {
            DecoratedMediaManager mediaManager = getDecoratedMediaManager();
            if(mediaManager.isPlaying()) {
                mediaManager.stop();
            } else {
                mediaManager.play(translation.getFilename(), progressBar, translation.getIsAsset());
            }
        } catch (AudioFileException e) {
            String message = String.format(getString(R.string.could_not_play_audio_message),
                    getLanguageTabsFragment().getCurrentTranslation().getDictionary().getLanguage());
            showToast(message);
            Log.d(TAG, e.getMessage());
        }
    }

    @OnClick(R.id.summary_activity_back)
    protected void summaryBackClicked(){
        stopMediaManager();
        startNextActivity(SummaryActivity.this, RecordAudioActivity.class);
    }

    @OnClick(R.id.translation_indicator_layout)
    protected void indicatorLayoutClicked() {
        int visibility = isTranslationChildVisible() ? View.GONE : View.VISIBLE;
        int backgroundResource = isTranslationChildVisible() ? R.drawable.expand_arrow : R.drawable.collapse_arrow;
        translationChildLayout.setVisibility(visibility);
        indicatorIcon.setBackgroundResource(backgroundResource);
    }

    private void updateSummaryTextView() {
        updateSourceTextView();
        String translatedText = getLanguageTabsFragment().getCurrentTranslation().getTranslation().getTranslatedText();
        int detailText = translatedText.isEmpty() ? R.string.summary_detail_no_audio : R.string.activity_summary_instructions;
        summaryDetail.setText(detailText);
    }

    private void setOnLanguageTabClickListener() {
        getLanguageTabsFragment().setOnLanguageTabSelectedListener(new OnLanguageTabSelectedListener() {
            @Override
            public void onLanguageTabSelected(NewTranslation previousTranslation) {
                updateTranslatedTextView();
                updateSummaryTextView();
                stopMediaManager();
            }
        });
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
        String translatedText = getLanguageTabsFragment().getCurrentTranslation().getTranslation().getTranslatedText();
        if (translatedText.isEmpty()) {
            translatedTextView.setHint(String.format("Add %s translation", getLanguageTabsFragment().getCurrentTranslation().getDictionary().getLanguage()));
        }

        updateTextInTextView(translatedTextView, translatedText);
    }
    private void updateSourceTextView() {
        sourceTextView.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor));
        if(!getLanguageTabsFragment().getCurrentTranslation().getTranslation().isAudioFilePresent()){
            sourceTextView.setTextColor(ContextCompat.getColor(this, R.color.textDisabled));
        }
        updateTextInTextView(sourceTextView, getContextFromIntent().getSourcePhrase());
    }

    private void updateTextInTextView(TextView textView, String textToBeUpdated){
        textView.setText(textToBeUpdated);
    }

    private void stopMediaManager() {
        DecoratedMediaManager mediaManager = getDecoratedMediaManager();
        if (mediaManager.isPlaying()) {
            mediaManager.stop();
        }
    }

    private boolean isTranslationChildVisible() {
        return translationChildLayout.getVisibility() == View.VISIBLE;
    }

    private DecoratedMediaManager getDecoratedMediaManager(){
        return getMainApplication().getDecoratedMediaManager();
    }

    private void saveTranslation() {
        getContextFromIntent().save();
    }
}
