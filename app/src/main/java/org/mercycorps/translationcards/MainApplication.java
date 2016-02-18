package org.mercycorps.translationcards;

import android.app.Application;

/**
 * Created by njimenez on 2/18/16.
 */
public class MainApplication extends Application {

    private MediaPlayerManager mediaPlayerManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayerManager = new MediaPlayerManager();
    }

    public MediaPlayerManager getMediaPlayerManager() {
        return mediaPlayerManager;
    }
}
