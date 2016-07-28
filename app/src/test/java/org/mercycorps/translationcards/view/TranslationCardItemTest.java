package org.mercycorps.translationcards.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestBaseComponent;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.TranslationService;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.res.Attribute;
import org.robolectric.shadows.RoboAttributeSet;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;

import javax.inject.Inject;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by abeltamrat on 7/12/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationCardItemTest {
    public static final String DEFAULT_DICTIONARY_LABEL = "Dictionary";
    public static final String DEFAULT_AUDIO_FILE = "audio.mp3";
    public static final boolean IS_NOT_ASSET = false;

    private Activity activity;
    private DeckService deckService;

    @Inject
    DecoratedMediaManager decoratedMediaManager;

    @Before
    public void setUp() throws Exception {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);

        activity = Robolectric.buildActivity(Activity.class).create().get();
        deckService = ((TestMainApplication) RuntimeEnvironment.application).getDeckService();
        Deck basicDeck = new Deck("Test Deck", "", "1", 1, false, new Language("eng", "Langauge"));
        when(deckService.currentDeck()).thenReturn(basicDeck);
    }

    private TranslationCardItem getDefaultTranslationCard() {
        TranslationCardItem translationCardItem = new TranslationCardItem(activity);
        Translation translationItem = new Translation("First Translation", false, null, 1, "Translated Text");
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL);
        return translationCardItem;
    }

    @Test
    public void shouldSetTranslationSourceTextWhenLoaded() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        assertEquals("First Translation", ((TextView) translationCardItem.findViewById(R.id.origin_translation_text)).getText());
    }

    @Test
    public void shouldSetDestinationTranslationTextWhenLoaded() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        assertEquals("Translated Text", ((TextView) translationCardItem.findViewById(R.id.translated_text)).getText());
    }

    @Test
    public void shouldHaveCollapseIconVisibleWhenLoaded() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        ImageView expansionIndicatorIcon = (ImageView) translationCardItem.findViewById(R.id.indicator_icon);
        assertThat(shadowOf(expansionIndicatorIcon.getBackground()).getCreatedFromResId(), is(R.drawable.collapse_arrow));
    }

    @Test
    public void shouldHaveExpandIconVisibleWhenLoadedAndCollapsed() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        ImageView expansionIndicatorIcon = (ImageView) translationCardItem.findViewById(R.id.indicator_icon);

        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertEquals(R.drawable.expand_arrow, shadowOf(expansionIndicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldCollapseCardWhenIndicatorIconIsClicked() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        View translationChild = translationCardItem.findViewById(R.id.translation_child);
        assertEquals(View.GONE, translationChild.getVisibility());
    }

    @Test
    public void shouldShowExpandedCardTopBackgroundWhenExpanded() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        View translationParent = translationCardItem.findViewById(R.id.translation_card_parent);
        assertEquals(R.drawable.card_top_background_expanded, shadowOf(translationParent.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldNotShowExpandedCardTopBackgroundWhenCollapsed() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        View translationParent = translationCardItem.findViewById(R.id.translation_card_parent);
        assertEquals(R.drawable.card_top_background, shadowOf(translationParent.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldShowTranslationChildWhenActivityIsCreated() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        View translationCardChild = translationCardItem.findViewById(R.id.translation_child);
        assertEquals(View.VISIBLE, translationCardChild.getVisibility());
    }

    @Test
    public void shouldShowCardBottomWhenCardIndicatorIsClickedTwice() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertEquals(View.VISIBLE, translationCardItem.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowCollapseCardIndicatorWhenIndicatorIsClickedTwice() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        ImageView indicatorIcon = (ImageView) translationCardItem.findViewById(R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    @TargetApi(19)
    public void shouldGreyOutTranslationCardWhenItContainsNoAudio() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        LinearLayout translationCardParent = (LinearLayout) translationCardItem.findViewById(R.id.translation_card_parent);
        LayerDrawable bgDrawable = (LayerDrawable) translationCardParent.getBackground();
        GradientDrawable background = (GradientDrawable) bgDrawable.findDrawableByLayerId(R.id.card_top_background_expanded);
        assertEquals(TranslationCardItem.DISABLED_OPACITY, background.getAlpha());
    }

    @Test
    public void shouldDisplayMuteIconWhenTranslationContainsNoAudio() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        View audioIcon = translationCardItem.findViewById(R.id.audio_icon);
        assertThat(shadowOf(audioIcon.getBackground()).getCreatedFromResId(), is(R.drawable.no_audio_40));
    }

    @Test
    public void shouldDisplayAudioIconWhenTranslationContainsAudio() {
        TranslationCardItem translationCardItem = createTranslationCardItemWithAudioAndNoTranslatedText();
        View audioIcon = translationCardItem.findViewById(R.id.audio_icon);
        assertThat(shadowOf(audioIcon.getBackground()).getCreatedFromResId(), is(R.drawable.audio));
    }

    @Test
    public void shouldBeExpandedWhenExpandAttributeSetToTrue() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("org.mercycorps.translationcards:attr/expandedOnStart",
                String.valueOf(true), "org.mercycorps.translationcards"));
        AttributeSet attrs = new RoboAttributeSet(attributes, shadowOf(activity).getResourceLoader());
        TranslationCardItem tc = new TranslationCardItem(RuntimeEnvironment.application, attrs);
        Translation translationItem = new Translation("First Translation", false, null, 1, "Translated Text");
        tc.setTranslation(translationItem, "English");
        assertEquals(View.VISIBLE, tc.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowToastNotificationWhenTranslationCardWithoutAudioFileIsClicked() throws AudioFileException {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        when(decoratedMediaManager.isPlaying()).thenReturn(false);
        doThrow(new AudioFileException()).when(decoratedMediaManager).play(anyString(), any(ProgressBar.class), anyBoolean());

        translationCardItem.findViewById(R.id.translation_card).performClick();

        assertEquals(DEFAULT_DICTIONARY_LABEL + " translation not recorded.", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void shouldBeCollapsedWhenExpandAttributeSetToFalse() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("org.mercycorps.translationcards:attr/expandedOnStart",
                String.valueOf(false), "org.mercycorps.translationcards"));
        AttributeSet attrs = new RoboAttributeSet(attributes, shadowOf(activity).getResourceLoader());
        TranslationCardItem tc = new TranslationCardItem(RuntimeEnvironment.application, attrs);
        Translation translationItem = new Translation("First Translation", false, null, 1, "Translated Text");
        tc.setTranslation(translationItem, "English");
        assertEquals(View.GONE, tc.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldStopPlayingWhenPlayButtonIsClickedTwice() throws AudioFileNotSetException {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        when(decoratedMediaManager.isPlaying()).thenReturn(false).thenReturn(true);
        createTranslationCardItemWithAudioAndNoTranslatedText();
        translationCardItem.findViewById(R.id.translation_card).performClick();
        translationCardItem.findViewById(R.id.translation_card).performClick();
        verify(decoratedMediaManager).stop();
    }

    @Test
    public void shouldGreyOutTranslationSourceTextWhenItContainsNoAudio() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        TextView translationText = (TextView) translationCardItem.findViewById(R.id.origin_translation_text);
        assertEquals(getColor(activity, R.color.textDisabled), translationText.getCurrentTextColor());
    }

    @Test
    public void shouldPlayAudioFileWhenTranslationCardIsClicked() throws AudioFileException {
        TranslationCardItem translationCardItem = createTranslationCardItemWithAudioAndNoTranslatedText();
        translationCardItem.findViewById(R.id.translation_card).performClick();
        ProgressBar progressBar = (ProgressBar) translationCardItem.findViewById(R.id.translation_card_progress_bar);
        verify(decoratedMediaManager).play(DEFAULT_AUDIO_FILE, progressBar, IS_NOT_ASSET);
    }


    @Test
    public void shouldShowHintTextWhenNoTranslatedTextPhraseIsProvided() {
        TranslationCardItem translationCardItem = createTranslationCardItemWithAudioAndNoTranslatedText();
        TextView translatedTextView = (TextView) translationCardItem.findViewById(R.id.translated_text);
        assertEquals(String.format("Add %s translation", DEFAULT_DICTIONARY_LABEL), translatedTextView.getHint());
    }

    @Test
    public void shouldSetTextViewToEmptyStringWhenNoTranslatedTextPhraseIsProvided() {
        TranslationCardItem translationCardItem = createTranslationCardItemWithAudioAndNoTranslatedText();

        TextView translatedTextView = (TextView) translationCardItem.findViewById(R.id.translated_text);
        assertEquals("", translatedTextView.getText().toString());
    }

    @Test
    public void shouldHideEditAndDeleteOptionsIfCardIsLocked() {
        DeckService deckService = ((MainApplication) RuntimeEnvironment.application).getDeckService();
        Deck basicDeck = new Deck("Test Deck", "", "1", 1, true, new Language("eng", "Langauge"));
        when(deckService.currentDeck()).thenReturn(basicDeck);
        TranslationCardItem tc = getTranslationCardItemWithEditAndDeleteButtonsConfiguredToShow();
        assertEquals(View.GONE, tc.findViewById(R.id.translation_grandchild).getVisibility());
    }

    @Test
    public void shouldShowEditAndDeleteSectionIfAttributeSetToShow() {
        TranslationCardItem tc = getTranslationCardItemWithEditAndDeleteButtonsConfiguredToShow();
        tc.findViewById(R.id.indicator_icon).performClick();
        assertEquals(View.VISIBLE, tc.findViewById(R.id.translation_grandchild).getVisibility());
    }


    @Test
    public void shouldHaveEditCardIconWhenAttributeSet() {
        TranslationCardItem tc = getTranslationCardItemWithEditAndDeleteButtonsConfiguredToShow();

        ImageView editCardIcon = (ImageView) tc.findViewById(R.id.edit_card_icon);
        assertThat(editCardIcon, is(notNullValue()));
    }

    @Test
    public void shouldHaveEditCardLabelWhenAttributeSet() {
        TranslationCardItem tc = getTranslationCardItemWithEditAndDeleteButtonsConfiguredToShow();

        TextView editCardLabel = (TextView) tc.findViewById(R.id.edit_card_label);
        assertThat(editCardLabel.getText().toString(), is("Edit this flashcard"));
    }

    @Test
    public void shouldHaveDeleteCardIconWhenAttributeSet() {
        TranslationCardItem tc = getTranslationCardItemWithEditAndDeleteButtonsConfiguredToShow();
        ImageView deleteCardIcon = (ImageView) tc.findViewById(R.id.delete_card_icon);
        assertThat(deleteCardIcon, is(notNullValue()));
    }

    @Test
    public void shouldHaveDeleteCardLabelWhenAttributeSet() {
        TranslationCardItem tc = getTranslationCardItemWithEditAndDeleteButtonsConfiguredToShow();

        TextView deleteCardLabel = (TextView) tc.findViewById(R.id.delete_card_label);
        assertThat(deleteCardLabel.getText().toString(), is("Delete this flashcard"));
    }

    @NonNull
    private TranslationCardItem getTranslationCardItemWithEditAndDeleteButtonsConfiguredToShow() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("org.mercycorps.translationcards:attr/showEditAndDeleteOptions",
                String.valueOf(true), "org.mercycorps.translationcards"));
        AttributeSet attrs = new RoboAttributeSet(attributes, shadowOf(activity).getResourceLoader());
        TranslationCardItem tc = new TranslationCardItem(RuntimeEnvironment.application, attrs);
        Translation translationItem = new Translation("First Translation", false, null, 1, "Translated Text");
        tc.setTranslation(translationItem, "English");
        return tc;
    }

    @Test
    public void shouldShowTranslationCardChildWhenTranslationServiceReturnsExpanded() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        Translation translationItem = new Translation("First Translation", false, DEFAULT_AUDIO_FILE, 1, "");
        TranslationService translationService = ((TestMainApplication) RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(true);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, 0);
        View childView = translationCardItem.findViewById(R.id.translation_child);

        assertEquals(View.VISIBLE, childView.getVisibility());
    }

    @Test
    public void shouldHideTranslationCardChildWhenTranslationServiceReturnsNotExpanded() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        Translation translationItem = new Translation("First Translation", false, DEFAULT_AUDIO_FILE, 1, "");
        TranslationService translationService = ((TestMainApplication) RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(false);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, 0);
        View childView = translationCardItem.findViewById(R.id.translation_child);

        assertEquals(View.GONE, childView.getVisibility());
    }

    @Test
    public void shouldCallLanguageServiceOnExpansionClick() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        Translation translationItem = new Translation("First Translation", false, DEFAULT_AUDIO_FILE, 1, "");
        TranslationService translationService = ((TestMainApplication) RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(false);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, 0);
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();

        verify(translationService).expandCard(0);
    }

    @Test
    public void shouldHaveCorrectTextFormattingWhenTranslatedTextIsEmpty() {
        int disabledTextColor = ContextCompat.getColor(activity, R.color.textDisabled);
        TranslationCardItem translationsListItem = getDefaultTranslationCard();
        Translation translationItem = new Translation("First Translation", false, DEFAULT_AUDIO_FILE, 1, "");
        translationsListItem.setTranslation(translationItem, "English");

        TextView translatedText = (TextView) translationsListItem.findViewById(R.id.translated_text);

        assertEquals(18f, translatedText.getTextSize());
        assertEquals(disabledTextColor, translatedText.getCurrentTextColor());
    }

    @Test
    public void shouldCallLanguageServiceOnCollapseClick() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        Translation translationItem = new Translation("First Translation", false, DEFAULT_AUDIO_FILE, 1, "");
        TranslationService translationService = ((TestMainApplication) RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(true);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, 0);
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        verify(translationService).minimizeCard(0);
    }

    @Test
    public void shouldSetDeleteClickListenerIfCardNotLocked() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        View.OnClickListener deleteListener = mock(View.OnClickListener.class);
        translationCardItem.setDeleteClickListener(deleteListener);
        translationCardItem.findViewById(R.id.translation_card_delete).performClick();
        verify(deleteListener).onClick(any(View.class));
    }

    @Test
    public void shouldNotSetDeleteClickListenerIfCardLocked() {
        Deck basicDeck = new Deck("Test Deck", "", "1", 1, true, new Language("eng", "Langauge"));
        when(deckService.currentDeck()).thenReturn(basicDeck);
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        View.OnClickListener deleteListener = mock(View.OnClickListener.class);
        translationCardItem.setDeleteClickListener(deleteListener);
        translationCardItem.findViewById(R.id.translation_card_delete).performClick();
        verifyZeroInteractions(deleteListener);
    }

    @Test
    public void shouldSetEditClickListenerIfCardNotLocked() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        View.OnClickListener editListener = mock(View.OnClickListener.class);
        translationCardItem.setEditClickListener(editListener);
        translationCardItem.findViewById(R.id.translation_card_edit).performClick();
        verify(editListener).onClick(any(View.class));
    }

    @Test
    public void shouldNotSetEditClickListenerIfCardLocked() {
        Deck basicDeck = new Deck("Test Deck", "", "1", 1, true, new Language("eng", "Langauge"));
        when(deckService.currentDeck()).thenReturn(basicDeck);
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        View.OnClickListener editListener = mock(View.OnClickListener.class);
        translationCardItem.setEditClickListener(editListener);
        translationCardItem.findViewById(R.id.translation_card_edit).performClick();
        verifyZeroInteractions(editListener);
    }

    private TranslationCardItem createTranslationCardItemWithAudioAndNoTranslatedText() {
        TranslationCardItem translationCardItem = getDefaultTranslationCard();
        Translation translationItem = new Translation("First Translation", false, DEFAULT_AUDIO_FILE, 1, "");
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL);
        return translationCardItem;
    }

}