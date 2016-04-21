package org.mercycorps.translationcards.media;

import android.widget.ProgressBar;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.exception.AudioFileException;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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

    public DecoratedMediaManager() {
    }

    //// TODO: 3/28/16 There is an implicit order here. Play has to be called before you can call maxDuration or getCurrentPosition.
    public void play(String filename, ProgressBar progressBar, boolean isAsset) throws AudioFileException {
        this.progressBar = progressBar;
        this.filename = filename;
        try {
            getAudioPlayerManager().play(this.filename, isAsset);
            progressBar.setMax(getAudioPlayerManager().getMaxDuration());
            schedule();
        } catch (IOException e) {
            throw new AudioFileException(e);
        }
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
                else progressBar.setProgress(getAudioPlayerManager().getCurrentPosition());
            }
        };
    }


    public void stop() {
        if (progressBar == null) return;
        getAudioPlayerManager().stop();
        progressBar.setProgress(RESET_PROGRESS_BAR);
        scheduledFuture.cancel(true);
    }

    private AudioPlayerManager getAudioPlayerManager() {
        return ((MainApplication) getContextFromMainApp()).getAudioPlayerManager();
    }

    private ScheduledExecutorService getExecutorService(){
        return ((MainApplication) getContextFromMainApp()).getScheduledExecutorService();
    }
    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public boolean isPlaying() {
        return getAudioPlayerManager().isPlaying();
    }

    public boolean isCurrentlyPlayingSameCard(String fileName) {
        return isSameAudioFile(fileName) && isPlaying();
    }

    private boolean isSameAudioFile(String fileName) {
        return fileName != null && fileName.equals(this.filename);
    }
}
