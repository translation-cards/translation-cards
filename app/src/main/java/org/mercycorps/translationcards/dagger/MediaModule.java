package org.mercycorps.translationcards.dagger;

import android.media.AudioManager;
import android.media.MediaPlayer;

import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaModule {

    @PerApplication
    @Provides
    DecoratedMediaManager providesDecoratedMediaManager(AudioPlayerManager audioPlayerManager, ScheduledExecutorService scheduledExecutorService) {
        return new DecoratedMediaManager(audioPlayerManager, scheduledExecutorService);
    }

    @PerApplication
    @Provides
    AudioPlayerManager providesAudioPlayerManager() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return new AudioPlayerManager(mediaPlayer);
    }

    @PerApplication
    @Provides
    AudioRecorderManager providesAudioRecorderManager() {
        return new AudioRecorderManager();
    }

    @Provides
    ScheduledExecutorService providesScheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }
}