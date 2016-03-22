package org.mercycorps.translationcards.media;

import android.media.MediaRecorder;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.refactor.activity.RecordAudioActivity;
import org.mercycorps.translationcards.refactor.activity.RecordAudioException;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MediaRecorderManagerTest {

    private MediaRecorder mediaRecorder;
    private MediaConfig mediaConfig;
    private MediaRecorderManager mediaRecorderManager;

    @Before
    public void setUp() throws Exception {
        mediaRecorder = mock(MediaRecorder.class);
        mediaRecorderManager = new MediaRecorderManager(mediaRecorder);
        mediaConfig = new MediaConfig("A FILE");
    }

    @Test
    public void shouldSetAudioSourceWhenRecording() throws RecordAudioException {
        mediaRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setAudioSource(mediaConfig.getAudioSource());
    }

    @Test
    public void shouldSetOutputFormatWhenRecording() throws RecordAudioException {
        mediaRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setOutputFormat(mediaConfig.getOutputFormat());
    }

    @Test
    public void shouldSetOutputFileWhenRecording() throws RecordAudioException {
        mediaRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setOutputFile(mediaConfig.getFileName());
    }

    @Test
    public void shouldSetAudioEncoderWhenRecording() throws RecordAudioException {
        mediaRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setAudioEncoder(mediaConfig.getAudioEncoder());
    }

    @Test
    public void shouldPrepareMediaRecorderWhenRecording() throws RecordAudioException, IOException {
        mediaRecorderManager.record(mediaConfig);
        verify(mediaRecorder).prepare();
    }

    @Test
    public void shouldStartRecordingAudioWhenRecording() throws RecordAudioException {
        mediaRecorderManager.record(mediaConfig);
        verify(mediaRecorder).start();
    }

    @Test(expected = RecordAudioException.class)
    public void shouldThrowRecordAudioExceptionWhenOutputFileIsNull() throws RecordAudioException, IOException {
        Mockito.doThrow(new IOException()).when(mediaRecorder).prepare();
        mediaRecorderManager.record(mediaConfig);
    }

    @Test
    public void shouldCallStopMediaRecorderWhenStopRecording() {
        mediaRecorderManager.stop();
        verify(mediaRecorder).stop();
    }

    @Test
    public void shouldCallResetMediaRecorderWhenStopRecording() {
        mediaRecorderManager.stop();
        verify(mediaRecorder).reset();
    }
}