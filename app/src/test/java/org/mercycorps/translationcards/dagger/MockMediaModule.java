package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@PerApplication
@Module
public class MockMediaModule {

    @PerApplication
    @Provides
    DecoratedMediaManager providesDecoratedMediaManager() {
        return mock(DecoratedMediaManager.class);
    }

    @PerApplication
    @Provides
    AudioPlayerManager providesAudioPlayerManager() {
        return mock(AudioPlayerManager.class);
    }

    @PerApplication
    @Provides
    AudioRecorderManager providesAudioRecorderManager() {
        return mock(AudioRecorderManager.class);
    }
}
