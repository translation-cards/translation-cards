package org.mercycorps.translationcards.refactor.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.NewTranslationContext;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.MediaRecorderManager;

public class RecordAudioActivity extends AppCompatActivity {
    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    private boolean isRecording;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        isRecording = false;
        initState();
        setOnClickListeners();
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
        findViewById(R.id.record_audio_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRecording = !isRecording;
                handleIsRecordingState();
                updateButtonStates(R.id.record_activity_back);
                updateButtonStates(R.id.record_activity_next);
            }
        });
    }

    private void handleIsRecordingState() {
        MediaRecorderManager mediaRecorderManager = ((MainApplication) getApplication()).getMediaRecorderManager();
        if (!isRecording) {
            updateTranslationContext(mediaRecorderManager.stop());
        } else {
            mediaRecorderManager.record();
        }
    }

    private void updateTranslationContext(String audioFileName) {
        getContextFromIntent().setAudioFile(audioFileName);
    }


    private NewTranslationContext getContextFromIntent(){
       return (NewTranslationContext) getIntent().getSerializableExtra(CONTEXT_INTENT_KEY);

    }
}
