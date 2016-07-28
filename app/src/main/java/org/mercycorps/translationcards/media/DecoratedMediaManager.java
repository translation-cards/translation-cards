package org.mercycorps.translationcards.media;

import android.widget.ProgressBar;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.exception.AudioFileException;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import static org.mercycorps.translationcards.MainApplication.getContextFromMainApp;

/**
 * Created by karthikbalasubramanian on 3/28/16.
 */
public class DecoratedMediaManager {
    private final int INITIAL_DELAY = 0;
    private final int PERIOD = 100;
    private ProgressBar progressBar;
    private ScheduledFuture scheduledFuture;
    private static final int RESET_PROGRESS_BAR = 0;
    private String filename;
    private AudioPlayerManager audioPlayerManager;

    @Inject
    public DecoratedMediaManager(AudioPlayerManager audioPlayerManager) {
        this.audioPlayerManager = audioPlayerManager;
    }

    //// TODO: 3/28/16 There is an implicit order here. Play has to be called before you can call maxDuration or getCurrentPosition.
    public void play(String filename, ProgressBar progressBar, boolean isAsset) throws AudioFileException {
        this.progressBar = progressBar;
        this.filename = filename;
        audioPlayerManager.play(this.filename, isAsset);
        progressBar.setMax(audioPlayerManager.getMaxDuration());
        schedule();
    }


    private void schedule() {
        scheduledFuture = getExecutorService().scheduleAtFixedRate(createRunnable(), INITIAL_DELAY, PERIOD, TimeUnit.MILLISECONDS);
    }

    //// TODO: 3/28/16 Figure out how to unit test this!
    private Runnable createRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                if(!isPlaying()) stop();
                else progressBar.setProgress(audioPlayerManager.getCurrentPosition());
            }
        };
    }


    public void stop() {
        if (playHasNotBeenCalled()) return;
        audioPlayerManager.stop();
        progressBar.setProgress(RESET_PROGRESS_BAR);
        scheduledFuture.cancel(true);
    }

    private boolean playHasNotBeenCalled() {
        return progressBar == null || scheduledFuture == null;
    }

    private ScheduledExecutorService getExecutorService(){
        return ((MainApplication) getContextFromMainApp()).getScheduledExecutorService();
    }
    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public boolean isPlaying() {
        return audioPlayerManager.isPlaying();
    }

    public boolean isCurrentlyPlayingSameCard(String fileName) {
        return isSameAudioFile(fileName) && isPlaying();
    }

    private boolean isSameAudioFile(String fileName) {
        return fileName != null && fileName.equals(this.filename);
    }
}
