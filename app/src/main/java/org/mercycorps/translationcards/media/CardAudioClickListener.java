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
            stopMediaPlayer();
        } else {
            stopMediaPlayer();
            lastMediaPlayerManager.play(getFileDescriptor(), progressBar, translation);
        }
    }

    public void stopMediaPlayer() {
        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
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
