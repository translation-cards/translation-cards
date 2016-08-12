package org.mercycorps.translationcards;

import android.app.Application;
import android.content.Context;
import android.media.MediaRecorder;

import org.mercycorps.translationcards.dagger.BaseComponent;
import org.mercycorps.translationcards.dagger.DaggerBaseComponent;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.repository.DeckRepository;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

import javax.inject.Inject;

/**
 * Used to create singletons for dependency injection.
 *
 * @author patdale216@gmail.com (Pat Dale)
 * @author natashaj7@hotmail.com (Natasha Jimenez)
 */
public class MainApplication extends Application {

    private static String TAG = MainApplication.class.getName();
    public static final String PRE_BUNDLED_DECK_EXTERNAL_ID = "org.innovation.unhcr.txc-default-deck";
    private static Context context;
    protected boolean isTest = false;

    private static BaseComponent baseComponent;

    @Inject DeckRepository deckRepository;
    @Inject TxcImportUtility txcImportUtility;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        baseComponent = DaggerBaseComponent.builder().build();
        baseComponent.inject(this);

        createAudioRecordingDirs(); //// TODO: 3/23/16 is this the correct place to do this
        checkForBundledDeckAndLoad();
    }

    public BaseComponent getBaseComponent(){
        return baseComponent;
    }

    private void checkForBundledDeckAndLoad() {
        if (deckRepository.retrieveKeyForDeckWithExternalId(PRE_BUNDLED_DECK_EXTERNAL_ID) == DeckRepository.NONEXISTENT_ID) {
            txcImportUtility.loadBundledDeck();
        }
    }

    public static Context getContextFromMainApp() {
        return context;
    }

    public FileDescriptor getFileDescriptor(String fileName) throws IOException {
        return new FileInputStream(new File(fileName)).getFD();
    }

    private void createAudioRecordingDirs() {
        File recordingsDir = new File(getFilesDir(), "recordings");
        recordingsDir.mkdirs();
    }

    public String getFilePathPrefix() {
        return getFilesDir().getAbsolutePath() + "/recordings/";
    }

    public MediaRecorder getMediaRecorder() {
        return new MediaRecorder();
    }
}
