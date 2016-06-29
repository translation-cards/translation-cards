package org.mercycorps.translationcards;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.media.AudioPlayerManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.TranslationService;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
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

    public static final String PRE_BUNDLED_DECK_EXTERNAL_ID = "org.innovation.unhcr.txc-default-deck";
    private DbManager dbManager;
    private AudioRecorderManager audioRecorderManager;
    private AudioPlayerManager audioPlayerManager;
    private static Context context;
    private  ScheduledExecutorService scheduledExecutorService;
    private DecoratedMediaManager decoratedMediaManager;
    private TranslationService translationService;
    private DictionaryService dictionaryService;
    private DeckService deckService;
    private TranslationRepository translationRepository;
    protected boolean isTest = false;
    private LanguageService languageService;
    private DeckRepository deckRepository;
    private DictionaryRepository dictionaryRepository;
    private TxcImportUtility txcImportUtility;

    @Override
    public void onCreate() {
        super.onCreate();
        MediaPlayer mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        languageService = new LanguageService();
        audioRecorderManager = new AudioRecorderManager();
        scheduledExecutorService =  Executors.newScheduledThreadPool(1);
        audioPlayerManager = new AudioPlayerManager(mediaPlayer);
        decoratedMediaManager = new DecoratedMediaManager();
        context = getApplicationContext();
        createAudioRecordingDirs(); //// TODO: 3/23/16 is this the correct place to do this
        if(isTest) return;
        dbManager = new DbManager(getApplicationContext());
        translationRepository = new TranslationRepository(dbManager);
        dictionaryRepository = new DictionaryRepository(dbManager.getDbh(), translationRepository);
        deckRepository = new DeckRepository(dictionaryRepository, dbManager.getDbh());
        deckService = new DeckService(languageService, Arrays.asList(deckRepository.getAllDecks()), deckRepository);
        dictionaryService = new DictionaryService(dictionaryRepository, deckService);
        translationService = new TranslationService(translationRepository, dictionaryService);
        txcImportUtility = new TxcImportUtility(languageService, deckRepository, translationRepository, dictionaryRepository);
        checkForBundledDeckAndLoad(dbManager.getDbh());
    }

    private void checkForBundledDeckAndLoad(DbManager.DbHelper dbHelper) {
        if(deckRepository.retrieveDeckWithExternalId(PRE_BUNDLED_DECK_EXTERNAL_ID) == DeckRepository.NONEXISTENT_ID){
            txcImportUtility.loadBundledDeck(dbHelper.getWritableDatabase());
        }
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public AudioRecorderManager getAudioRecorderManager() {
        return audioRecorderManager;
    }

    public AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManager;
    }

    public static Context getContextFromMainApp(){
        return context;
    }

    public FileDescriptor getFileDescriptor(String fileName) throws IOException {
        return new FileInputStream(new File(fileName)).getFD();
    }

    private void createAudioRecordingDirs(){
        File recordingsDir = new File(getFilesDir(), "recordings");
        recordingsDir.mkdirs();
    }

    public String getFilePathPrefix(){
        return getFilesDir().getAbsolutePath()+"/recordings/";
    }

    public MediaRecorder getMediaRecorder(){
        return new MediaRecorder();
    }

    public ScheduledExecutorService getScheduledExecutorService(){
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
