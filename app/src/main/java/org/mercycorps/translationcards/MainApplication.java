package org.mercycorps.translationcards;

import android.app.Application;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.media.MediaPlayerManager;
import org.mercycorps.translationcards.media.MediaRecorderManager;

/**
 * Used to create singletons for dependency injection.
 *
 * @author patdale216@gmail.com (Pat Dale)
 * @author natashaj7@hotmail.com (Natasha Jimenez)
 */
public class MainApplication extends Application {

    private MediaPlayerManager mediaPlayerManager;
    private DbManager dbManager;
    private MediaRecorderManager mediaRecorderManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayerManager = new MediaPlayerManager(mediaPlayer);
        dbManager = new DbManager(getApplicationContext());
        mediaRecorderManager = new MediaRecorderManager();
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public MediaRecorderManager getMediaRecorderManager() {
        return mediaRecorderManager;
    }
}
