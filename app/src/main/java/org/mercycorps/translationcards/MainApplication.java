package org.mercycorps.translationcards;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.support.annotation.NonNull;
import android.util.Log;

import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.porting.LanguagesImportUtility;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.PermissionService;
import org.mercycorps.translationcards.service.TranslationService;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Used to create singletons for dependency injection.
 *
 * @author patdale216@gmail.com (Pat Dale)
 * @author natashaj7@hotmail.com (Natasha Jimenez)
 */
public class MainApplication extends Application {

    private static String TAG = MainApplication.class.getName();
    public static final String PRE_BUNDLED_DECK_EXTERNAL_ID = "org.innovation.unhcr.txc-default-deck";
    private DatabaseHelper databaseHelper;
    private AudioRecorderManager audioRecorderManager;
    private static Context context;
    private ScheduledExecutorService scheduledExecutorService;
    private DecoratedMediaManager decoratedMediaManager;
    private TranslationService translationService;
    private DictionaryService dictionaryService;
    private DeckService deckService;
    private TranslationRepository translationRepository;
    protected boolean isTest = false;
    private LanguageService languageService;
    private PermissionService permissionService;
    private DeckRepository deckRepository;
    private DictionaryRepository dictionaryRepository;
    private TxcImportUtility txcImportUtility;

    private static BaseComponent baseComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        permissionService = new PermissionService();
        audioRecorderManager = new AudioRecorderManager();
        scheduledExecutorService = Executors.newScheduledThreadPool(1);
        AudioPlayerManager audioPlayerManager = new AudioPlayerManager(mediaPlayer);
        decoratedMediaManager = new DecoratedMediaManager(audioPlayerManager);
        context = getApplicationContext();
        createAudioRecordingDirs(); //// TODO: 3/23/16 is this the correct place to do this
        LanguagesImportUtility languagesImportUtility = createLanguagesImportUtility();
        languageService = new LanguageService(languagesImportUtility);
        if (isTest) return;
        databaseHelper = new DatabaseHelper(context);
        translationRepository = new TranslationRepository(databaseHelper);
        dictionaryRepository = new DictionaryRepository(databaseHelper, translationRepository, languageService);
        deckRepository = new DeckRepository(dictionaryRepository, databaseHelper, languageService);
        txcImportUtility = new TxcImportUtility(languageService, deckRepository, translationRepository, dictionaryRepository);
        checkForBundledDeckAndLoad(databaseHelper);
        deckService = new DeckService(languageService, Arrays.asList(deckRepository.getAllDecks()), deckRepository);
        dictionaryService = new DictionaryService(dictionaryRepository, deckService);
        translationService = new TranslationService(translationRepository, dictionaryService);

        ApplicationComponent applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        baseComponent = DaggerBaseComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
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

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public AudioRecorderManager getAudioRecorderManager() {
        return audioRecorderManager;
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

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public DecoratedMediaManager getDecoratedMediaManager() {
        return decoratedMediaManager;
    }

    public TranslationService getTranslationService() {
        return translationService;
    }

    public DictionaryService getDictionaryService() {
        return dictionaryService;
    }

    public DeckService getDeckService() {
        return deckService;
    }

    public LanguageService getLanguageService() {
        return languageService;
    }

    public PermissionService getPermissionService() {
        return permissionService;
    }

    public DeckRepository getDeckRepository() {
        return deckRepository;
    }

    public DictionaryRepository getDictionaryRepository() {
        return dictionaryRepository;
    }

    public TranslationRepository getTranslationRepository() {
        return translationRepository;
    }
}
