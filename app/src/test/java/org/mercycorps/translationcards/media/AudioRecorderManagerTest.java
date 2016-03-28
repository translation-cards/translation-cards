package org.mercycorps.translationcards.media;

import android.media.MediaRecorder;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.refactor.activity.RecordAudioException;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class AudioRecorderManagerTest {

    private MediaRecorder mediaRecorder;
    private MediaConfig mediaConfig;
    private AudioRecorderManager audioRecorderManager;

    @Before
    public void setUp() throws Exception {
        mediaRecorder = mock(MediaRecorder.class);
        audioRecorderManager = new AudioRecorderManager(mediaRecorder);
        mediaConfig = new MediaConfig("A FILE");
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
    public void shouldCallStopMediaRecorderWhenStopRecording() {
        audioRecorderManager.stop();
        verify(mediaRecorder).stop();
    }

    @Test
    public void shouldCallResetMediaRecorderWhenStopRecording() {
        audioRecorderManager.stop();
        verify(mediaRecorder).reset();
    }

    @Ignore
    @Test
    public void threeClickBug() throws RecordAudioException {
        audioRecorderManager.record(mediaConfig);
        audioRecorderManager.stop();
        audioRecorderManager.record(mediaConfig);
    }
}