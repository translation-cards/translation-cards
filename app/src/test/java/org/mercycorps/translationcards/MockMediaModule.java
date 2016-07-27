package org.mercycorps.translationcards;

import org.mercycorps.translationcards.media.DecoratedMediaManager;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockMediaModule {

    DecoratedMediaManager decoratedMediaManager = mock(DecoratedMediaManager.class);

    @Provides
    DecoratedMediaManager providesDecoratedMediaManager() {
        return decoratedMediaManager;
    }
}
