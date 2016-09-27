package org.mercycorps.translationcards.addDeck.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.activity.EnterAuthorActivity;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckSourceLanguageActivity;
import org.mercycorps.translationcards.addDeck.activity.LanguageSelectorActivity;
import org.mercycorps.translationcards.addDeck.presenter.EnterDeckDestinationPresenter.EnterDeckDestinationView;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class EnterDeckDestinationPresenterTest {
    private static final String A_LANGUAGE = "A Language";
    private EnterDeckDestinationPresenter presenter;
    private EnterDeckDestinationView view;
    private NewDeckContext newDeckContext;

    @Before
    public void setup() {
        view = mock(EnterDeckDestinationView.class);
        newDeckContext = mock(NewDeckContext.class);
        presenter = new EnterDeckDestinationPresenter(view, newDeckContext);
    }
    @Test
    public void shouldSetBitMap() {
        presenter.setBitmaps();

        verify(view).setActivityBitmap(R.id.enter_deck_destination_image, R.drawable.enter_phrase_image);
    }

    @Test
    public void shouldSetNextButtonClickableWhenDestinationLanguagesExist() {
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add(A_LANGUAGE);
        }};
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);

        presenter.refreshView();

        verify(view).enableNextButton();
    }

    @Test
    public void shouldSetNextButtonToNotClickableWhenNoDestinationLanguagesArePresent() {
        when(newDeckContext.getDestinationLanguages()).thenReturn(new HashSet<String>());

        presenter.refreshView();

        verify(view).disableNextButton();
    }

    @Test
    public void shouldAddLanguagesToViewWhenPopulatingLanguageList() {
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add(A_LANGUAGE);
        }};
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);


        presenter.refreshView();

        verify(view).removeAllLanguageChips();
        verify(view).addLanguageChip(A_LANGUAGE);
    }

    @Test
    public void shouldNotAddLanguageChipsToViewWhenNoLanguagesExist() {
        presenter.refreshView();

        verify(view).removeAllLanguageChips();
        verify(view, times(0)).addLanguageChip(anyString());
    }

    @Test
    public void shouldRemoveALanguageChipAndRepopulateFlexboxWhenALanguageIsDeleted() {
        final String anotherLanguage = "Another Language";
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add(A_LANGUAGE);
            add(anotherLanguage);
        }};
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);

        presenter.deleteLanguage(A_LANGUAGE);

        assertFalse(newDeckContext.getDestinationLanguages().contains(A_LANGUAGE));
        verify(view).removeAllLanguageChips();
        verify(view).addLanguageChip(anotherLanguage);
    }

    @Test
    public void shouldDisableNextButtonWhenAllLanguageChipsAreDeleted() {
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add(A_LANGUAGE);
        }};
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);

        presenter.deleteLanguage(A_LANGUAGE);

        verify(view).disableNextButton();
    }

    @Test
    public void shouldNotAddNewLanguageChipWhenSelectedLanguageIsNull() {
        presenter.newLanguageSelected(null);

        assertEquals(new HashSet<String>(), newDeckContext.getDestinationLanguages());
        verifyZeroInteractions(view);
    }

    @Test
    public void shouldAddNewLanguageChipToViewWhenANewLanguageIsSelected() {
        when(newDeckContext.getDestinationLanguages()).thenReturn(new HashSet<>(Collections.singletonList("Some Language")));

        presenter.newLanguageSelected(A_LANGUAGE);

        verify(newDeckContext).addDestinationLanguage(A_LANGUAGE);
        verify(view).addLanguageChip(A_LANGUAGE);
        verify(view).enableNextButton();
    }

    @Test
    public void shouldStartEnterAuthorActivityWhenNextButtonIsClickedWithDestinationLanguages() {
        HashSet<String> destinationLanguages = new HashSet<String>() {{
            add(A_LANGUAGE);
        }};
        when(newDeckContext.getDestinationLanguages()).thenReturn(destinationLanguages);

        presenter.nextButtonClicked();

        verify(view).startActivityWithClass(EnterAuthorActivity.class);
    }

    @Test
    public void shouldNotStartEnterAuthorActivityWhenNextButtonIsClickedWithNoDestinationLanguages() {
        presenter.nextButtonClicked();

        verify(view, times(0)).startActivityWithClass(EnterAuthorActivity.class);
    }

    @Test
    public void shouldStartEnterDeckSourceLanguageActivityWhenBackButtonIsClicked() {
        presenter.backButtonClicked();

        verify(view).startActivityWithClass(EnterDeckSourceLanguageActivity.class);
    }

    @Test
    public void shouldStartLangaugeSelectorActivityForResultWhenAddingANewLanguage() {
        presenter.addNewDestinationLanguageClicked();

        int requestCode = 0;
        verify(view).startActivityForResult(LanguageSelectorActivity.class, requestCode);
    }
}