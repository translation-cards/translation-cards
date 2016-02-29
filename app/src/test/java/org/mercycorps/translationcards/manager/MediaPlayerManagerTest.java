package org.mercycorps.translationcards.manager;

import android.media.MediaPlayer;
import android.widget.ProgressBar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.media.MediaPlayerManager;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MediaPlayerManagerTest {

    public static final int SOME_DURATION = 5;
    public static final String ANY_FILENAME = "";
    private MediaPlayer mediaPlayer;
    private MediaPlayerManager mediaPlayerManager;
    private Dictionary.Translation translation;

    @Before
    public void setUp() throws Exception {
        mediaPlayer = mock(MediaPlayer.class);
        when(mediaPlayer.getDuration()).thenReturn(SOME_DURATION);
        translation = mock(Dictionary.Translation.class);
        when(translation.getFilename()).thenReturn(ANY_FILENAME);

        mediaPlayerManager = new MediaPlayerManager(mediaPlayer);
    }

    @Test
    public void play_shouldSetMediaPlayerDataSourceToSomeFilename() throws IOException {
        Dictionary.Translation translation = mock(Dictionary.Translation.class);
        when(translation.getFilename()).thenReturn("SomeFilename");
        mediaPlayerManager.play(translation, mock(ProgressBar.class));

        verify(mediaPlayer).setDataSource("SomeFilename");
    }

    @Test
    public void play_shouldPrepareMediaPlayer() throws IOException {
        mediaPlayerManager.play(translation, mock(ProgressBar.class));

        verify(mediaPlayer).prepare();
    }

    @Test
    public void play_shouldSetProgressBarToDurationOfMediaPlayer() {
        ProgressBar progressBar = mock(ProgressBar.class);
        mediaPlayerManager.play(translation, progressBar);

        verify(progressBar).setMax(SOME_DURATION);
    }

    @Test
    public void play_shouldStartMediaPlayer() {
        mediaPlayerManager.play(translation, mock(ProgressBar.class));

        verify(mediaPlayer).start();
    }

    @Ignore
    @Test
    public void play_shouldSetOnCompletionListener() {

    }

    @Ignore
    @Test
    public void play_shouldStartMediaPlayerInNewThread() {

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

        mediaPlayerManager.play(translation, progressBar);
        mediaPlayerManager.stop();

        verify(progressBar).setProgress(0);
    }

    @Test
    public void play_shouldResetProgressBarIfItHasBeenSet() {
        ProgressBar progressBar = mock(ProgressBar.class);

        mediaPlayerManager.play(translation, progressBar);
        mediaPlayerManager.play(translation, mock(ProgressBar.class));

        verify(progressBar).setProgress(0);
    }
}