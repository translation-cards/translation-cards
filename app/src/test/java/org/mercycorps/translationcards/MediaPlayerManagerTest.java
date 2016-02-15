package org.mercycorps.translationcards;

import android.media.MediaPlayer;
import android.widget.ProgressBar;

import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.core.IsAnything.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Test for MediaPlayerManager
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
public class MediaPlayerManagerTest {
    @Test
    public void start_shouldResetAndPrepareMediaPlayer() throws IOException {
        MediaPlayer mediaPlayer = mock(MediaPlayer.class);
        ProgressBar progressBar = mock(ProgressBar.class);
        MediaPlayerManager mediaPlayerManager = new MediaPlayerManager(mediaPlayer, progressBar);

        String filename = "filename";
        mediaPlayerManager.start(filename);

        verify(mediaPlayer).reset();
        verify(mediaPlayer).setDataSource(filename);
        verify(mediaPlayer).prepare();
        verify(progressBar).setMax(mediaPlayer.getDuration());
        verify(mediaPlayer).start();
    }
}