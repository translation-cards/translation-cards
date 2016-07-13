package org.mercycorps.translationcards.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    }

    @Test
    public void shouldSetTranslationSourceTextWhenLoaded(){
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, "English");

        assertEquals("First Translation", originTranslationTextView.getText());
    }
    @Test
    public void shouldSetDestinationTranslationTextWhenLoaded(){
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, "English");

        assertEquals("Translated Text", destinationTranslationTextView.getText());
    }
    @Test
    public void shouldHaveCollapseIconVisibleWhenLoaded(){
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, "English");

        assertThat(shadowOf(expansionIndicatorIcon.getBackground()).getCreatedFromResId(), is(R.drawable.collapse_arrow));

    }

    @Test
    public void shouldHaveExpandIconVisibleWhenLoadedAndCollapsed(){
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, "English");

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
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, "English");
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

        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, "English");
        View translationCardChild = translationCardItem.findViewById(R.id.translation_child);
        assertEquals(View.VISIBLE, translationCardChild.getVisibility());
    }
    @Test
    public void shouldExpandTranslationCardWhenCardIndicatorIsClickedTwice() {
        Translation translationItem=new Translation("First Translation",false,null,1,"Translated Text");
        translationCardItem.setTranslation(translationItem, "English");
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        translationCardItem.findViewById(R.id.translation_indicator_layout).performClick();
        assertEquals(View.VISIBLE, translationCardItem.findViewById(R.id.translation_child).getVisibility());
    }
}