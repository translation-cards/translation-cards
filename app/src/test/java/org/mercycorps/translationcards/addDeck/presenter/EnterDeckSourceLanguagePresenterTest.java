package org.mercycorps.translationcards.addDeck.presenter;

import android.app.Activity;
import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckDestinationLanguagesActivity;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckTitleActivity;
import org.mercycorps.translationcards.addDeck.activity.LanguageSelectorActivity;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.presenter.EnterDeckSourceLanguagePresenter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class EnterDeckSourceLanguagePresenterTest {

    private EnterDeckSourceLanguagePresenter presenter;
    private EnterDeckSourceLanguageActivity activity;
    private NewDeckContext newDeckContext;

    @Before
    public void setUp() throws Exception {
        activity = mock(EnterDeckSourceLanguageActivity.class);
        newDeckContext = mock(NewDeckContext.class);
        presenter = new EnterDeckSourceLanguagePresenter(activity, newDeckContext);
    }

    @Test
    public void shouldInflateBitmaps() {
        presenter.inflateBitmaps();

        verify(activity).setActivityBitmap(R.id.deck_source_language_image, R.drawable.enter_phrase_image);
    }

    @Test
    public void shouldInitializeSourceLanguageWithEnglishWhenNoSourceLanguageSelected() {
        presenter = new EnterDeckSourceLanguagePresenter(activity, new NewDeckContext());

        presenter.initSourceLanguage();

        verify(activity).updateSourceLanguage("English");
    }

    @Test
    public void shouldUpdateSourceLanguageWhenNewSourceLanguageIsSelected() {
        Intent intent = mock(Intent.class);
        String selectedLanguage = "A Language";
        when(intent.getStringExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY)).thenReturn(selectedLanguage);

        presenter.newSourceLanguageSelected(Activity.RESULT_OK, intent);

        verify(newDeckContext).setSourceLanguage(selectedLanguage);
        verify(activity).updateSourceLanguage(selectedLanguage);
    }

    @Test
    public void shouldNotUpdateSourceLanguageWhenNoNewSourceLanguageIsSelected() {
        presenter.newSourceLanguageSelected(Activity.RESULT_CANCELED, mock(Intent.class));

        verifyZeroInteractions(newDeckContext);
        verifyZeroInteractions(activity);
    }

    @Test
    public void shouldStartEnterDeckDestinationLanguagesActivityWhenNextButtonClicked() {
        presenter.nextButtonClicked();

        verify(activity).startActivityWithClass(EnterDeckDestinationLanguagesActivity.class);
    }

    @Test
    public void shouldStartEnterDeckTitleActivityWhenBackButtonClicked() {
        presenter.backButtonClicked();

        verify(activity).startActivityWithClass(EnterDeckTitleActivity.class);
    }

    @Test
    public void shouldStartLanguageSelectorActivityForAResultWhenLanguageIsClicked() {
        presenter.sourceLanguageClicked();

        verify(activity).startActivityForResult(LanguageSelectorActivity.class, 0);
    }
}