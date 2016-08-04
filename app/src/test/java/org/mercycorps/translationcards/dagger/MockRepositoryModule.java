package org.mercycorps.translationcards.dagger;

import org.mercycorps.translationcards.model.DatabaseHelper;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;

import dagger.Module;
import dagger.Provides;

import static org.mockito.Mockito.mock;

@Module
public class MockRepositoryModule {

    @Provides
    DatabaseHelper providesDatabaseHelper() {
        return mock(DatabaseHelper.class);
    }

    @PerApplication
    @Provides
    TranslationRepository providesTranslationRepository() {
        return mock(TranslationRepository.class);
    }

    @PerApplication
    @Provides
    DictionaryRepository providesDictionaryRepository() {
        return mock(DictionaryRepository.class);
    }

    @PerApplication
    @Provides
    DeckRepository providesDeckRepository() {
        return mock(DeckRepository.class);
    }
}
