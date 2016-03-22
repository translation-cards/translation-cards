package org.mercycorps.translationcards.media;

import android.media.MediaRecorder;

import org.mercycorps.translationcards.refactor.activity.RecordAudioException;

import java.io.IOException;

public class MediaRecorderManager {

    private MediaRecorder mediaRecorder;

    public MediaRecorderManager(MediaRecorder mediaRecorder) {
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
            throw new RecordAudioException("Unable to record audio.");
        }
    }

    private void setupMediaRecorder(MediaConfig mediaConfig) throws RecordAudioException {
        mediaRecorder.setAudioSource(mediaConfig.getAudioSource());
        mediaRecorder.setOutputFormat(mediaConfig.getOutputFormat());
        mediaRecorder.setOutputFile(mediaConfig.getFileName());
        mediaRecorder.setAudioEncoder(mediaConfig.getAudioEncoder());
    }

    public void stop() {
        mediaRecorder.stop();
        mediaRecorder.reset();
    }


}
