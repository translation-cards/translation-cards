/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mercycorps.translationcards;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Stack;

/**
 * Activity to handle recording of new audio.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class RecordingActivity extends AppCompatActivity {

    private static final String TAG = "RecordingActivity";

    public static final String INTENT_KEY_DICTIONARY_ID = "dictionaryId";
    public static final String INTENT_KEY_DICTIONARY_LABEL = "dictionaryLabel";
    public static final String INTENT_KEY_TRANSLATION_ID = "translationId";
    public static final String INTENT_KEY_TRANSLATION_LABEL = "translationLabel";
    public static final String INTENT_KEY_TRANSLATION_IS_ASSET = "translationIsAsset";
    public static final String INTENT_KEY_TRANSLATION_FILENAME = "translationFilename";
    public static final String INTENT_KEY_TRANSLATION_TEXT = "translatedText";

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;
    private Deck deck;
    private Intent intent;

    private enum Step {
        INSTRUCTIONS,
        LABEL,
        AUDIO,
        DONE
    }

    private enum RecordingStatus {
        FRESH,
        RECORDING,
        RECORDED,
        LISTENING
    }

    private Stack<Step> stepHistory;
    private RecordingStatus recordingStatus;
    private boolean inEditMode;
    private long dictionaryId;
    private String dictionaryLabel;
    private long translationId;
    private String label;
    private boolean isAsset;
    private String filename;
    private boolean savedIsAsset;
    private String savedFilename;
    private String translatedText;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private ImageButton recordButton;
    private ImageButton listenButton;
    private ImageView currentBitmapView;
    private Bitmap currentBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stepHistory = new Stack<>();
        dictionaryId = getIntent().getLongExtra(INTENT_KEY_DICTIONARY_ID, -1);
        dictionaryLabel = getIntent().getStringExtra(INTENT_KEY_DICTIONARY_LABEL);
        translationId = getIntent().getLongExtra(INTENT_KEY_TRANSLATION_ID, -1);
        label = getIntent().getStringExtra(INTENT_KEY_TRANSLATION_LABEL);
        translatedText = getIntent().getStringExtra(INTENT_KEY_TRANSLATION_TEXT);
        deck = (Deck) getIntent().getSerializableExtra(DecksActivity.INTENT_KEY_DECK_ID);
        intent = new Intent();
        intent.putExtra(DecksActivity.INTENT_KEY_DECK_ID, deck);
        isAsset = savedIsAsset = getIntent().getBooleanExtra(
                INTENT_KEY_TRANSLATION_IS_ASSET, false);
        filename = savedFilename = getIntent().getStringExtra(INTENT_KEY_TRANSLATION_FILENAME);
        mediaRecorder = null;
        mediaPlayer = null;
        if (translationId == -1) {
            inEditMode = false;
            moveToInstructionsStep();
        } else {
            inEditMode = true;
            moveToLabelStep();
        }
        getSupportActionBar().hide();
    }

    @Override
    public void onBackPressed() {
        stepHistory.pop();
        if (stepHistory.empty()) {
            recycleBitmap();
            super.onBackPressed();
            return;
        }
        switch (stepHistory.peek()) {
            case INSTRUCTIONS:
                moveToInstructionsStep();
                break;
            case LABEL:
                moveToLabelStep();
                break;
            case AUDIO:
                moveToAudioStep();
                break;
            case DONE:
                moveToDoneStep();
                break;
        }
    }

    private void moveToInstructionsStep() {
        setContentView(R.layout.recording_instructions);
        recycleBitmap();
        currentBitmap = BitmapFactory.decodeResource(
                getResources(), R.drawable.recording_instructions_image);
        currentBitmapView = (ImageView) findViewById(R.id.recording_instructions_image);
        currentBitmapView.setImageBitmap(currentBitmap);
        TextView titleView = (TextView) findViewById(R.id.recording_instructions_title);
        titleView.setText(getString(R.string.recording_instructions_title, dictionaryLabel));
        ImageButton backButton = (ImageButton) findViewById(R.id.recording_instructions_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        Button startButton = (Button) findViewById(R.id.recording_instructions_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkRecordingPermission()) {
                    moveToLabelStep();
                } else {
                    ActivityCompat.requestPermissions(RecordingActivity.this,
                            new String[] {Manifest.permission.RECORD_AUDIO},
                            PERMISSIONS_REQUEST_RECORD_AUDIO);
                }
            }
        });
        stepHistory.push(Step.INSTRUCTIONS);
    }

    private void moveToLabelStep() {
        setContentView(R.layout.recording_label);
        TextView labelTitle = (TextView) findViewById(R.id.recording_label_title);
        labelTitle.setText(getString(R.string.recording_label_create_title, dictionaryLabel));
        recycleBitmap();
        currentBitmap = BitmapFactory.decodeResource(
                getResources(), R.drawable.recording_label_image);
        currentBitmapView = (ImageView) findViewById(R.id.recording_label_image);
        currentBitmapView.setImageBitmap(currentBitmap);
        final EditText labelField = (EditText) findViewById(R.id.recording_label_field);
        final EditText translatedTextField = (EditText) findViewById(R.id.recording_translated_text_field);
        fillPrepopulatedField(label, labelField, getString(R.string.recording_label_hint_text));
        fillPrepopulatedField(translatedText, translatedTextField, getString(R.string.translated_text_hint));
        if (inEditMode) {
            ImageView deleteButton = (ImageView) findViewById(R.id.recording_label_delete_image);
            deleteButton.setVisibility(View.VISIBLE);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DbManager dbm = new DbManager(RecordingActivity.this);
                    dbm.deleteTranslation(translationId);
                    if (!isAsset) {
                        File oldFile = new File(filename);
                        oldFile.delete();
                        if (!savedIsAsset && (savedFilename != null) &&
                                !savedFilename.equals(filename)) {
                            oldFile = new File(savedFilename);
                            oldFile.delete();
                        }
                    }
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            findViewById(R.id.recording_label_step_marker).setVisibility(View.GONE);
        }
        labelField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setFieldBasedOnFocus(hasFocus, labelField, R.string.recording_label_hint_text);
            }
        });

        translatedTextField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                setFieldBasedOnFocus(hasFocus, translatedTextField, R.string.recording_text_hint_text);
            }
        });

        labelField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing here.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0 && !s.equals(getString(R.string.recording_label_hint_text))) {
                    setLabelNextButtonEnabled(true);
                } else {
                    setLabelNextButtonEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing here.
            }
        });
        View nextButton = (View) findViewById(R.id.recording_label_next);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label = labelField.getText().toString();
                if (label.length() == 0 ||
                        label.equals(getString(R.string.recording_label_hint_text))) {
                    return;
                }

                translatedText = translatedTextField.getText().toString();
                if(translatedText.trim().isEmpty()
                        || translatedText.equals(getString(R.string.recording_text_hint_text))) {
                    translatedText = "";
                }
                moveToAudioStep();
            }
        });
        stepHistory.push(Step.LABEL);
    }

    private void setFieldBasedOnFocus(boolean hasFocus, EditText field, int hintText) {
        if (hasFocus && field.getText().toString().equals(
                getString(hintText))) {
            field.setText("");
            field.setTextColor(Color.BLACK);
        } else if (!hasFocus && field.getText().toString().trim().isEmpty()) {
            field.setHint(hintText);
        }
        if (hasFocus) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInputFromWindow(
                    field.getApplicationWindowToken(),
                    InputMethodManager.SHOW_FORCED, 0);
        }
    }

    private void fillPrepopulatedField(String fieldValue, EditText field, String hintText) {
        if (fieldValue != null && !fieldValue.isEmpty()) {
            field.setText(fieldValue);
            field.setTextColor(Color.BLACK);
            field.setSelection(fieldValue.length());
            setLabelNextButtonEnabled(true);
        } else {
            field.setHint(hintText);
        }
    }

    private void setLabelNextButtonEnabled(boolean enabled) {
        TextView text = (TextView) findViewById(R.id.recording_label_next_text);
        ImageView image = (ImageView) findViewById(R.id.recording_label_next_image);
        if (enabled) {
            text.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor));
            image.setImageResource(R.drawable.forward_arrow);
        } else {
            text.setTextColor(ContextCompat.getColor(this, R.color.textDisabled));
            image.setImageResource(R.drawable.forward_arrow_40p);
        }
    }

    private void moveToAudioStep() {
        recordingStatus = (filename == null) ? RecordingStatus.FRESH : RecordingStatus.RECORDED;
        setContentView(R.layout.recording_audio);
        if (recordingStatus == RecordingStatus.RECORDED) {
            setAudioSaveButtonEnabled();
        }
        recycleBitmap();
        TextView titleView = (TextView) findViewById(R.id.recording_audio_title);
        titleView.setText(getString(R.string.recording_audio_title, dictionaryLabel));
        TextView labelView = (TextView) findViewById(R.id.origin_translation_text);
        labelView.setText(label);
        findViewById(R.id.translation_indicator_layout).setVisibility(View.GONE);
        findViewById(R.id.text_indicator_divider).setVisibility(View.GONE);
        recordButton = (ImageButton) findViewById(R.id.recording_audio_button_record);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (recordingStatus) {
                    case FRESH:
                    case RECORDED:
                        startRecording();
                        break;
                    case RECORDING:
                        stopRecording();
                        setAudioSaveButtonEnabled();
                        break;
                }
            }
        });
        listenButton = (ImageButton) findViewById(R.id.recording_audio_button_listen);
        if (recordingStatus == RecordingStatus.RECORDED) {
            listenButton.setBackgroundResource(R.drawable.button_listen_enabled);
        }
        listenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (recordingStatus) {
                    case RECORDING:
                        stopRecording();
                        setAudioSaveButtonEnabled();
                        startListening();
                        break;
                    case RECORDED:
                        startListening();
                        break;
                    case LISTENING:
                        finishListening(mediaPlayer);
                        break;
                }
            }
        });
        View backButton = findViewById(R.id.recording_audio_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToLabelStep();
            }
        });
        View saveButton = findViewById(R.id.recording_audio_save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (filename == null) {
                    // There's no recording yet, they're not done.
                    return;
                }
                DbManager dbm = new DbManager(RecordingActivity.this);
                if (translationId == -1) {
                    translationId = dbm.addTranslationAtTop(dictionaryId, label, false, filename, translatedText);
                } else {
                    dbm.updateTranslation(translationId, label, isAsset, filename, translatedText);
                    // If we're replacing the audio and the prior file wasn't an included audio
                    // asset, delete it.
                    if (filename != null && savedFilename != null &&
                            !savedFilename.equals(filename) &&
                            !savedIsAsset) {
                        File oldFile = new File(savedFilename);
                        oldFile.delete();
                        savedFilename = filename;
                        savedIsAsset = isAsset;
                    }
                }
                moveToDoneStep();
            }
        });
        stepHistory.push(Step.AUDIO);
    }

    private void startRecording() {
        if (!checkRecordingPermission()) {
            return;
        }
        listenButton.setBackgroundResource(R.drawable.button_listen_enabled);
        if (!isAsset && (filename != null)) {
            File oldFile = new File(filename);
            oldFile.delete();
            filename = null;
        }
        File recordingsDir = new File(getFilesDir(), "recordings");
        recordingsDir.mkdirs();
        File targetFile = new File(
                recordingsDir,
                String.format("%d.3gp", (new Random()).nextInt() * (new Random()).nextInt()));
        filename = targetFile.getAbsolutePath();
        isAsset = false;
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(filename);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "Error preparing media recorder: " + e);
            // TODO(nworden): something
        }
        mediaRecorder.start();
        recordButton.setBackgroundResource(R.drawable.button_record_active);
        recordingStatus = RecordingStatus.RECORDING;
    }

    private void stopRecording() {
        recordButton.setBackgroundResource(R.drawable.button_record_enabled);
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        listenButton.setBackgroundResource(R.drawable.button_listen_enabled);
        recordingStatus = RecordingStatus.RECORDED;
    }

    private void startListening() {
        recordButton.setBackgroundResource(R.drawable.button_record_disabled);
        listenButton.setBackgroundResource(R.drawable.button_listen_active);
        recordingStatus = RecordingStatus.LISTENING;
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            if (isAsset) {
                AssetFileDescriptor fd = getAssets().openFd(filename);
                mediaPlayer.setDataSource(
                        fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                fd.close();
            } else {
                mediaPlayer.setDataSource(new FileInputStream(filename).getFD());
            }
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.d(TAG, "Error preparing audio.");
            throw new IllegalArgumentException(e);
            // TODO(nworden): something
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finishListening(mp);
            }
        });
        mediaPlayer.start();
    }

    private void finishListening(MediaPlayer mediaPlayer) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.reset();
        mediaPlayer.release();
        recordButton.setBackgroundResource(R.drawable.button_record_enabled);
        listenButton.setBackgroundResource(R.drawable.button_listen_enabled);
        recordingStatus = RecordingStatus.RECORDED;
    }

    private void setAudioSaveButtonEnabled() {
        TextView text = (TextView) findViewById(R.id.recording_audio_save_text);
        text.setTextColor(ContextCompat.getColor(this, R.color.primaryTextColor));
        ImageView image = (ImageView) findViewById(R.id.recording_audio_save_image);
        image.setImageResource(R.drawable.forward_arrow);
    }

    private void moveToDoneStep() {
        setContentView(R.layout.recording_done);
        recycleBitmap();
        currentBitmap = BitmapFactory.decodeResource(
                getResources(), R.drawable.recording_done_image);
        currentBitmapView = (ImageView) findViewById(R.id.recording_done_image);
        currentBitmapView.setImageBitmap(currentBitmap);
        TextView titleView = (TextView) findViewById(R.id.recording_done_title);
        titleView.setText(getString(R.string.recording_done_title, dictionaryLabel));
        TextView detailView = (TextView) findViewById(R.id.recording_done_detail);
        detailView.setText(getString(R.string.recording_done_detail, dictionaryLabel));
        TextView cardTextView = (TextView) findViewById(R.id.origin_translation_text);
        cardTextView.setText(label);
        TextView translatedCardText = (TextView) findViewById(R.id.translated_text);
        if (translatedText.trim().isEmpty()) {
            translatedCardText.setText(getString(R.string.translated_text_hint));
        } else {
            translatedCardText.setText(translatedText);
        }
        ImageView cardIndicator = (ImageView) findViewById(R.id.indicator_icon);
        cardIndicator.setBackgroundResource(R.drawable.back_arrow);
        findViewById(R.id.translation_child).setVisibility(View.VISIBLE);
        findViewById(R.id.translation_child_actions).setVisibility(View.GONE);

        View backButton = findViewById(R.id.recording_done_edit);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inEditMode = true;
                moveToLabelStep();
            }
        });

        View doneButton = (View) findViewById(R.id.recording_done_done);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycleBitmap();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        stepHistory.push(Step.DONE);
    }

    private void recycleBitmap() {
        if (currentBitmap != null) {
            currentBitmap.recycle();
            currentBitmapView.setImageBitmap(null);
            currentBitmap = null;
            System.gc();
        }
    }

    private boolean checkRecordingPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode) {
            case PERMISSIONS_REQUEST_RECORD_AUDIO:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    moveToLabelStep();
                } else {
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
                break;
        }
    }
}