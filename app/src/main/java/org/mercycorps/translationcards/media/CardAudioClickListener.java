package org.mercycorps.translationcards.media;

import android.view.View;
import android.widget.ProgressBar;

import org.mercycorps.translationcards.data.Dictionary;

/**
 * Created by njimenez and pdale on 2/18/16.
 */
public class CardAudioClickListener implements View.OnClickListener {
    private Dictionary.Translation translation;
    private final ProgressBar progressBar;
    private MediaPlayerManager lastMediaPlayerManager;

    public CardAudioClickListener(Dictionary.Translation translation, ProgressBar progressBar,
                                  MediaPlayerManager lastMediaPlayerManager) {
        this.translation = translation;
        this.progressBar = progressBar;
        this.lastMediaPlayerManager = lastMediaPlayerManager;
    }

    @Override
    public void onClick(View v) {
        if(lastMediaPlayerManager.isCurrentlyPlayingSameCard(translation)) {
            stop();
        } else {
            stop();
            lastMediaPlayerManager.play(translation, progressBar);
        }
    }

    public void stop() {
        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
        }
    }
}
