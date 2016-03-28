package org.mercycorps.translationcards.media;

import android.media.MediaRecorder;

import org.mercycorps.translationcards.MainApplication;

public class MediaConfig {

    private int audioSource = MediaRecorder.AudioSource.MIC;
    private int outputFormat = MediaRecorder.OutputFormat.THREE_GPP;
    private int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;

    private String absoluteFilePath;

    public MediaConfig(String absoluteFilePath) {
        this.absoluteFilePath = absoluteFilePath;
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

    public String getAbsoluteFilePath() {
        return absoluteFilePath;
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

    public void setAbsoluteFilePath(String absoluteFilePath) {
        this.absoluteFilePath = absoluteFilePath;
    }

    public static MediaConfig createMediaConfig(){
        return new MediaConfig(getFileNameWithPath());
    }

    public static String getFileNameWithPath(){
        String fileName = String.format("TranslationCards%d.3gp", System.currentTimeMillis());
        String filePathPrefix = ((MainApplication) MainApplication.getContextFromMainApp()).getFilePathPrefix();
        return filePathPrefix + fileName;
    }
}
