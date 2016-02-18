package org.mercycorps.translationcards;

import android.media.MediaPlayer;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MediaPlayerManager implements Runnable {

    private final Lock lock;
    private boolean running;
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;

    public MediaPlayerManager() {
        lock = new ReentrantLock();
        running = true;
        mediaPlayer = new MediaPlayer();
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        running = true;

    }

    public void setProgressBar(ProgressBar progressBar) {
        resetProgressBar();
        this.progressBar = progressBar;
    }

    public boolean stop() {
        lock.lock();
        if (!running) {
            // Already stopped, just return false.
            lock.unlock();
            return false;
        }
        running = false;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.release();
        resetProgressBar();
        lock.unlock();
        return true;
    }

    private void resetProgressBar() {
        if (progressBar != null) {
           progressBar.setProgress(0);
        }
    }

    private boolean tryUpdate() {
        lock.lock();
        if (!running) {
            lock.unlock();
            return false;
        }
        final int currentPosition = mediaPlayer.getCurrentPosition();
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(currentPosition);
            }
        });
        lock.unlock();
        return true;
    }

    @Override
    public void run() {
        while (tryUpdate()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // Do nothing.
            }
        }
    }
}