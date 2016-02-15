package org.mercycorps.translationcards;

import android.media.MediaPlayer;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MediaPlayerManager implements Runnable {

    private final Lock lock;
    private boolean running;
    private final MediaPlayer mediaPlayer;
    private final ProgressBar progressBar;

    public MediaPlayerManager(MediaPlayer mediaPlayer, ProgressBar progressBar) {
        lock = new ReentrantLock();
        running = true;
        this.mediaPlayer = mediaPlayer;
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
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(0);
            }
        });
        lock.unlock();
        return true;
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

    public void start(String filename) {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(filename);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        progressBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        new Thread(this).start();
    }
}