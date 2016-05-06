package org.mercycorps.translationcards.media;

import android.media.MediaPlayer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class AudioPlayerManagerTest {

    private static final boolean IS_NOT_ASSET = false;
    private MediaPlayer mediaPlayer;
    private AudioPlayerManager audioPlayerManager;
    public static final String ANY_FILE = "ANY FILE";

    @Before
    public void setUp() throws Exception {
        mediaPlayer = mock(MediaPlayer.class);
        audioPlayerManager = new AudioPlayerManager(mediaPlayer);
    }

    @Test
    public void shouldPrepareMediaPlayer() throws AudioFileException, IOException {
        audioPlayerManager.play(ANY_FILE, IS_NOT_ASSET);
        verify(mediaPlayer).prepare();
    }

    @Test
    public void shouldStartMediaPlayer() throws AudioFileException {
        audioPlayerManager.play(ANY_FILE, IS_NOT_ASSET);
        verify(mediaPlayer).start();
    }

    @Test
    public void shouldStopMediaPlayer() {
        when(mediaPlayer.isPlaying()).thenReturn(true);

        audioPlayerManager.stop();

        verify(mediaPlayer).stop();
    }

    @Test
    public void shouldResetMediaPlayer() {
        when(mediaPlayer.isPlaying()).thenReturn(true);

        audioPlayerManager.stop();

        verify(mediaPlayer).reset();
    }

    @Test (expected = AudioFileException.class)
    public void shouldThrowExceptionWhenNoAudioFileIsFound() throws AudioFileException, IOException {
        audioPlayerManager.play("", false);
    }

    @Test
    public void shouldThrowExceptionWithMessageWhenNoAudioFileIsFound() {
        try {
            audioPlayerManager.play("", false);
        } catch(AudioFileException e) {
            assertEquals("Audio file not found.", e.getMessage());
        }
    }
}