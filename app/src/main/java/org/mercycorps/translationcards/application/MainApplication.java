package org.mercycorps.translationcards.application;

import android.app.Application;
import android.media.AudioManager;
import android.media.MediaPlayer;

import org.mercycorps.translationcards.manager.MediaPlayerManager;

/**
 * Created by njimenez on 2/18/16.
 */
public class MainApplication extends Application {

    private MediaPlayerManager mediaPlayerManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayerManager = new MediaPlayerManager(mediaPlayer);
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }
}
