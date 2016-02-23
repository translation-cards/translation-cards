package org.mercycorps.translationcards;

import android.app.Application;
import android.media.AudioManager;
import android.media.MediaPlayer;

import org.mercycorps.translationcards.media.MediaPlayerManager;

/**
 *
 *
 * @author patdale216@gmail.com (Pat Dale)
 * @author natashaj7@hotmail.com (Natasha Jimenez)
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
