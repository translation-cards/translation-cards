package org.mercycorps.translationcards;

import android.media.AudioManager;
import android.media.MediaPlayer;

import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaModule {
    @Provides
    DecoratedMediaManager providesDecoratedMediaManager() {
        return new DecoratedMediaManager();
    }

    @Provides
    AudioPlayerManager providesAudioPlayerManager() {
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        return new AudioPlayerManager(mediaPlayer);
    }
}