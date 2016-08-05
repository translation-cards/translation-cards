package org.mercycorps.translationcards;

import android.media.MediaRecorder;

import org.mercycorps.translationcards.dagger.ApplicationComponent;
import org.mercycorps.translationcards.dagger.ApplicationModule;
import org.mercycorps.translationcards.dagger.BaseComponent;
import org.mercycorps.translationcards.dagger.DaggerApplicationComponent;
import org.mercycorps.translationcards.dagger.DaggerTestBaseComponent;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.TranslationService;
import org.robolectric.TestLifecycleApplication;

import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;

import static org.mockito.Mockito.mock;

public class TestMainApplication extends MainApplication implements TestLifecycleApplication {

    private MediaRecorder mediaRecorder = mock(MediaRecorder.class);
    private TranslationService translationService = mock(TranslationService.class);
    private DictionaryService dictionaryService = mock(DictionaryService.class);
    private DeckService deckService = mock(DeckService.class);

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
    public FileDescriptor getFileDescriptor(String fileName) throws IOException {
        return new FileDescriptor();
    }

    @Override
    public DeckService getDeckService() {
        return deckService;
    }

    @Override
    public MediaRecorder getMediaRecorder() {
        return mediaRecorder;
    }

    @Override
    public TranslationService getTranslationService() {
        return translationService;
    }

    @Override
    public DictionaryService getDictionaryService() {
        return dictionaryService;
    }
}
