package org.mercycorps.translationcards;

import android.media.MediaRecorder;

import org.mercycorps.translationcards.media.AudioRecorderManager;
import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.PermissionService;
import org.mercycorps.translationcards.service.TranslationService;
import org.robolectric.TestLifecycleApplication;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.ScheduledExecutorService;

import static org.mockito.Mockito.mock;

public class TestMainApplication extends MainApplication implements TestLifecycleApplication {

    private DatabaseHelper databaseHelper = mock(DatabaseHelper.class);
    private AudioRecorderManager audioRecorderManager = mock(AudioRecorderManager.class);
    private MediaRecorder mediaRecorder = mock(MediaRecorder.class);
    private ScheduledExecutorService mockedScheduledExecutorService = mock(ScheduledExecutorService.class);
    private TranslationService translationService = mock(TranslationService.class);
    private DictionaryService dictionaryService = mock(DictionaryService.class);
    private DeckService deckService = mock(DeckService.class);
    private PermissionService permissionService = mock(PermissionService.class);
    private DeckRepository deckRepository = mock(DeckRepository.class);
    private DictionaryRepository dictionaryRepository = mock(DictionaryRepository.class);
    private TranslationRepository translationRepository = mock(TranslationRepository.class);

    private static BaseComponent baseComponent;

    @Override
    public void onCreate() {
        isTest = true;
        super.onCreate();

        ApplicationComponent applicationComponent = DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        baseComponent = DaggerTestBaseComponent.builder()
                .applicationComponent(applicationComponent)
                .build();
    }

    @Override
    public BaseComponent getBaseComponent(){
        return baseComponent;
    }

    @Override
    public void beforeTest(Method method) {
        System.out.println();
    }

    @Override
    public void prepareTest(Object o) {

    }

    @Override
    public void afterTest(Method method) {

    }

    @Override
    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    @Override
    public AudioRecorderManager getAudioRecorderManager() {
        return audioRecorderManager;
    }

    @Override
    public FileDescriptor getFileDescriptor(String fileName) throws IOException {
        return new FileDescriptor();
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        return mediaRecorder;
    }

    @Override
    public ScheduledExecutorService getScheduledExecutorService(){
        return mockedScheduledExecutorService;
    }

    @Override
    public TranslationService getTranslationService() {
        return translationService;
    }

    @Override
    public DictionaryService getDictionaryService() {
        return dictionaryService;
    }

    @Override
    public DeckService getDeckService() {
        return deckService;
    }

    @Override
    public PermissionService getPermissionService() {
        return permissionService;
    }

    public DeckRepository getDeckRepository() {
        return deckRepository;
    }

    @Override
    public DictionaryRepository getDictionaryRepository() {
        return dictionaryRepository;
    }

    @Override
    public TranslationRepository getTranslationRepository() {
        return translationRepository;
    }

}
