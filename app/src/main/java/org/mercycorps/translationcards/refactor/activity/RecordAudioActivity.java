package org.mercycorps.translationcards.refactor.activity;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.MediaConfig;
import org.mercycorps.translationcards.media.AudioRecorderManager;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecordAudioActivity extends AddTranslationActivity {
    private static final String TAG = "RecordAudioActivity";

    @Bind(R.id.play_audio_button)ImageButton playAudioButton;
    @Bind(R.id.record_audio_button)ImageButton recordAudioButton;
    @Bind(R.id.record_audio_title)TextView recordAudioTitle;
    @Bind(R.id.origin_translation_text)TextView originTranslationText;
    @Bind(R.id.go_to_enter_source_phrase_activity)LinearLayout backButton;
    @Bind(R.id.text_indicator_divider)FrameLayout translationTextIndicatorDivider;
    @Bind({ R.id.go_to_enter_source_phrase_activity, R.id.record_activity_next})
    List<LinearLayout> backAndNext;


    @Override
    protected void setActivityTitle() {
        recordAudioTitle.setText(String.format(getString(R.string.record_audio_title), getContextFromIntent().getDictionary().getLabel()));
    }

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_record_audio);
    }

    public void initStates(){
        updatePlayButtonState();
        updateBackAndNextButtonStates();
        showTranslationSourcePhrase();
        showBackButton();
        hideIndicatorDivider();
    }

    private void hideIndicatorDivider() {
        translationTextIndicatorDivider.setVisibility(View.GONE);
    }

    protected void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
    }

    private void showTranslationSourcePhrase() {
        originTranslationText.setText(getContextFromIntent().getTranslation().getLabel());
    }

    private void updateBackAndNextButtonStates(){
        ButterKnife.apply(backAndNext, VISIBILTY, getVisibility());
    }

    private int getVisibility(){
        boolean translationHasAudioFile = getContextFromIntent().getTranslation().isAudioFilePresent();
        return translationHasAudioFile && !getAudioRecorderManager().isRecording() ? View.VISIBLE : View.GONE;
    }

    private void updatePlayButtonState() {
        boolean translationHasAudioFile = getContextFromIntent().getTranslation().isAudioFilePresent();
        if (translationHasAudioFile) {
            playAudioButton.setBackgroundResource(R.drawable.play_button_enabled);
        }
        playAudioButton.setClickable(translationHasAudioFile);
    }


    @OnClick(R.id.record_activity_next)
    public void recordActivityNextClick(){
        stopAudioIfPlaying();
        stopIfRecording();
        startNextActivity(RecordAudioActivity.this, EnterTranslatedPhraseActivity.class);
    }

    private void stopIfRecording() {
        if(getAudioRecorderManager().isRecording()){
            getAudioRecorderManager().stop();
            updateBackAndNextButtonStates();
            updateRecordButtonState();
        }
    }

    @OnClick(R.id.go_to_enter_source_phrase_activity)
    public void recordActivityBackClick(){
        stopAudioIfPlaying();
        stopIfRecording();
        startNextActivity(RecordAudioActivity.this, EnterSourcePhraseActivity.class);
    }

    @OnClick(R.id.record_audio_button)
    public void recordAudioButtonClick(){
        stopAudioIfPlaying();
        tryToRecord();
        updateRecordButtonState();
        updateBackAndNextButtonStates();
        updatePlayButtonState();
    }

    private void updateRecordButtonState() {
        if (getAudioRecorderManager().isRecording()) {
            recordAudioButton.setBackgroundResource(R.drawable.button_record_active);
        } else {
            recordAudioButton.setBackgroundResource(R.drawable.button_record_enabled);
        }
    }


    @OnClick(R.id.play_audio_button)
    public void playAudioButtonClick(){
        try {
            stopIfRecording();
            playAudioFile();
        } catch (AudioFileException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            showToast(e.getLocalizedMessage());
        }
    }


    protected void stopAudioIfPlaying() {
        if (getAudioPlayerManager().isPlaying()) {
            getAudioPlayerManager().stop();
        }
    }


    private void tryToRecord() {
        try {
            handleIsRecordingState();
        } catch (RecordAudioException e) {
            Log.d(TAG, "Error creating file for recording: " + e);
            showToast(getString(R.string.unable_to_record_message));
        }
    }

    private void playAudioFile() throws AudioFileException {
        try {
            NewTranslationContext context = getContextFromIntent();
            getAudioPlayerManager().play(context.getTranslation().getFilename());
        } catch (IOException e) {
            throw new AudioFileException("Unable to play audio file", e);
        }
    }

    private void handleIsRecordingState() throws RecordAudioException {
        AudioRecorderManager audioRecorderManager = getAudioRecorderManager();
        if (audioRecorderManager.isRecording()) {
            audioRecorderManager.stop();
        } else {
            //TODO looks chaotic. Refactor
            MediaConfig mediaConfig = MediaConfig.createMediaConfig();
            getContextFromIntent().setAudioFile(mediaConfig.getAbsoluteFilePath());
            audioRecorderManager.record(mediaConfig);
        }
    }

}
