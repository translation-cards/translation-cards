package org.mercycorps.translationcards.exception;

/**
 * Created by karthikbalasubramanian on 3/28/16.
 */
public class AudioFileNotSetException extends Exception {
    public AudioFileNotSetException() {
        super();
    }

    public AudioFileNotSetException(String detailMessage) {
        super(detailMessage);
    }

    public AudioFileNotSetException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public AudioFileNotSetException(Throwable throwable) {
        super(throwable);
    }
}
