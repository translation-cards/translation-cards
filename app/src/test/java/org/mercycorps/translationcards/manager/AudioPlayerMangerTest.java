package org.mercycorps.translationcards.manager;

import android.media.MediaPlayer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.io.FileDescriptor;
import java.io.IOException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class AudioPlayerMangerTest {

    private MediaPlayer mediaPlayer;
    private AudioPlayerManager audioPlayerManager;
    public static final String ANY_FILE = "ANY FILE";

    @Before
    public void setUp() throws Exception {
        mediaPlayer = mock(MediaPlayer.class);
        audioPlayerManager = new AudioPlayerManager(mediaPlayer);
    }

    @Ignore
    @Test
    public void shouldSetMediaPlayerDataSourceToSomeFile() throws IOException {
        audioPlayerManager.play(ANY_FILE);
        verify(mediaPlayer).setDataSource(new FileDescriptor()); //TODO Unable to test because w need an actual FileDescriptor
    }

    @Test
    public void shouldPrepareMediaPlayer() throws IOException {
        audioPlayerManager.play(ANY_FILE);
        verify(mediaPlayer).prepare();
    }

    @Test
    public void shouldStartMediaPlayer() throws IOException {
        audioPlayerManager.play(ANY_FILE);
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
}