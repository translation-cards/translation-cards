package org.mercycorps.translationcards.media;

import android.media.MediaPlayer;
import android.util.Log;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.refactor.activity.AudioFileNotSetException;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import java.io.FileDescriptor;
import java.io.IOException;

public class AudioPlayerManager {
    private static final String TAG = "MediaPlayerManager";
    private MediaPlayer mediaPlayer;
    private FileDescriptor fileDescriptor;

    public AudioPlayerManager(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

    }

    public void stop() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    public void play(String fileName) throws IOException {
        prepareMediaPlayer(fileName);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mediaPlayer.start();
    }


    private void prepareMediaPlayer(String fileName) {
        try {
            fileDescriptor = ((MainApplication) MainApplication.getContextFromMainApp()).getFileDescriptor(fileName);
            mediaPlayer.setDataSource(fileDescriptor);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            e.printStackTrace();
        }
    }

    public int getCurrentPosition() {
        if(fileDescriptor == null) return 0 ;
        return mediaPlayer.getCurrentPosition();
    }

    public int getMaxDuration()  {
        if(fileDescriptor == null) return 0;
        return mediaPlayer.getDuration();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}
