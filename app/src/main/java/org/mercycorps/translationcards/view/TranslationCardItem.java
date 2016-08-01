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
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.TranslationService;
import org.mercycorps.translationcards.uiHelper.ToastHelper;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by abeltamrat on 7/12/16.
 */
public class TranslationCardItem extends LinearLayout {
    private static final String TAG = "TranslationCardItem";
    private Translation translation;
    private String dictionaryLanguage;
    private boolean cardExpanded = true;
    private boolean greyOutIfNoAudio = true;
    private boolean playAudioOnClick = true;
    private boolean showAudioIcon = true;
    private boolean showDeleteAndEditOptions = false;
    private TranslationService translationService;
    private DeckService deckService;
    private Integer index;
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
    @Inject
    DecoratedMediaManager mediaManager;

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
        MainApplication application = (MainApplication)getContext().getApplicationContext();
        application.getBaseComponent().inject(this);
        translationService=((MainApplication)getContext().getApplicationContext()).getTranslationService();
        deckService=((MainApplication)getContext().getApplicationContext()).getDeckService();
    }
    private void setStateFromAttributes(Context context, AttributeSet attrs){
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TranslationCardItem,
                0, 0);
        try {
            cardExpanded = a.getBoolean(R.styleable.TranslationCardItem_expandedOnStart, true);
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
        updateExpansionIndicator();
        updateCardTopBackground();
        setCardBottomVisibility();
        updateEditDeleteVisibility();
        updateAudioIconVisibility();
        greyOutCardIfNoAudioTranslation();
    }


    public void setTranslation(Translation translation, String language){
        this.translation = translation;
        this.dictionaryLanguage = language;
        initState();
    }

    public void setTranslation(Translation translation, String language, int index){
        this.translation =translation;
        this.dictionaryLanguage=language;
        this.index = index;
        initState();
    }
    public void setEditClickListener(OnClickListener listener){
        if(!deckService.currentDeck().isLocked()){
            this.editButton.setOnClickListener(listener);
        }
    }
    public void setDeleteClickListener(OnClickListener listener){
        if(!deckService.currentDeck().isLocked()) {
            this.deleteButton.setOnClickListener(listener);
        }
    }

    private boolean isCardExpanded(){
        if(index != null){
            cardExpanded = translationService.cardIsExpanded(index);
        }
        return cardExpanded;
    }

    public void setCardExpanded(boolean cardExpanded){
        this.cardExpanded =cardExpanded;
        if(index != null) {
            if (cardExpanded) {
                translationService.expandCard(index);
            }
            else{
                translationService.minimizeCard(index);
            }
        }
    }

    private void updateExpansionIndicator(){
        if(isCardExpanded())
        {
            expansionIndicatorIcon.setBackgroundResource(R.drawable.collapse_arrow);
        }
        else{
            expansionIndicatorIcon.setBackgroundResource(R.drawable.expand_arrow);
        }
    }
    private void updateCardTopBackground(){
        View cardTopBackground = findViewById(R.id.translation_card_parent);
        int rightPadding = cardTopBackground.getPaddingRight();
        int leftPadding = cardTopBackground.getPaddingLeft();
        if (isCardExpanded()) {
            cardTopBackground.setBackgroundResource(R.drawable.card_top_background_expanded);
        } else {
            cardTopBackground.setBackgroundResource(R.drawable.card_top_background);
        }
        cardTopBackground.setPadding(leftPadding, 0, rightPadding, 0);
    }

    private void setCardBottomVisibility(){
        int visibility = isCardExpanded() ?  View.VISIBLE : View.GONE ;
        translationChild.setVisibility(visibility);
    }

    protected void updateEditDeleteVisibility() {
        if(!showDeleteAndEditOptions || deckService.currentDeck().isLocked()){
            translationGrandChild.setVisibility(View.GONE);
        }
    }
    protected void updateAudioIconVisibility() {
        if(!showAudioIcon) {
            audioIconLayout.setVisibility(View.GONE);
        }
    }

    private void updateTranslatedTextView() {
        String translatedText = translation.getTranslatedText();
        translatedTextView.setText(translatedText);
        if (translatedText.isEmpty()) {
            String formattedLanguage = LanguageService.getTitleCaseName(dictionaryLanguage);
            translatedTextView.setHint(String.format("Add %s translation", formattedLanguage));
            translatedTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18f);
            translatedTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.textDisabled));
        }
        else{
            translatedTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f);
            translatedTextView.setTextColor(ContextCompat.getColor(this.getContext(), R.color.primaryTextColor));
        }
    }
    private void setTranslationSourcePhrase() {
        originTranslationTextView.setText(translation.getLabel());
    }

    private void greyOutCardIfNoAudioTranslation() {
        if(greyOutIfNoAudio) {
            LayerDrawable cardTopBackground = (LayerDrawable) translationCardParent.getBackground();
            int backgroundId = isCardExpanded() ? R.id.card_top_background_expanded : R.id.card_top_background;
            GradientDrawable cardTopBackgroundDrawable = (GradientDrawable) cardTopBackground.findDrawableByLayerId(backgroundId);
            if (!translation.isAudioFilePresent()) {
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
        setCardExpanded(!isCardExpanded());
        updateExpansionIndicator();
        updateCardTopBackground();
        setCardBottomVisibility();
        greyOutCardIfNoAudioTranslation();
    }


    @OnClick(R.id.translation_card)
    protected void translationCardClicked() {
        if(playAudioOnClick && progressBar != null) {
            try {
                if(mediaManager.isCurrentlyPlayingSameCard(translation.getFilename())) {
                    mediaManager.stop();
                } else if (mediaManager.isPlaying()) {
                    mediaManager.stop();
                    mediaManager.play(translation.getFilename(), progressBar, translation.getIsAsset());
                } else {
                    mediaManager.play(translation.getFilename(), progressBar, translation.getIsAsset());
                }
            } catch (AudioFileException e) {
                String message = String.format(this.getContext().getString(R.string.could_not_play_audio_message),
                        dictionaryLanguage);
                ToastHelper.showToast(this.getContext(), message);
                Log.d(TAG, e.getMessage());
            }
        }
    }
    public void setShowDeleteAndEditOptions(boolean showDeleteAndEditOptions){
        this.showDeleteAndEditOptions=showDeleteAndEditOptions;
    }
    public void setGreyOutIfNoAudio(boolean greyOutIfNoAudio){
        this.greyOutIfNoAudio=greyOutIfNoAudio;
    }
    public Translation getTranslation(){
        return translation;
    }
}
