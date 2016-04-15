package org.mercycorps.translationcards.media;

import android.media.MediaPlayer;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
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

    private static final boolean IS_NOT_ASSET = false;
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
        audioPlayerManager.play(ANY_FILE, IS_NOT_ASSET);
        verify(mediaPlayer).setDataSource(new FileDescriptor()); //TODO Unable to test because w need an actual FileDescriptor
    }

    @Test
    public void shouldPrepareMediaPlayer() throws IOException {
        audioPlayerManager.play(ANY_FILE, IS_NOT_ASSET);
        verify(mediaPlayer).prepare();
    }

    @Test
    public void shouldStartMediaPlayer() throws IOException {
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
}