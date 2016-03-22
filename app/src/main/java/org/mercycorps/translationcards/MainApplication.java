package org.mercycorps.translationcards;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.fileUtil.FileHelper;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.MediaPlayerManager;
import org.mercycorps.translationcards.media.MediaRecorderManager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.FileHandler;

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
    private AudioPlayerManager audioPlayerManager;
    private FileHelper fileHelper;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayerManager = new MediaPlayerManager(mediaPlayer);
        dbManager = new DbManager(getApplicationContext());
        mediaRecorderManager = new MediaRecorderManager(new MediaRecorder());
        audioPlayerManager = new AudioPlayerManager(mediaPlayer);
        fileHelper = new FileHelper();
        context = getApplicationContext();
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

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public FileHelper getFileHelper(){
        return fileHelper;
    }

    public static Context getContextFromMainApp(){
        return context;
    }

    public FileDescriptor getFileDescriptor(String fileName) throws IOException {
        return new FileInputStream(new File(fileName)).getFD();
    }
}
