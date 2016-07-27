package org.mercycorps.translationcards;

import org.mercycorps.translationcards.media.DecoratedMediaManager;

import dagger.Module;
import dagger.Provides;

@Module
public class MediaModule {
    @Provides
    DecoratedMediaManager providesDecoratedMediaManager() {
        return new DecoratedMediaManager();
    }
}