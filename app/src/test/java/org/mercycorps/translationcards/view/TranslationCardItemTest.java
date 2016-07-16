package org.mercycorps.translationcards.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.TestMainApplication;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.exception.AudioFileNotSetException;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.TranslationService;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.res.Attribute;
import org.robolectric.shadows.RoboAttributeSet;
import org.robolectric.shadows.ShadowToast;

import java.util.ArrayList;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.getDecoratedMediaManager;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
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
    private TranslationCardItem translationCardItem;
    private TextView originTranslationTextView;
    private TextView destinationTranslationTextView;
    private ImageView expansionIndicatorIcon;
    private Activity activity;
    @Before
    public void setUp() throws Exception {
        activity= Robolectric.buildActivity(Activity.class).create().get();

        translationCardItem = (TranslationCardItem) LayoutInflater.from(activity).inflate(R.layout.translation_card_item, null);
        originTranslationTextView = (TextView)translationCardItem.findViewById(R.id.origin_translation_text);
        destinationTranslationTextView = (TextView)translationCardItem.findViewById(R.id.translated_text);
        expansionIndicatorIcon = (ImageView) translationCardItem.findViewById(R.id.indicator_icon);


        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL);
    }

    @Test
    public void shouldSetTranslationSourceTextWhenLoaded(){
        assertEquals("First Translation", originTranslationTextView.getText());
    }
    @Test
    public void shouldSetDestinationTranslationTextWhenLoaded(){
        assertEquals("Translated Text", destinationTranslationTextView.getText());
    }
    @Test
    public void shouldHaveCollapseIconVisibleWhenLoaded(){
        assertThat(shadowOf(expansionIndicatorIcon.getBackground()).getCreatedFromResId(), is(R.drawable.collapse_arrow));
    }

    @Test
    public void shouldHaveExpandIconVisibleWhenLoadedAndCollapsed(){
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertEquals(R.drawable.expand_arrow, shadowOf(expansionIndicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldCollapseCardWhenIndicatorIconIsClicked() {
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        View translationChild = translationCardItem.findViewById( R.id.translation_child);
        assertEquals(View.GONE, translationChild.getVisibility());
    }
    @Test
    public void shouldShowExpandedCardTopBackgroundWhenExpanded() {
        View translationParent = translationCardItem.findViewById( R.id.translation_card_parent);
        assertEquals(R.drawable.card_top_background_expanded, shadowOf(translationParent.getBackground()).getCreatedFromResId());
    }
    @Test
    public void shouldNotShowExpandedCardTopBackgroundWhenCollapsed() {
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        View translationParent = translationCardItem.findViewById( R.id.translation_card_parent);
        assertEquals(R.drawable.card_top_background, shadowOf(translationParent.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldShowTranslationChildWhenActivityIsCreated() {
        View translationCardChild = translationCardItem.findViewById(R.id.translation_child);
        assertEquals(View.VISIBLE, translationCardChild.getVisibility());
    }
    @Test
    public void shouldShowCardBottomWhenCardIndicatorIsClickedTwice() {
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertEquals(View.VISIBLE, translationCardItem.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowCollapseCardIndicatorWhenIndicatorIsClickedTwice() {
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        ImageView indicatorIcon = (ImageView) translationCardItem.findViewById(R.id.indicator_icon);
        assertEquals(R.drawable.collapse_arrow, shadowOf(indicatorIcon.getBackground()).getCreatedFromResId());
    }

    @Test
    @TargetApi(19)
    public void shouldGreyOutTranslationCardWhenItContainsNoAudio() {
        LinearLayout translationCardParent = (LinearLayout) translationCardItem.findViewById(R.id.translation_card_parent);
        LayerDrawable bgDrawable= (LayerDrawable)translationCardParent.getBackground();
        GradientDrawable background = (GradientDrawable)bgDrawable.findDrawableByLayerId(R.id.card_top_background_expanded);
        assertEquals(TranslationCardItem.DISABLED_OPACITY, background.getAlpha());
    }

    @Test
    public void shouldDisplayMuteIconWhenTranslationContainsNoAudio() {
        View audioIcon= translationCardItem.findViewById(R.id.audio_icon);
        assertThat(shadowOf(audioIcon.getBackground()).getCreatedFromResId(), is(R.drawable.no_audio_40));
    }
    @Test
    public void shouldDisplayAudioIconWhenTranslationContainsAudio() {
        createTranslationCardItemWithAudioOrTranslatedText();
        View audioIcon= translationCardItem.findViewById(R.id.audio_icon);
        assertThat(shadowOf(audioIcon.getBackground()).getCreatedFromResId(), is(R.drawable.audio));
    }

    @Test
    public void shouldBeExpandedWhenExpandAttributeSetToTrue(){
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("org.mercycorps.translationcards:attr/expandedOnStart",
                String.valueOf(true), "org.mercycorps.translationcards"));
        AttributeSet attrs =new RoboAttributeSet(attributes,shadowOf(activity).getResourceLoader() );
        TranslationCardItem tc = new TranslationCardItem(RuntimeEnvironment.application,attrs );
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        tc.setTranslation(translationItem, "English");
        assertEquals(View.VISIBLE, tc.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldShowToastNotificationWhenTranslationCardWithoutAudioFileIsClicked() throws AudioFileException {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(false);
        doThrow(new AudioFileException()).when(getDecoratedMediaManager()).play(anyString(), any(ProgressBar.class), anyBoolean());

        translationCardItem.findViewById(R.id.translation_card).performClick();

        assertEquals(DEFAULT_DICTIONARY_LABEL + " translation not recorded.", ShadowToast.getTextOfLatestToast());
    }

    @Test
    public void shouldBeCollapsedWhenExpandAttributeSetToFalse(){
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("org.mercycorps.translationcards:attr/expandedOnStart",
                String.valueOf(false), "org.mercycorps.translationcards"));
        AttributeSet attrs =new RoboAttributeSet(attributes,shadowOf(activity).getResourceLoader() );
        TranslationCardItem tc = new TranslationCardItem(RuntimeEnvironment.application,attrs );
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        tc.setTranslation(translationItem, "English");
        assertEquals(View.GONE, tc.findViewById(R.id.translation_child).getVisibility());
    }

    @Test
    public void shouldStopPlayingWhenPlayButtonIsClickedTwice() throws AudioFileNotSetException {
        when(getDecoratedMediaManager().isPlaying()).thenReturn(false).thenReturn(true);
        createTranslationCardItemWithAudioOrTranslatedText();
        translationCardItem.findViewById(R.id.translation_card).performClick();
        translationCardItem.findViewById(R.id.translation_card).performClick();
        verify(getDecoratedMediaManager()).stop();
    }

    @Test
    public void shouldGreyOutTranslationSourceTextWhenItContainsNoAudio() {
        TextView translationText = (TextView) translationCardItem.findViewById(R.id.origin_translation_text);
        assertEquals(getColor(activity, R.color.textDisabled), translationText.getCurrentTextColor());
    }

    @Test
    public void shouldPlayAudioFileWhenTranslationCardIsClicked() throws AudioFileException {
        createTranslationCardItemWithAudioOrTranslatedText();
        translationCardItem.findViewById(R.id.translation_card).performClick();
        ProgressBar progressBar = (ProgressBar) translationCardItem.findViewById(R.id.translation_card_progress_bar);
        verify(getDecoratedMediaManager()).play(DEFAULT_AUDIO_FILE, progressBar, IS_NOT_ASSET);
    }


    @Test
    public void shouldShowHintTextWhenNoTranslatedTextPhraseIsProvided() {
        createTranslationCardItemWithAudioOrTranslatedText();
        TextView translatedTextView = (TextView) translationCardItem.findViewById(R.id.translated_text);
        assertEquals(String.format("Add %s translation",  DEFAULT_DICTIONARY_LABEL), translatedTextView.getHint());
    }

    @Test
    public void shouldSetTextViewToEmptyStringWhenNoTranslatedTextPhraseIsProvided() {
        createTranslationCardItemWithAudioOrTranslatedText();

        TextView translatedTextView = (TextView) translationCardItem.findViewById(R.id.translated_text);
        assertEquals("", translatedTextView.getText().toString());
    }

    @Test
    public void shouldHideEditAndDeleteOptionsIfCardIsLocked() {
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("org.mercycorps.translationcards:attr/showEditAndDeleteOptions",
                String.valueOf(true), "org.mercycorps.translationcards"));
        AttributeSet attrs =new RoboAttributeSet(attributes,shadowOf(activity).getResourceLoader() );
        TranslationCardItem tc = new TranslationCardItem(RuntimeEnvironment.application,attrs );
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        tc.setTranslation(translationItem, "English", true, 0);
        assertEquals(View.GONE, tc.findViewById(R.id.translation_grandchild).getVisibility());
    }

    @Test
    public void shouldChangeTextSizeIfNoTranslationPresent() {
        translationCardItem.setTranslationTextSize(18f);

        assertEquals(18f, ((TextView) translationCardItem.findViewById(R.id.translated_text)).getTextSize());
    }

    @Test
    public void shouldShowTranslationCardChildWhenTranslationServiceReturnsExpanded() {
        Translation translationItem=new Translation("First Translation",false,DEFAULT_AUDIO_FILE,1,"");
        TranslationService translationService = ((TestMainApplication)RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(true);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, false, 0);
        View childView=translationCardItem.findViewById(R.id.translation_child);

        assertEquals(View.VISIBLE, childView.getVisibility());
    }

    @Test
    public void shouldHideTranslationCardChildWhenTranslationServiceReturnsNotExpanded() {
        Translation translationItem=new Translation("First Translation",false,DEFAULT_AUDIO_FILE,1,"");
        TranslationService translationService = ((TestMainApplication)RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(false);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, false, 0);
        View childView=translationCardItem.findViewById(R.id.translation_child);

        assertEquals(View.GONE, childView.getVisibility());
    }

    @Test
    public void shouldCallLanguageServiceOnExpansionClick() {
        Translation translationItem=new Translation("First Translation",false,DEFAULT_AUDIO_FILE,1,"");
        TranslationService translationService = ((TestMainApplication)RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(false);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, false, 0);
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();

        verify(translationService).expandCard(0);
    }

    @Test
    public void shouldCallLanguageServiceOnCollapseClick() {
        Translation translationItem=new Translation("First Translation",false,DEFAULT_AUDIO_FILE,1,"");
        TranslationService translationService = ((TestMainApplication)RuntimeEnvironment.application).getTranslationService();
        when(translationService.cardIsExpanded(0)).thenReturn(true);
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL, false, 0);
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        verify(translationService).minimizeCard(0);
    }

    private void createTranslationCardItemWithAudioOrTranslatedText() {
        Translation translationItem=new Translation("First Translation",false,DEFAULT_AUDIO_FILE,1,"");
        translationCardItem.setTranslation(translationItem, DEFAULT_DICTIONARY_LABEL);
    }

}