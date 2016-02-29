package org.mercycorps.translationcards.media;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.mercycorps.translationcards.data.Dictionary;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by njimenez and pdale on 2/18/16.
 */
public class CardAudioClickListener implements View.OnClickListener {
    private static final String TAG = "CardAudioClickListener";
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
            lastMediaPlayerManager.play(getFileDescriptor(translation.getFilename()), progressBar, translation);
        }
    }

    private FileDescriptor getFileDescriptor(String filename) {
        FileDescriptor fileDescriptor;
        try {
            fileDescriptor = new FileInputStream(filename).getFD();
        } catch (IOException e) {
            Log.d(TAG, "Error preparing audio.");
            throw new IllegalArgumentException(e);
        }
        return fileDescriptor;
    }

    public void stop() {
        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
        }
    }
}
