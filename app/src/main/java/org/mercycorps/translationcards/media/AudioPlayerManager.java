package org.mercycorps.translationcards.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.util.Log;

import org.mercycorps.translationcards.MainApplication;

import java.io.FileDescriptor;
import java.io.IOException;

public class AudioPlayerManager {
    private static final String TAG = "AudioPlayerManager";
    private MediaPlayer mediaPlayer;
    private FileDescriptor fileDescriptor;

    public AudioPlayerManager(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;

    }

    public void stop() {
        if(isPlaying()){
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
    }

    public void play(String fileName, boolean isAsset) throws IOException {
        prepareMediaPlayer(fileName, isAsset);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stop();
            }
        });
        mediaPlayer.start();
    }


    private void prepareMediaPlayer(String fileName, boolean isAsset) {
        try {
            mediaPlayer.reset();
            setMediaPlayerDataSource(fileName, isAsset);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            e.printStackTrace();
        }
    }

    private void setMediaPlayerDataSource(String fileName, boolean isAsset) throws IOException {
        Context context = MainApplication.getContextFromMainApp();
        if (isAsset) {
            AssetFileDescriptor assetFileDescriptor = context.getAssets().openFd(fileName);
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor());
            assetFileDescriptor.close();
        } else {
            fileDescriptor = ((MainApplication) context).getFileDescriptor(fileName);
            mediaPlayer.setDataSource(fileDescriptor);
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
