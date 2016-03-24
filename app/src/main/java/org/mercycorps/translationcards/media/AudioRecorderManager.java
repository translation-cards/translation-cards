package org.mercycorps.translationcards.media;

import android.media.MediaRecorder;

import org.mercycorps.translationcards.refactor.activity.RecordAudioException;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AudioRecorderManager {

    private MediaRecorder mediaRecorder;

    public AudioRecorderManager(MediaRecorder mediaRecorder) {
        this.mediaRecorder = mediaRecorder;
    }

    public boolean record(MediaConfig mediaConfig) throws RecordAudioException {
        setupMediaRecorder(mediaConfig);
        prepareMediaRecorder();
        mediaRecorder.start();
        return true;
    }

    private void prepareMediaRecorder() throws RecordAudioException {
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RecordAudioException("Unable to record audio " ,e);
        }
    }

    private void setupMediaRecorder(MediaConfig mediaConfig) throws RecordAudioException {
        mediaRecorder.setAudioSource(mediaConfig.getAudioSource());
        mediaRecorder.setOutputFormat(mediaConfig.getOutputFormat());
        mediaRecorder.setAudioEncoder(mediaConfig.getAudioEncoder());
        mediaRecorder.setOutputFile(mediaConfig.getFileNameWithPath());
    }

    public void stop() {
        mediaRecorder.stop();
        mediaRecorder.reset();
    }


}
