package org.mercycorps.translationcards.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.LanguageService;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abeltamrat on 7/12/16.
 */
public class TranslationCardItem extends LinearLayout {
    private Translation translationItem;
    private String dictionaryLanguage;
    private boolean isCardExpanded = true;

    @Bind(R.id.origin_translation_text)
    TextView originTranslationTextView;
    @Bind(R.id.translated_text)
    TextView translatedTextView;
    @Bind(R.id.indicator_icon)
    ImageView expansionIndicatorIcon;
    @Bind(R.id.translation_child)
    LinearLayout translationChild;
    @Bind(R.id.translation_grandchild)
    LinearLayout translationGrandChild;
    @Bind(R.id.audio_icon_layout)
    FrameLayout audioIconLayout;

    public void setTranslation(Translation translation, String language){
        this.translationItem=translation;
        this.dictionaryLanguage=language;
        init();
        initState();
    }
    public TranslationCardItem(Context context) {
        super(context);
    }

    public TranslationCardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TranslationCardItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.translation_card_item, this);
        ButterKnife.bind(this);
    }

    public void initState(){
        setTranslationSourcePhrase();
        updateTranslatedTextView();

        setExpansionIndicator();
        setCardTopBackground();
        setCardBottomVisibility();
        hideGrandchildAndAudioIcon();
    }
    private void setExpansionIndicator(){
        if(isCardExpanded)
        {
            expansionIndicatorIcon.setBackgroundResource(R.drawable.collapse_arrow);
        }
        else{
            expansionIndicatorIcon.setBackgroundResource(R.drawable.expand_arrow);
        }
    }
    private void setCardTopBackground(){
        View cardTopBackground = findViewById(R.id.translation_card_parent);
        int rightPadding = cardTopBackground.getPaddingRight();
        int leftPadding = cardTopBackground.getPaddingLeft();
        if (isCardExpanded) {
            cardTopBackground.setBackgroundResource(R.drawable.card_top_background_expanded);
        } else {
            cardTopBackground.setBackgroundResource(R.drawable.card_top_background);
        }
        cardTopBackground.setPadding(leftPadding, 0, rightPadding, 0);
    }

    private void setCardBottomVisibility(){
        int visibility = isCardExpanded ?  View.VISIBLE : View.GONE ;
        translationChild.setVisibility(visibility);
    }

    protected void hideGrandchildAndAudioIcon() {
        translationGrandChild.setVisibility(View.GONE);
        audioIconLayout.setVisibility(View.GONE);
    }

    private void updateTranslatedTextView() {
        String translatedText = translationItem.getTranslatedText();
        translatedTextView.setText(translatedText);
        if (translatedText.isEmpty()) {
            String formattedLanguage = LanguageService.getTitleCaseName(dictionaryLanguage);
            translatedTextView.setHint(String.format("Add %s translation", formattedLanguage));
        }
    }
    private void setTranslationSourcePhrase() {
        originTranslationTextView.setText(translationItem.getLabel());
    }

    @OnClick(R.id.translation_indicator_layout)
    protected void toggleCardExpansion(){
        isCardExpanded=!isCardExpanded;
        setExpansionIndicator();
        setCardTopBackground();
        setCardBottomVisibility();
    }
}
