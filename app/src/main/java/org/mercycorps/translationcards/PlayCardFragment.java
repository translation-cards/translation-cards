package org.mercycorps.translationcards;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PlayCardFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PlayCardFragment";

    private Dictionary.Translation translationCard;
    private MediaPlayerManager lastMediaPlayerManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        translationCard = (Dictionary.Translation) getArguments().getSerializable("TranslationCard");

        setLandscapeScreenOrientation();
        View playCardView = inflater.inflate(R.layout.play_card_view, container, false);
        setTranslationText(playCardView);
        playTranslationAudio(playCardView);

        return playCardView;
    }

    private void playTranslationAudio(View playCardView) {
        ProgressBar progressBar = (ProgressBar) playCardView.findViewById(R.id.play_card_progress_bar);

        if (lastMediaPlayerManager != null) {
            lastMediaPlayerManager.stop();
        }
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            translationCard.setMediaPlayerDataSource(getContext(), mediaPlayer);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            return;
        }

        lastMediaPlayerManager = new MediaPlayerManager(mediaPlayer, progressBar);
        mediaPlayer.setOnCompletionListener(
                new ManagedMediaPlayerCompletionListener(lastMediaPlayerManager));
        progressBar.setMax(mediaPlayer.getDuration());
        mediaPlayer.start();
        new Thread(lastMediaPlayerManager).start();
    }

    private class MediaPlayerManager implements Runnable {

        private final Lock lock;
        private boolean running;
        private final MediaPlayer mediaPlayer;
        private final ProgressBar progressBar;

        public MediaPlayerManager(MediaPlayer mediaPlayer, ProgressBar progressBar) {
            lock = new ReentrantLock();
            running = true;
            this.mediaPlayer = mediaPlayer;
            this.progressBar = progressBar;
        }

        public boolean stop() {
            lock.lock();
            if (!running) {
                // Already stopped, just return false.
                lock.unlock();
                return false;
            }
            running = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(0);
                }
            });
            lock.unlock();
            return true;
        }

        private boolean tryUpdate() {
            lock.lock();
            if (!running) {
                lock.unlock();
                return false;
            }
            final int currentPosition = mediaPlayer.getCurrentPosition();
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(currentPosition);
                }
            });
            lock.unlock();
            return true;
        }

        @Override
        public void run() {
            while (tryUpdate()) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    // Do nothing.
                }
            }
        }
    }
    private class ManagedMediaPlayerCompletionListener implements MediaPlayer.OnCompletionListener {

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

    private void setTranslationText(View playCardView) {
        TextView playCardText = (TextView) playCardView.findViewById(R.id.play_card_text);
        playCardText.setText(translationCard.getTranslatedText());
        playCardText.setOnClickListener(this);
    }

    private void setLandscapeScreenOrientation() {
        if (getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();
        lastMediaPlayerManager.stop();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}
