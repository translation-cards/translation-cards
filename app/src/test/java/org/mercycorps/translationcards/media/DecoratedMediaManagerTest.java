package org.mercycorps.translationcards.media;

import android.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.DEFAULT_MAX;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.DEFAULT_POSITION;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.INITIAL_DELAY;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.PERIOD;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DecoratedMediaManagerTest {
    public static final String FILENAME = "Filename";
    private static final boolean IS_NOT_ASSET = false;
    private DecoratedMediaManager decoratedMediaManager;
    private static final int RESET_PROGRESS_BAR = 0;

    private ProgressBar progressBar;
    private AudioPlayerManager audioPlayerManager;
    private ScheduledExecutorService scheduledExecutorService;

    @Before
    public void setUp() throws Exception {
        audioPlayerManager = mock(AudioPlayerManager.class);
        scheduledExecutorService = mock(ScheduledExecutorService.class);
        progressBar = mock(ProgressBar.class);
        setupAudioPlayerManager();

        decoratedMediaManager = new DecoratedMediaManager(audioPlayerManager, scheduledExecutorService);
    }

    @Test
    public void shouldPlayAudioWhenPlayIsCalled() throws Exception {
        decoratedMediaManager.play(FILENAME, progressBar, IS_NOT_ASSET);
        verify(audioPlayerManager).play(FILENAME, IS_NOT_ASSET);
    }

    @Test
    public void shouldStopAudioWhenStopIsCalled() throws AudioFileException, AudioFileNotSetException {
        decoratedMediaManager.play(FILENAME, progressBar, IS_NOT_ASSET);
        decoratedMediaManager.stop();
        verify(audioPlayerManager).stop();
    }

    @Test
    public void shouldSetMaxDurationOfProgressBarWhenPlayIsCalled() throws Exception {
        decoratedMediaManager.play(FILENAME, progressBar, IS_NOT_ASSET);
        verify(progressBar).setMax(DEFAULT_MAX);
    }

    @Test
    public void shouldCreatedScheduledFutureWhenPlayIsCalled() throws Exception {
        decoratedMediaManager.play(FILENAME, progressBar, IS_NOT_ASSET);
        verify(scheduledExecutorService).scheduleAtFixedRate(any(Runnable.class), eq(INITIAL_DELAY), eq(PERIOD), eq(TimeUnit.MILLISECONDS));
    }

    @Test
    public void shouldResetProgressBarWhenStopIsCalled() throws Exception{
        decoratedMediaManager.play(FILENAME, progressBar, IS_NOT_ASSET);
        decoratedMediaManager.stop();
        verify(progressBar).setProgress(eq(RESET_PROGRESS_BAR));
    }

    @Test
    public void shouldStopAndDestroyScheduledFutureWhenStopIsCalled() throws Exception{
        decoratedMediaManager.play(FILENAME, progressBar, IS_NOT_ASSET);
        decoratedMediaManager.stop();
        verify(decoratedMediaManager.getScheduledFuture()).cancel(true);
    }

    private void setupAudioPlayerManager() {
        when(audioPlayerManager.getCurrentPosition()).thenReturn(DEFAULT_POSITION);
        when(audioPlayerManager.getMaxDuration()).thenReturn(DEFAULT_MAX);
        when(scheduledExecutorService.scheduleAtFixedRate(any(Runnable.class), eq(INITIAL_DELAY), eq(PERIOD), eq(TimeUnit.MILLISECONDS)))
                .thenReturn(mock(ScheduledFuture.class));
    }
}
