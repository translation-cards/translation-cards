package org.mercycorps.translationcards.media;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

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
        if(decoratedMediaManager.isCurrentlyPlayingSameCard(translation.getFilename())) {
            stopMediaPlayer();
        } else {
            stopMediaPlayer();
            try {
                decoratedMediaManager.play(translation.getFilename(), progressBar, translation.getIsAsset());
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

    private FileDescriptor getFileDescriptor() {
        FileDescriptor fileDescriptor;
        try {
            fileDescriptor = loadFile();
        } catch (IOException e) {
            Log.d(TAG, "Error preparing audio.");
            throw new IllegalArgumentException(e);
        }
        return fileDescriptor;
    }

    private FileDescriptor loadFile() throws IOException {
        FileDescriptor fileDescriptor;
        if (translation.getIsAsset()) {
            fileDescriptor = progressBar.getContext().getAssets().openFd(translation.getFilename()).getFileDescriptor();
        } else {
            fileDescriptor = new FileInputStream(translation.getFilename()).getFD();
        }
        return fileDescriptor;
    }
}
