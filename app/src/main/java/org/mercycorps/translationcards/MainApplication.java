package org.mercycorps.translationcards;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.util.Log;

import org.mercycorps.translationcards.dagger.BaseComponent;
import org.mercycorps.translationcards.dagger.DaggerBaseComponent;
import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.porting.LanguagesImportUtility;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.LanguageService;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

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
    private DeckRepository deckRepository;
    private TxcImportUtility txcImportUtility;

    private static BaseComponent baseComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        baseComponent = DaggerBaseComponent.builder().build();

        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        context = getApplicationContext();
        createAudioRecordingDirs(); //// TODO: 3/23/16 is this the correct place to do this
        LanguagesImportUtility languagesImportUtility = createLanguagesImportUtility();
        LanguageService languageService = new LanguageService(languagesImportUtility);
        if (isTest) return;
        DatabaseHelper databaseHelper = new DatabaseHelper(languageService);
        TranslationRepository translationRepository = new TranslationRepository(databaseHelper);
        DictionaryRepository dictionaryRepository = new DictionaryRepository(databaseHelper, translationRepository, languageService);
        deckRepository = new DeckRepository(dictionaryRepository, databaseHelper, languageService);
        txcImportUtility = new TxcImportUtility(languageService, deckRepository, translationRepository, dictionaryRepository);
        checkForBundledDeckAndLoad(databaseHelper);
    }

    public BaseComponent getBaseComponent(){
        return baseComponent;
    }

    @NonNull
    private LanguagesImportUtility createLanguagesImportUtility() {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open("language_codes.json");
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            inputStream = null;
        }
        LanguagesImportUtility languagesImportUtility = new LanguagesImportUtility(inputStream);
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return languagesImportUtility;
    }

    private void checkForBundledDeckAndLoad(DatabaseHelper dbHelper) {
        if (deckRepository.retrieveKeyForDeckWithExternalId(PRE_BUNDLED_DECK_EXTERNAL_ID) == DeckRepository.NONEXISTENT_ID) {
            txcImportUtility.loadBundledDeck(dbHelper.getWritableDatabase());
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
