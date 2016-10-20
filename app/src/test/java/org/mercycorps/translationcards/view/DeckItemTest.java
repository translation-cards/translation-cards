package org.mercycorps.translationcards.view;

import android.app.Activity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DeckItemTest {

    private DeckItem deckItem;
    @Inject DeckService deckService;
    @Inject DictionaryService dictionaryService;
    private Deck deck;

    @Before
    public void setUp() throws Exception {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);

        deckItem = new DeckItem(buildActivity(Activity.class).create().get());
        deck = mock(Deck.class);
        deckItem.setDeck(deck);
    }

    @Test
    public void shouldStartTranslationsActivityWhenDeckItemIsClicked() {
        deckItem.deckClicked();

        verify(deckService).setCurrentDeck(deck);
        verify(dictionaryService).setCurrentDictionary(0);
        assertEquals(TranslationsActivity.class.getName(), shadowOf((Activity) deckItem.getContext()).getNextStartedActivity().getComponent().getClassName());
    }
}