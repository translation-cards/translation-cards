package org.mercycorps.translationcards.refactor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.MediaConfig;
import org.mercycorps.translationcards.media.MediaRecorderManager;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class RecordAudioActivity extends AppCompatActivity {
    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    private static final String TAG = "RecordAudioActivity";
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        isRecording = false;
        setOnClickListeners();
        initState();
    }

    private void initState() {
        updatePlayButtonState();
        updateButtonStates(R.id.record_activity_back);
        updateButtonStates(R.id.record_activity_next);
    }

    private void updateButtonStates(int restId) {
        boolean translationHasAudioFile = getContextFromIntent().getTranslation().isAudioFilePresent();

        int visibility = translationHasAudioFile && !isRecording ? View.VISIBLE : View.GONE;
        findViewById(restId).setVisibility(visibility);
    }

    // TODO change color of play button depending on whether or not recording is happening
    private void updatePlayButtonState() {
        boolean translationHasAudioFile = getContextFromIntent().getTranslation().isAudioFilePresent();
        findViewById(R.id.play_audio_button).setClickable(translationHasAudioFile);
    }


    private void setOnClickListeners() {
        setupPlayButtonListener();
        setupRecordAudioButtonListener();
        startActivityForButton(R.id.record_activity_next, EnterTranslatedPhraseActivity.class);
        startActivityForButton(R.id.record_activity_back, EnterSourcePhraseActivity.class);
    }


    private void startActivityForButton(int viewId, final Class activityToStart){
        findViewById(viewId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordAudioActivity.this, activityToStart);
                intent.putExtra(CONTEXT_INTENT_KEY, getContextFromIntent());
                startActivity(intent);
            }
        });
    }

    private void setupRecordAudioButtonListener(){
        findViewById(R.id.record_audio_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;
                tryToRecord();
                updateButtonStates(R.id.record_activity_back);
                updateButtonStates(R.id.record_activity_next);

            }
        });
    }

    private void tryToRecord() {
        try {
            handleIsRecordingState();
        } catch (RecordAudioException e) {
            Log.d(TAG, "Error creating file for recording: " + e);
            ToastHelper.showToast(getApplicationContext(), getString(R.string.unable_to_record_message));
        }
    }

    private void setupPlayButtonListener(){
        findViewById(R.id.play_audio_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(isRecording) handleIsRecordingState();
                    playAudioFile();
                } catch (AudioFileException | RecordAudioException e) {
                    Log.d(TAG, "Error getting audio asset: " + e);
                    ToastHelper.showToast(getApplicationContext(), e.getLocalizedMessage());
                }
            }
        });
    }

    private void playAudioFile() throws AudioFileException {
        try {
            NewTranslationContext context = getContextFromIntent();
            AudioPlayerManager audioPlayerManager = ((MainApplication) getApplication()).getAudioPlayerManager();
            audioPlayerManager.play(context.getTranslation().getFilename());
        } catch (IOException e) {
            throw new AudioFileException("Unable to play audio file.");
        }
    }

    private void handleIsRecordingState() throws RecordAudioException {
        MediaRecorderManager mediaRecorderManager = ((MainApplication) getApplication()).getMediaRecorderManager();

        if (!isRecording) {
            mediaRecorderManager.stop();
        } else {
            //TODO looks chaotic. Refactor
            MediaConfig mediaConfig = MediaConfig.createMediaConfig();
            getContextFromIntent().setAudioFile(mediaConfig.getFileName());
            mediaRecorderManager.record(mediaConfig);
        }
    }

    private NewTranslationContext getContextFromIntent(){
       return (NewTranslationContext) getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);

    }
}
