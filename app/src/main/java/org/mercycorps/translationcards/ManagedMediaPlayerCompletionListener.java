package org.mercycorps.translationcards;

import android.media.MediaPlayer;

public class ManagedMediaPlayerCompletionListener implements MediaPlayer.OnCompletionListener {

    private MediaPlayerManager manager;

    public ManagedMediaPlayerCompletionListener(MediaPlayerManager manager) {
        super();
        this.manager = manager;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        manager.stop();
    }
}
