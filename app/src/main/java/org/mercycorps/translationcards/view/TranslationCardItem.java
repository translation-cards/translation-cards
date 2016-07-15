package org.mercycorps.translationcards.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.exception.AudioFileException;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.TranslationService;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abeltamrat on 7/12/16.
 */
public class TranslationCardItem extends LinearLayout {
    private static final String TAG = "TranslationCardItem";
    private Translation translationItem;
    private String dictionaryLanguage;
    private boolean isCardExpanded;
    private boolean greyOutIfNoAudio;
    private boolean playAudioOnClick;
    private boolean showAudioIcon;
    private boolean showDeleteAndEditOptions;
    private boolean isCardLocked = false;
    private TranslationService translationService;
    private int position;
    public static final int DISABLED_OPACITY = 220;
    public static final int DEFAULT_OPACITY = 255;

    @Bind(R.id.origin_translation_text)
    TextView originTranslationTextView;
    @Bind(R.id.translated_text)
    TextView translatedTextView;
    @Bind(R.id.indicator_icon)
    ImageView expansionIndicatorIcon;
    @Bind(R.id.translation_child)
    LinearLayout translationChild;
    @Bind(R.id.translation_card_parent)
    LinearLayout translationCardParent;
    @Bind(R.id.translation_grandchild)
    LinearLayout translationGrandChild;
    @Bind(R.id.audio_icon_layout)
    FrameLayout audioIconLayout;
    @Bind(R.id.audio_icon)
    ImageView audioIcon;
    @Bind(R.id.translation_card_progress_bar)
    ProgressBar progressBar;
    @Bind(R.id.translation_card_edit)
    View editButton;
    @Bind(R.id.translation_card_delete)
    View deleteButton;

    public TranslationCardItem(Context context) {
        super(context);
        init();
    }

    public TranslationCardItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        setStateFromAttributes(context,attrs);
    }

    public TranslationCardItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        setStateFromAttributes(context,attrs);
    }
    private void init(){
        inflate(getContext(), R.layout.translation_card, this);
        ButterKnife.bind(this);
    }
    private void setStateFromAttributes(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TranslationCardItem,
                0, 0);
        try {
            isCardExpanded = a.getBoolean(R.styleable.TranslationCardItem_expandedOnStart, true);
            greyOutIfNoAudio = a.getBoolean(R.styleable.TranslationCardItem_greyOutCardIfNoAudio, true);
            showAudioIcon = a.getBoolean(R.styleable.TranslationCardItem_showAudioIcon, true);
            playAudioOnClick = a.getBoolean(R.styleable.TranslationCardItem_playAudioOnClick, true);
            showDeleteAndEditOptions = a.getBoolean(R.styleable.TranslationCardItem_showEditAndDeleteOptions, false);
        } finally {
            a.recycle();
        }
    }

    private void initState(){
        setTranslationSourcePhrase();
        updateTranslatedTextView();
        setExpansionIndicator();
        setCardTopBackground();
        setCardBottomVisibility();
        hideGrandchildAndAudioIcon();
        greyOutCardIfNoAudioTranslation();
    }


    public void setTranslation(Translation translation, String language){
        this.translationItem=translation;
        this.dictionaryLanguage=language;
        initState();
    }

    public void setTranslation(Translation translation, String language, boolean isCardLocked, int position){
        this.translationItem=translation;
        this.dictionaryLanguage=language;
        this.isCardLocked = isCardLocked;
        this.position = position;
        initState();
    }

    public void setTranslationService(TranslationService translationService){
        this.translationService=translationService;
    }

    public void setTranslationTextSize(float textSize){
        ((TextView) findViewById(R.id.translated_text)).setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
    }

    public void setEditAndDeleteClickListeners(View.OnClickListener editClickListener, View.OnClickListener deleteClickListener){
        editButton.setOnClickListener(editClickListener);
        deleteButton.setOnClickListener(deleteClickListener);
    }

    private boolean getIsCardExpanded(){
        if(translationService != null){
            isCardExpanded=translationService.cardIsExpanded(position);
        }
        return isCardExpanded;
    }

    private void setExpansionIndicator(){
        if(getIsCardExpanded())
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
        if (getIsCardExpanded()) {
            cardTopBackground.setBackgroundResource(R.drawable.card_top_background_expanded);
        } else {
            cardTopBackground.setBackgroundResource(R.drawable.card_top_background);
        }
        cardTopBackground.setPadding(leftPadding, 0, rightPadding, 0);
    }

    private void setCardBottomVisibility(){
        int visibility = getIsCardExpanded() ?  View.VISIBLE : View.GONE ;
        translationChild.setVisibility(visibility);
    }

    protected void hideGrandchildAndAudioIcon() {
        if(!showAudioIcon) {
            audioIconLayout.setVisibility(View.GONE);
        }
        if(!showDeleteAndEditOptions || isCardLocked){
            translationGrandChild.setVisibility(View.GONE);
        }
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

    private void greyOutCardIfNoAudioTranslation() {
        if(greyOutIfNoAudio) {
            LayerDrawable cardTopBackground = (LayerDrawable) translationCardParent.getBackground();
            int backgroundId = getIsCardExpanded() ? R.id.card_top_background_expanded : R.id.card_top_background;
            GradientDrawable cardTopBackgroundDrawable = (GradientDrawable) cardTopBackground.findDrawableByLayerId(backgroundId);
            if (!translationItem.isAudioFilePresent()) {
                cardTopBackgroundDrawable.setAlpha(DISABLED_OPACITY);
                originTranslationTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.textDisabled));
                audioIcon.setBackgroundResource(R.drawable.no_audio_40);
            } else {
                cardTopBackgroundDrawable.setAlpha(DEFAULT_OPACITY);
                originTranslationTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.primaryTextColor));
                audioIcon.setBackgroundResource(R.drawable.audio);
            }
        }
    }

    @OnClick(R.id.translation_indicator_layout)
    protected void toggleCardExpansion(){
        isCardExpanded=!getIsCardExpanded();
        if(translationService != null) {
            if (isCardExpanded) {
                translationService.expandCard(position);
            }
            else{
                translationService.minimizeCard(position);
            }
        }
        setExpansionIndicator();
        setCardTopBackground();
        setCardBottomVisibility();
        greyOutCardIfNoAudioTranslation();
    }


    @OnClick(R.id.translation_card)
    protected void translationCardClicked() {
        if(playAudioOnClick && progressBar != null) {
            try {
                DecoratedMediaManager mediaManager = ((MainApplication)getContext().getApplicationContext()).getDecoratedMediaManager();
                if(mediaManager.isPlaying()) {
                    mediaManager.stop();
                } else {
                    mediaManager.play(translationItem.getFilename(), progressBar, translationItem.getIsAsset());
                }
            } catch (AudioFileException e) {
                String message = String.format(this.getContext().getString(R.string.could_not_play_audio_message),
                        dictionaryLanguage);
                ToastHelper.showToast(this.getContext(), message);
                Log.d(TAG, e.getMessage());
            }
        }
    }
}
