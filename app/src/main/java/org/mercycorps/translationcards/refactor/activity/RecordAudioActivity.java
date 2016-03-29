package org.mercycorps.translationcards.refactor.activity;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
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
    private boolean isRecording = false;

    @Bind(R.id.play_audio_button)ImageButton playAudioButton;
    @Bind(R.id.record_audio_title)TextView recordAudioTitle;
    @Bind(R.id.origin_translation_text)TextView originTranslationText;
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
    }

    private void showTranslationSourcePhrase() {
        originTranslationText.setText(getContextFromIntent().getTranslation().getLabel());
    }

    private void updateBackAndNextButtonStates(){
        ButterKnife.apply(backAndNext, VISIBILTY, getVisibility());
    }

    private int getVisibility(){
        boolean translationHasAudioFile = getContextFromIntent().getTranslation().isAudioFilePresent();
        return translationHasAudioFile && !isRecording ? View.VISIBLE : View.GONE;
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
        startNextActivity(RecordAudioActivity.this, EnterTranslatedPhraseActivity.class);
    }

    @OnClick(R.id.go_to_enter_source_phrase_activity)
    public void recordActivityBackClick(){
        startNextActivity(RecordAudioActivity.this, EnterSourcePhraseActivity.class);
    }

    @OnClick(R.id.record_audio_button)
    public void recordAudioButtonClick(){
        isRecording = !isRecording;
        tryToRecord();
        updateBackAndNextButtonStates();
        updatePlayButtonState();
    }


    private void tryToRecord() {
        try {
            handleIsRecordingState();
        } catch (RecordAudioException e) {
            Log.d(TAG, "Error creating file for recording: " + e);
            showToast(getString(R.string.unable_to_record_message));
        }
    }

    @OnClick(R.id.play_audio_button)
    public void playAudioButtonClick(){
        try {
            if(isRecording) {
                isRecording = !isRecording;
                handleIsRecordingState();
            }
            playAudioFile();
        } catch (AudioFileException | RecordAudioException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            showToast(e.getLocalizedMessage());
        }
    }

    private void playAudioFile() throws AudioFileException {
        try {

            NewTranslationContext context = getContextFromIntent();
            AudioPlayerManager audioPlayerManager = ((MainApplication) getApplication()).getAudioPlayerManager();
            audioPlayerManager.play(context.getTranslation().getFilename());
        } catch (IOException e) {
            throw new AudioFileException("Unable to play audio file", e);
        }
    }

    private void handleIsRecordingState() throws RecordAudioException {
        AudioRecorderManager audioRecorderManager = ((MainApplication) getApplication()).getAudioRecorderManager();
        if (!isRecording) {
            audioRecorderManager.stop();
        } else {
            //TODO looks chaotic. Refactor
            MediaConfig mediaConfig = MediaConfig.createMediaConfig();
            getContextFromIntent().setAudioFile(mediaConfig.getAbsoluteFilePath());
            audioRecorderManager.record(mediaConfig);
        }
    }

}
