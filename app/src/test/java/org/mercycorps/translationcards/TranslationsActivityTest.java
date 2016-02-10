package org.mercycorps.translationcards;

import android.content.Intent;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.inject.AbstractModule;

import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import roboguice.RoboGuice;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationsActivityTest {

    public static final int DEFAULT_DECK_ID = 1;
    public static final String DEFAULT_DECK_NAME = "Default";
    public static final String NO_VALUE = "";
    public static final long DEFAULT_LONG = -1;
    public static final String DICTIONARY_TEST_LABEL = "TestLabel";
    public static final String TRANSLATED_TEXT = "TranslatedText";
    public static final String TRANSLATION_LABEL = "TranslationLabel";
    private TranslationsActivity translationsActivity;
    private DbManager dbManagerMock;

    @Before
    public void setUp() {
        RoboGuice.setUseAnnotationDatabases(false);
        Intent intent = new Intent();
        Deck deck = new Deck(DEFAULT_DECK_NAME, NO_VALUE, DEFAULT_DECK_ID, DEFAULT_LONG);
        intent.putExtra("Deck", deck);
        initializeMockDbManager();
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application,
                new TranslationsActivityTestModule());
        RoboGuice.getInjector(RuntimeEnvironment.application).injectMembers(this);
        translationsActivity = Robolectric.buildActivity(TranslationsActivity.class).withIntent(intent).create().get();
    }

    private void initializeMockDbManager() {
        dbManagerMock = mock(DbManager.class);
        Dictionary[] dictionaries = new Dictionary[1];
        Dictionary.Translation[] translations = new Dictionary.Translation[1];
        translations[0] = new Dictionary.Translation(TRANSLATION_LABEL, false, "", DEFAULT_LONG,
                TRANSLATED_TEXT);
        dictionaries[0] = new Dictionary(DICTIONARY_TEST_LABEL, translations, DEFAULT_LONG, DEFAULT_DECK_ID);
        when(dbManagerMock.getAllDictionariesForDeck(DEFAULT_DECK_ID)).thenReturn(dictionaries);
    }

    @Test
    public void onCreate_shouldShowDeckNameInToolbar() {
        assertThat(translationsActivity.getSupportActionBar().getTitle().toString(), is("Default"));
    }

    @Test
    public void onCreate_dbmGetsCalled() {
        verify(dbManagerMock).getAllDictionariesForDeck(DEFAULT_DECK_ID);
    }

    @Test
    public void onCreate_shouldInitializeTranslationsAdapter() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);
        View translationsListItem = translationsList.getAdapter().getView(1, null, translationsList);

        TextView originTranslationText = (TextView) translationsListItem.findViewById(R.id.origin_translation_text);
        assertThat(originTranslationText.getText().toString(), is(TRANSLATION_LABEL));

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);
        assertThat(translatedText.getText().toString(), is(TRANSLATED_TEXT));
    }

    @Test
    public void onClick_shouldInflateAndDeflateTranslationListItem() {
        ListView translationsList = (ListView) translationsActivity
                .findViewById(R.id.translations_list);

        View translationsListItem = translationsList.getAdapter().getView(1, null, translationsList);

        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.GONE));

        translationsListItem.performClick();
        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.VISIBLE));

        translationsListItem.performClick();
        assertThat(translationsListItem.findViewById(R.id.translation_child).getVisibility(), is(View.GONE));
    }

    @Test
    public void initTabs_shouldShowLanguageTabWhenOnHomeScreen() {
        LinearLayout tabContainer = (LinearLayout) translationsActivity.findViewById(R.id.tabs);

        assertThat(tabContainer.getChildCount(), is(1));

        View languageTab = tabContainer.getChildAt(0);
        TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
        assertThat(languageTabText.getText().toString(), is(DICTIONARY_TEST_LABEL.toUpperCase()));
    }

    @Test
    public void setDictionary_shouldNotHaveAnyTranslationCardsWhenNoneHaveBeenCreated() {
        TextView translationCardText = (TextView) translationsActivity.findViewById(R.id.card_text);

        assertThat(translationCardText, is(nullValue()));
    }

    @Test
    public void shouldGoToDecksActivityWhenBackButtonPressed() {
        ShadowActivity shadowActivity = Shadows.shadowOf(translationsActivity);

        shadowActivity.onBackPressed();
        assertTrue(shadowActivity.isFinishing());
    }

    public class TranslationsActivityTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(DbManager.class).toInstance(dbManagerMock);
        }

    }
}