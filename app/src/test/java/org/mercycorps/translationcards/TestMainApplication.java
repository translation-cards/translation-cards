package org.mercycorps.translationcards;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.fileUtil.FileHelper;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.MediaPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.robolectric.TestLifecycleApplication;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;

public class TestMainApplication extends MainApplication implements TestLifecycleApplication {

    private DbManager dbManager = mock(DbManager.class);
    private AudioRecorderManager audioRecorderManager = mock(AudioRecorderManager.class);
    private AudioPlayerManager audioPlayerManager = mock(AudioPlayerManager.class);
    private FileHelper fileHelper = mock(FileHelper.class);

    @Override
    public void beforeTest(Method method) {
        System.out.println();
    }

    @Override
    public void prepareTest(Object o) {

    }

    @Override
    public void afterTest(Method method) {

    }

    @Override
    public MediaPlayerManager getMediaPlayerManager() {
        return mock(MediaPlayerManager.class);
    }

    @Override
    public DbManager getDbManager() {
        return dbManager;
    }

    @Override
    public AudioRecorderManager getAudioRecorderManager() {
        return audioRecorderManager;
    }

    @Override
    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    @Override
    public FileHelper getFileHelper(){
        return fileHelper;
    }

    @Override
    public FileDescriptor getFileDescriptor(String fileName) throws IOException {
        return new FileDescriptor();
    }
}
