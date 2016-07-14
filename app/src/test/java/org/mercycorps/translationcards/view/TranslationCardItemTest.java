package org.mercycorps.translationcards.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Translation;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by abeltamrat on 7/12/16.
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationCardItemTest {
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
        translationCardItem.setTranslation(translationItem, "English");
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
    public void shouldExpandTranslationCardWhenCardIndicatorIsClickedTwice() {
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertEquals(View.VISIBLE, translationCardItem.findViewById(R.id.translation_child).getVisibility());
    }


    @Test
    @TargetApi(19)
    public void shouldGreyOutTranslationCardWhenItContainsNoAudio() {
        LinearLayout translationCardParent = (LinearLayout) translationCardItem.findViewById(R.id.translation_card_parent);
        LayerDrawable bgDrawable= (LayerDrawable)translationCardParent.getBackground();
        GradientDrawable background = (GradientDrawable)bgDrawable.findDrawableByLayerId(R.id.card_top_background_expanded);
        assertEquals(TranslationCardItem.DISABLED_OPACITY, background.getAlpha());
    }
}