package org.mercycorps.translationcards.media;

import android.media.MediaPlayer;
import android.util.Log;
import android.widget.ProgressBar;

import org.mercycorps.translationcards.data.Dictionary;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MediaPlayerManager implements Runnable {

    private static final String TAG = "MediaPlayerManager";
    private final Lock lock;
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private Dictionary.Translation translation;

    public MediaPlayerManager(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
        lock = new ReentrantLock();
    }

    public void stop() {
        lock.lock();
        mediaPlayer.stop();
        mediaPlayer.reset();
        resetProgressBar();
        lock.unlock();
    }

    private void resetProgressBar() {
        if (progressBar != null) {
           progressBar.setProgress(0);
        }
    }

    private boolean tryUpdate() {
        lock.lock();
        if (!mediaPlayer.isPlaying()) {
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

    public void play(FileDescriptor audioFile, ProgressBar progressBar, Dictionary.Translation translation) {
        lock.lock();
        resetProgressBar();
        this.translation = translation;
        this.progressBar = progressBar;
        try {
            mediaPlayer.setDataSource(audioFile);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            e.printStackTrace();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });

        this.progressBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        new Thread(this).start();
        lock.unlock();
    }

    public boolean isCurrentlyPlayingSameCard(Dictionary.Translation translation) {
        return isSameAudioFile(translation) && mediaPlayer.isPlaying();
    }

    private boolean isSameAudioFile(Dictionary.Translation translation) {
        return this.translation != null && translation.getFilename().equals(this.translation.getFilename());
    }
}