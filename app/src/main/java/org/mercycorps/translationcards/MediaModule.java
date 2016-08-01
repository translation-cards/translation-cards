package org.mercycorps.translationcards;

import android.media.AudioManager;
import android.media.MediaPlayer;

import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaModule {

    @PerActivity
    @Provides
    DecoratedMediaManager providesDecoratedMediaManager(AudioPlayerManager audioPlayerManager, ScheduledExecutorService scheduledExecutorService) {
        return new DecoratedMediaManager(audioPlayerManager, scheduledExecutorService);
    }

    @Provides
    AudioPlayerManager providesAudioPlayerManager() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return new AudioPlayerManager(mediaPlayer);
    }

    @Provides
    ScheduledExecutorService providesScheduledExecutorService() {
        return Executors.newScheduledThreadPool(1);
    }
}