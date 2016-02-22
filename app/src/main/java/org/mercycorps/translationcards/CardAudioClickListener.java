package org.mercycorps.translationcards;

import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by njimenez and pdale on 2/18/16.
 */
public class CardAudioClickListener implements View.OnClickListener {
    private Dictionary.Translation translationCard;
    private final ProgressBar progressBar;
    private MediaPlayerManager lastMediaPlayerManager;

    public CardAudioClickListener(Dictionary.Translation translationCard, ProgressBar progressBar,
                                  MediaPlayerManager lastMediaPlayerManager) {
        this.translationCard = translationCard;
        this.progressBar = progressBar;
        this.lastMediaPlayerManager = lastMediaPlayerManager;
    }

    @Override
    public void onClick(View v) {
        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
        }

        lastMediaPlayerManager.play(translationCard.getFilename(), progressBar);
    }
}
