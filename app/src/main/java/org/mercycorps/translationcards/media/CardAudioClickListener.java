package org.mercycorps.translationcards.media;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

/**
 * Created by njimenez and pdale on 2/18/16.
 */
public class CardAudioClickListener implements View.OnClickListener {
    private static final String TAG = "CardAudioClickListener";
    private Translation translation;
    private final ProgressBar progressBar;
    private DecoratedMediaManager decoratedMediaManager;
    private String currentDictionary;

    public CardAudioClickListener(Translation translation, ProgressBar progressBar,
                                  DecoratedMediaManager decoratedMediaManager, String currentDictionary) {
        this.translation = translation;
        this.progressBar = progressBar;
        this.decoratedMediaManager = decoratedMediaManager;
        this.currentDictionary = currentDictionary;
    }

    @Override
    public void onClick(View v) {
        if (decoratedMediaManager.isCurrentlyPlayingSameCard(translation.getFilePath())) {
            stopMediaPlayer();
        } else {
            stopMediaPlayer();
            try {
                decoratedMediaManager.play(translation.getFilePath(), progressBar, translation.getIsAsset());
            } catch (AudioFileException e) {
                Context context = progressBar.getContext();
                String noAudioMessage = String.format(context.getString(R.string.could_not_play_audio_message), currentDictionary);
                ToastHelper.showToast(context, noAudioMessage);
            }
        }
    }

    public void stopMediaPlayer() {
        if (decoratedMediaManager != null) {
            decoratedMediaManager.stop();
        }
    }
}
