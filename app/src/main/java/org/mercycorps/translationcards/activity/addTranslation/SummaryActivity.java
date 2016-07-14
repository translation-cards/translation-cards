package org.mercycorps.translationcards.activity.addTranslation;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.view.TranslationCardItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.mercycorps.translationcards.fragment.TranslationTabsFragment.OnLanguageTabSelectedListener;

public class SummaryActivity extends AddTranslationActivity {
    private static final String TAG = "SummaryActivity";
    @Bind(R.id.summary_detail)TextView summaryDetail;
    @Bind(R.id.summary_progress_bar)ProgressBar progressBar;
    @Bind(R.id.translation_card_item)TranslationCardItem translationCard;

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
        updateSummaryTextView();
        updateTranslationCardView();
    }
    private void updateTranslationCardView(){
        Translation translation = getLanguageTabsFragment().getCurrentTranslation().getTranslation();
        String language = getLanguageTabsFragment().getCurrentTranslation().getDictionary().getLanguage();
        translationCard.setTranslation(translation, language);
    }

    @OnClick(R.id.save_translation_button)
    protected void summaryDoneClicked() {
        saveTranslation();
        stopMediaManager();
        startNextActivity(SummaryActivity.this, TranslationsActivity.class);
    }

    @OnClick(R.id.translation_card_item)
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

    private void updateSummaryTextView() {
        boolean audioRecorded = getLanguageTabsFragment().getCurrentTranslation().getTranslation().isAudioFilePresent();
        int detailText = audioRecorded ? R.string.activity_summary_instructions : R.string.summary_detail_no_audio;
        summaryDetail.setText(detailText);
    }

    private void setOnLanguageTabClickListener() {
        getLanguageTabsFragment().setOnLanguageTabSelectedListener(new OnLanguageTabSelectedListener() {
            @Override
            public void onLanguageTabSelected(NewTranslation previousTranslation) {
                updateSummaryTextView();
                stopMediaManager();
                updateTranslationCardView();
            }
        });
    }


    static final ButterKnife.Setter<View, Boolean> INVISIBLE = new ButterKnife.Setter<View, Boolean>() {
        @Override public void set(View view, Boolean value, int index) {
            view.setEnabled(value);
        }
    };

    private void stopMediaManager() {
        DecoratedMediaManager mediaManager = getDecoratedMediaManager();
        if (mediaManager.isPlaying()) {
            mediaManager.stop();
        }
    }

    private DecoratedMediaManager getDecoratedMediaManager(){
        return getMainApplication().getDecoratedMediaManager();
    }

    private void saveTranslation() {
        getContextFromIntent().save();
    }
}
