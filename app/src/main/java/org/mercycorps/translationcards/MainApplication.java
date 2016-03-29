package org.mercycorps.translationcards;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.fileUtil.FileHelper;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.media.MediaPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Used to create singletons for dependency injection.
 *
 * @author patdale216@gmail.com (Pat Dale)
 * @author natashaj7@hotmail.com (Natasha Jimenez)
 */
public class MainApplication extends Application {

    private MediaPlayerManager mediaPlayerManager;
    private DbManager dbManager;
    private AudioRecorderManager audioRecorderManager;
    private AudioPlayerManager audioPlayerManager;
    private FileHelper fileHelper;
    private static Context context;
    private  ScheduledExecutorService scheduledExecutorService;
    private DecoratedMediaManager decoratedMediaManager;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayerManager = new MediaPlayerManager(mediaPlayer);
        dbManager = new DbManager(getApplicationContext());
        audioRecorderManager = new AudioRecorderManager();
        scheduledExecutorService =  Executors.newScheduledThreadPool(1);
        audioPlayerManager = new AudioPlayerManager(mediaPlayer);
        decoratedMediaManager = new DecoratedMediaManager();
        fileHelper = new FileHelper();
        context = getApplicationContext();
        createAudioRecordingDirs(); //// TODO: 3/23/16 is this the correct place to do this
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public AudioRecorderManager getAudioRecorderManager() {
        return audioRecorderManager;
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

    private void createAudioRecordingDirs(){
        File recordingsDir = new File(getFilesDir(), "recordings");
        recordingsDir.mkdirs();
    }

    public String getFilePathPrefix(){
        return getFilesDir().getAbsolutePath()+"/recordings/";
    }

    public MediaRecorder getMediaRecorder(){
        return new MediaRecorder();
    }

    public ScheduledExecutorService getScheduledExecutorService(){
        return scheduledExecutorService;
    }

    public DecoratedMediaManager getDecoratedMediaManager() {
        return decoratedMediaManager;
    }
}
