package org.mercycorps.translationcards.media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.refactor.activity.RecordAudioException;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AudioRecorderManager {
    private MediaRecorder mediaRecorder;
    private boolean isRecording;

    public AudioRecorderManager() {
    }

    //// TODO: 3/28/16  Currently because of 3click bug, we are having to do this. Refactor
    public boolean record(MediaConfig mediaConfig) throws RecordAudioException {
        mediaRecorder = ((MainApplication) MainApplication.getContextFromMainApp()).getMediaRecorder();
        setupMediaRecorder(mediaConfig);
        prepareMediaRecorder();
        try {
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        isRecording = true;
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
        mediaRecorder.setOutputFile(mediaConfig.getAbsoluteFilePath());
        mediaRecorder.setAudioEncoder(mediaConfig.getAudioEncoder());

    }

    public void stop() {
        if(mediaRecorder == null) return;
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }
}
