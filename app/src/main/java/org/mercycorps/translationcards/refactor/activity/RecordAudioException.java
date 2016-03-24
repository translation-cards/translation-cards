package org.mercycorps.translationcards.refactor.activity;

public class RecordAudioException extends Exception {

    public RecordAudioException() {
    }

    public RecordAudioException(String detailMessage) {
        super(detailMessage);
    }

    public RecordAudioException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public RecordAudioException(Throwable throwable) {
        super(throwable);
    }
}
