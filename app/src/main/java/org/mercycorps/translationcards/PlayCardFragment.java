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

/**
 * Fragment to play translation card audio and show translated text
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
public class PlayCardFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PlayCardFragment";

    private static final String STATE_KEY_MEDIA_POSITION = "mediaPosition";

    private Dictionary.Translation translationCard;
    private MediaPlayerManager lastMediaPlayerManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View playCardView = inflater.inflate(R.layout.play_card_view, container, false);
        translationCard = (Dictionary.Translation) getArguments().getSerializable(
                "TranslationCard");
        setTranslationText(playCardView);
        int mediaPosition = -1;
        if (savedInstanceState != null) {
            mediaPosition = savedInstanceState.getInt(STATE_KEY_MEDIA_POSITION, -1);
        }
        playTranslationAudio(playCardView, mediaPosition);
        return playCardView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int mediaPosition = -1;
        if (lastMediaPlayerManager != null) {
            mediaPosition = lastMediaPlayerManager.stop();
        }
        outState.putInt(STATE_KEY_MEDIA_POSITION, mediaPosition);
    }

    @Override
    public void onStop() {
        super.onStop();
        lastMediaPlayerManager.stop();
    }

    @Override
    public void onClick(View v) {
        lastMediaPlayerManager.stop();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.popBackStack();

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    private void setTranslationText(View playCardView) {
        TextView playCardText = (TextView) playCardView.findViewById(R.id.play_card_text);
        String translatedText = translationCard.getTranslatedText();
        if (translatedText.isEmpty()) {
            translatedText = getString(R.string.no_translation_text);
        }
        playCardText.setText(translatedText);
        playCardText.setOnClickListener(this);
    }

    private boolean screenIsPortraitOriented() {
        return getContext().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }

    private void playTranslationAudio(View playCardView, int mediaPosition) {
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
        progressBar.setMax(mediaPlayer.getDuration());
        if (mediaPosition > 0) {
            mediaPlayer.seekTo(mediaPosition);
            progressBar.setProgress(mediaPosition);
        }

        lastMediaPlayerManager = new MediaPlayerManager(mediaPlayer, progressBar);
        mediaPlayer.setOnCompletionListener(
                new ManagedMediaPlayerCompletionListener(lastMediaPlayerManager));
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

        public int stop() {
            lock.lock();
            if (!running) {
                // Already stopped, just return false.
                lock.unlock();
                return -1;
            }
            running = false;
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            int position = mediaPlayer.getCurrentPosition();
            mediaPlayer.reset();
            mediaPlayer.release();
            progressBar.post(new Runnable() {
                @Override
                public void run() {
                    progressBar.setProgress(0);
                }
            });
            lock.unlock();
            return position;
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
                    Thread.sleep(100);
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
}
