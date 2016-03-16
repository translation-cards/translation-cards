package org.mercycorps.translationcards;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.media.MediaPlayerManager;
import org.robolectric.TestLifecycleApplication;

import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;

public class TestMainApplication extends MainApplication implements TestLifecycleApplication {

    private DbManager dbManager = mock(DbManager.class);

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
}
