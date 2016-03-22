package org.mercycorps.translationcards.media;

import android.media.MediaRecorder;

import java.util.Random;

public class MediaConfig {

    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int outputFormat = MediaRecorder.OutputFormat.THREE_GPP;
    private int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;

    private String fileName;

    public MediaConfig(String fileName) {
        this.fileName = fileName;
    }

    public int getAudioSource() {
        return audioSource;
    }

    public int getOutputFormat() {
        return outputFormat;
    }

    public int  getAudioEncoder() {
        return audioEncoder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setAudioSource(int audioSource) {
        this.audioSource = audioSource;
    }

    public void setOutputFormat(int outputFormat) {
        this.outputFormat = outputFormat;
    }

    public void setAudioEncoder(int audioEncoder) {
        this.audioEncoder = audioEncoder;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public static MediaConfig createMediaConfig(){
        String fileName = String.format("TranslationCards%d.3gp", System.currentTimeMillis());
       return new MediaConfig(fileName);
    }
}
