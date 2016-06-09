package org.mercycorps.translationcards.media;

import android.media.MediaRecorder;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.exception.RecordAudioException;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class AudioRecorderManagerTest {
    private MediaConfig mediaConfig;
    private AudioRecorderManager audioRecorderManager;
    private MediaRecorder mediaRecorder;

    @Before
    public void setUp() throws Exception {
        mediaRecorder = getMediaRecorder();
        audioRecorderManager = new AudioRecorderManager();
        mediaConfig = new MediaConfig("A FILE");
    }

    @After
    public void teardown() {
        audioRecorderManager.stop();
    }

    @Test
    public void shouldSetAudioSourceWhenRecording() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setAudioSource(mediaConfig.getAudioSource());
    }

    @Test
    public void shouldSetOutputFormatWhenRecording() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setOutputFormat(mediaConfig.getOutputFormat());
    }

    @Test
    public void shouldSetOutputFileWhenRecording() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setOutputFile(anyString());
    }

    @Test
    public void shouldSetAudioEncoderWhenRecording() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        verify(mediaRecorder).setAudioEncoder(mediaConfig.getAudioEncoder());
    }

    @Test
    public void shouldPrepareMediaRecorderWhenRecording() throws RecordAudioException, IOException {
        audioRecorderManager.record(mediaConfig);
        verify(mediaRecorder).prepare();
    }

    @Test
    public void shouldStartRecordingAudioWhenRecording() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        verify(mediaRecorder).start();
    }

    @Test(expected = RecordAudioException.class)
    public void shouldThrowRecordAudioExceptionWhenOutputFileIsNull() throws RecordAudioException, IOException {
        Mockito.doThrow(new IOException()).when(mediaRecorder).prepare();
        audioRecorderManager.record(mediaConfig);
    }

    @Test
    public void shouldCallStopMediaRecorderWhenStopRecording() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        audioRecorderManager.stop();
        verify(mediaRecorder).stop();
    }

    @Test
    public void shouldCallResetMediaRecorderWhenStopRecording() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        audioRecorderManager.stop();
        verify(mediaRecorder).reset();
    }

    public static MediaRecorder getMediaRecorder() {
        return getApplication().getMediaRecorder();
    }

    private static TestMainApplication getApplication() {
        return (TestMainApplication) RuntimeEnvironment.application;
    }

}