package org.mercycorps.translationcards.manager;

import android.media.MediaPlayer;
import android.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.MediaPlayerManager;

import java.io.FileDescriptor;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

public class MediaPlayerManagerTest {

    public static final int SOME_DURATION = 5;
    public static final String ANY_FILENAME = "";
    private MediaPlayer mediaPlayer;
    private MediaPlayerManager mediaPlayerManager;
    private Translation translation;
    public static final FileDescriptor ANY_FILE = new FileDescriptor();

    @Before
    public void setUp() throws Exception {
        mediaPlayer = mock(MediaPlayer.class);
        when(mediaPlayer.getDuration()).thenReturn(SOME_DURATION);
        translation = mock(Translation.class);

        mediaPlayerManager = new MediaPlayerManager(mediaPlayer);
    }

    @Test
    public void play_shouldSetMediaPlayerDataSourceToSomeFilename() throws IOException {
        FileDescriptor fileDescriptor = new FileDescriptor();
        mediaPlayerManager.play(fileDescriptor, mock(ProgressBar.class), translation);

        verify(mediaPlayer).setDataSource(fileDescriptor);
    }

    @Test
    public void play_shouldPrepareMediaPlayer() throws IOException {
        mediaPlayerManager.play(ANY_FILE, mock(ProgressBar.class), translation);

        verify(mediaPlayer).prepare();
    }

    @Test
    public void play_shouldSetProgressBarToDurationOfMediaPlayer() {
        ProgressBar progressBar = mock(ProgressBar.class);
        mediaPlayerManager.play(ANY_FILE, progressBar, translation);

        verify(progressBar).setMax(SOME_DURATION);
    }

    @Test
    public void play_shouldStartMediaPlayer() {
        mediaPlayerManager.play(ANY_FILE, mock(ProgressBar.class), translation);

        verify(mediaPlayer).start();
    }

    @Test
    public void stop_shouldStopMediaPlayer() {
        when(mediaPlayer.isPlaying()).thenReturn(true);

        mediaPlayerManager.stop();

        verify(mediaPlayer).stop();
    }

    @Test
    public void stop_shouldResetMediaPlayer() {
        when(mediaPlayer.isPlaying()).thenReturn(true);

        mediaPlayerManager.stop();

        verify(mediaPlayer).reset();
    }

    @Test
    public void stop_shouldResetProgressBarIfItHasBeenSet() {
        ProgressBar progressBar = mock(ProgressBar.class);
        when(mediaPlayer.isPlaying()).thenReturn(true);

        mediaPlayerManager.play(ANY_FILE, progressBar, translation);
        mediaPlayerManager.stop();

        verify(progressBar).setProgress(0);
    }

    @Test
    public void play_shouldResetProgressBarIfItHasBeenSet() {
        ProgressBar progressBar = mock(ProgressBar.class);

        mediaPlayerManager.play(ANY_FILE, progressBar, translation);
        mediaPlayerManager.play(ANY_FILE, mock(ProgressBar.class), translation);

        verify(progressBar).setProgress(0);
    }
}