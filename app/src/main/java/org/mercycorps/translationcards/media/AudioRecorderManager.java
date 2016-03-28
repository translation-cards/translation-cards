package org.mercycorps.translationcards.media;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import org.mercycorps.translationcards.refactor.activity.RecordAudioException;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class AudioRecorderManager {

    private MediaRecorder mediaRecorder;

    public AudioRecorderManager(MediaRecorder mediaRecorder) {
//        findAudioRecord();
    }

    public boolean record(MediaConfig mediaConfig) throws RecordAudioException {
        mediaRecorder = new MediaRecorder();
        setupMediaRecorder(mediaConfig);
        prepareMediaRecorder();
        try {
            mediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
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
        mediaRecorder.setOutputFile(mediaConfig.getFileNameWithPath());

        mediaRecorder.setAudioEncoder(mediaConfig.getAudioEncoder());

    }

    public void stop() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
    }
}
