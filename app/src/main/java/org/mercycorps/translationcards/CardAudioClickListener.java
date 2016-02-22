package org.mercycorps.translationcards;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by njimenez and pdale on 2/18/16.
 */
public class CardAudioClickListener implements View.OnClickListener {
    private String filename;
    private final ProgressBar progressBar;
    private MediaPlayerManager lastMediaPlayerManager;

    public CardAudioClickListener(String audioFilename, ProgressBar progressBar,
                                  MediaPlayerManager lastMediaPlayerManager) {
        this.filename = audioFilename;
        this.progressBar = progressBar;
        this.lastMediaPlayerManager = lastMediaPlayerManager;
    }

    @Override
    public void onClick(View v) {
        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
        }

        lastMediaPlayerManager.play(filename, progressBar);
    }
}
