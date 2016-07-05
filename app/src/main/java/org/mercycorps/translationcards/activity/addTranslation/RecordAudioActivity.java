package org.mercycorps.translationcards.activity.addTranslation;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.exception.RecordAudioException;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.media.MediaConfig;
import org.mercycorps.translationcards.service.PermissionService;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static org.mercycorps.translationcards.fragment.TranslationTabsFragment.OnLanguageTabSelectedListener;

public class RecordAudioActivity extends AddTranslationActivity {
    private static final String TAG = "RecordAudioActivity";

    @Bind(R.id.play_audio_button)
    RelativeLayout playAudioButton;
    @Bind(R.id.record_audio_button)
    RelativeLayout recordAudioButton;
    @Bind(R.id.origin_translation_text)
    TextView originTranslationText;
    @Bind(R.id.record_audio_icon)
    ImageView recordAudioIcon;
    @Bind(R.id.record_activity_back)
    LinearLayout backButton;
    @Bind(R.id.record_activity_back_arrow)
    ImageView backButtonArrow;
    @Bind(R.id.record_activity_next)
    LinearLayout nextButton;
    @Bind(R.id.recording_audio_next_text)
    TextView nextButtonText;
    @Bind(R.id.recording_audio_next_arrow)
    ImageView nextButtonArrow;
    @Bind(R.id.indicator_icon)
    ImageView translationCardIndicatorIcon;
    @Bind(R.id.translation_child)
    LinearLayout translationChild;
    @Bind(R.id.translation_grandchild)
    LinearLayout translationGrandChild;
    @Bind(R.id.translated_text)
    TextView translatedTextView;
    @Bind(R.id.audio_icon_layout)
    FrameLayout audioIconLayout;
    private PermissionService permissionService;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_record_audio);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionService = getMainApplication().getPermissionService();
    }

    @Override
    public void initStates() {
        inflateLanguageTabsFragment();
        setOnLanguageTabClickListener();
        updatePlayButtonState();
        showTranslationSourcePhrase();
        updateTranslatedTextView();
        updateNextButtonState(false);
        expandTranslationCard();
        hideGrandchildAndAudioIcon();
    }

    private void setOnLanguageTabClickListener() {
        getLanguageTabsFragment().setOnLanguageTabSelectedListener(new OnLanguageTabSelectedListener() {
            @Override
            public void onLanguageTabSelected(NewTranslation previousTranslation) {
                updatePlayButtonState();
                updateTranslatedTextView();
                stopAudioIfPlaying();
                stopIfRecording();
            }
        });
    }

    @OnClick(R.id.record_activity_next)
    public void recordActivityNextClick() {
        stopAudioIfPlaying();
        stopIfRecording();
        startNextActivity(RecordAudioActivity.this, SummaryActivity.class);
    }

    @OnClick(R.id.record_activity_back)
    public void recordActivityBackClick() {
        stopAudioIfPlaying();
        stopIfRecording();
        startNextActivity(RecordAudioActivity.this, EnterTranslatedPhraseActivity.class);
    }

    @OnClick(R.id.record_audio_button)
    public void recordAudioButtonClick() {
        if (!permissionService.checkPermission(this, Manifest.permission.RECORD_AUDIO )) {
            showPermissionDialog();
        } else {
            stopAudioIfPlaying();
            tryToRecord();
            boolean isRecording = getAudioRecorderManager().isRecording();
            updateRecordButtonState(isRecording);
            updateBackButtonState(isRecording);
            updateNextButtonState(isRecording);
            updatePlayButtonState();
        }
    }

    private void showPermissionDialog() {
        new AlertDialog.Builder(this)
                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        permissionService.requestPermissions(RecordAudioActivity.this, Manifest.permission.RECORD_AUDIO , permissionService.PERMISSIONS_REQUEST_RECORD_AUDIO);
                    }
                })
                .setView(R.layout.dialog_microphone_permission)
                .show();
    }

    @OnClick(R.id.play_audio_button)
    public void playAudioButtonClick() {
        try {
            stopIfRecording();
            playAudioFile();
        } catch (AudioFileException e) {
            Log.d(TAG, "Error getting audio asset: " + e);
            showToast(e.getMessage());
        }
    }

    @OnClick(R.id.translation_indicator_layout)
    protected void translationIndicatorLayoutClick() {
        int visibility = isTranslationChildVisible() ? View.GONE : View.VISIBLE;
        int backgroundResource = isTranslationChildVisible() ? R.drawable.expand_arrow : R.drawable.collapse_arrow;
        translationChild.setVisibility(visibility);
        translationCardIndicatorIcon.setBackgroundResource(backgroundResource);
    }

    protected void expandTranslationCard() {
        translationCardIndicatorIcon.setBackgroundResource(R.drawable.collapse_arrow);
        translationChild.setVisibility(View.VISIBLE);
    }

    protected void hideGrandchildAndAudioIcon() {
        translationGrandChild.setVisibility(View.GONE);
        audioIconLayout.setVisibility(View.GONE);
    }

    private void updateTranslatedTextView() {
        String translatedText = getLanguageTabsFragment().getCurrentTranslation().getTranslation().getTranslatedText();
        translatedTextView.setText(translatedText);
        if (translatedText.isEmpty()) {
            translatedTextView.setHint(String.format("Add %s translation", getLanguageTabsFragment().getCurrentTranslation().getDictionary().getLanguage()));
        }
    }

    private void updateNextButtonState(boolean isRecording) {
        List<NewTranslation> translations = getContextFromIntent().getNewTranslations();
        Boolean isAudioFilePresent = false;
        for (NewTranslation translation : translations) {
            if (translation.getTranslation().isAudioFilePresent()) {
                isAudioFilePresent = true;
                break;
            }
        }
        boolean isEnabled = isAudioFilePresent && !isRecording;
        nextButton.setClickable(isEnabled);
        int nextButtonTextColor = isEnabled ? R.color.primaryTextColor : R.color.textDisabled;
        nextButtonText.setTextColor(ContextCompat.getColor(this, nextButtonTextColor));
        int nextButtonArrowColor = isEnabled ? R.drawable.forward_arrow : R.drawable.forward_arrow_disabled;
        nextButtonArrow.setBackgroundResource(nextButtonArrowColor);
    }

    private void showTranslationSourcePhrase() {
        originTranslationText.setText(getContextFromIntent().getSourcePhrase());
    }

    private void updateBackButtonState(boolean isRecording) {
        boolean isEnabled = !isRecording;
        backButton.setClickable(isEnabled);
        int backButtonArrowColor = isEnabled ? R.drawable.back_arrow : R.drawable.back_arrow_disabled;
        backButtonArrow.setBackgroundResource(backButtonArrowColor);
    }

    private void updatePlayButtonState() {
        boolean translationHasAudioFile = getLanguageTabsFragment().getCurrentTranslation().getTranslation().isAudioFilePresent();
        int playButtonColor = translationHasAudioFile ? R.color.green : R.color.grey;
        playAudioButton.setBackgroundResource(playButtonColor);
        playAudioButton.setClickable(translationHasAudioFile);
    }

    private void stopIfRecording() {
        if (getAudioRecorderManager().isRecording()) {
            getAudioRecorderManager().stop();
            boolean isRecording = getAudioRecorderManager().isRecording();
            updateBackButtonState(isRecording);
            updateNextButtonState(isRecording);
            updateRecordButtonState(isRecording);
        }
    }

    private void updateRecordButtonState(boolean isRecording) {
        if (isRecording) {
            recordAudioButton.setBackgroundResource(R.color.deep_red);
            recordAudioIcon.setBackgroundResource(R.drawable.stop);
        } else {
            recordAudioButton.setBackgroundResource(R.color.red);
            recordAudioIcon.setBackgroundResource(R.drawable.record);
        }
    }

    private boolean isTranslationChildVisible() {
        return translationChild.getVisibility() == View.VISIBLE;
    }

    protected void stopAudioIfPlaying() {
        if (getAudioPlayerManager().isPlaying()) {
            getAudioPlayerManager().stop();
        }
    }


    private void tryToRecord() {
        try {
            handleIsRecordingState();
        } catch (RecordAudioException e) {
            Log.d(TAG, "Error creating file for recording: " + e);
            showToast(getString(R.string.unable_to_record_message));
        }
    }

    private void playAudioFile() throws AudioFileException {
        Translation translation = getLanguageTabsFragment().getCurrentTranslation().getTranslation();
        getAudioPlayerManager().play(translation.getFilename(), translation.getIsAsset());
    }

    private void handleIsRecordingState() throws RecordAudioException {
        AudioRecorderManager audioRecorderManager = getAudioRecorderManager();
        if (audioRecorderManager.isRecording()) {
            audioRecorderManager.stop();
        } else {
            MediaConfig mediaConfig = MediaConfig.createMediaConfig();
            getLanguageTabsFragment().getCurrentTranslation().setAudioFile(mediaConfig.getAbsoluteFilePath());
            updateIsAudioFileAsset();
            audioRecorderManager.record(mediaConfig);
        }
    }

    private void updateIsAudioFileAsset() {
        Translation translation = getLanguageTabsFragment().getCurrentTranslation().getTranslation();
        if (translation.getIsAsset()) {
            translation.setIsAsset(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (permissionService.permissionGranted(grantResults)) {
            ToastHelper.showToast(this, getString(R.string.permission_granted));
        }
    }
}
