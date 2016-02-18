package org.mercycorps.translationcards;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;

/**
 * Created by njimenez on 2/18/16.
 */
public class CardAudioClickListener implements View.OnClickListener {
    private static final String TAG = "CardAudioClickListener";
    private Dictionary.Translation translationCard;
    private final ProgressBar progressBar;
    private MediaPlayerManager lastMediaPlayerManager;

    public CardAudioClickListener(Dictionary.Translation translationCard, ProgressBar progressBar, MediaPlayerManager lastMediaPlayerManager) {
        this.translationCard = translationCard;
        this.progressBar = progressBar;
        this.lastMediaPlayerManager = lastMediaPlayerManager;
    }

    @Override
    public void onClick(View v) {
        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            translationCard.setMediaPlayerDataSource(progressBar.getContext(), mediaPlayer);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            return;
        }

        lastMediaPlayerManager.setMediaPlayer(mediaPlayer);
        lastMediaPlayerManager.setProgressBar(progressBar);
        mediaPlayer.setOnCompletionListener(
                new ManagedMediaPlayerCompletionListener(lastMediaPlayerManager));
        progressBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        new Thread(lastMediaPlayerManager).start();
    }
}
