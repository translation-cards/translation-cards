package org.mercycorps.translationcards.activity.translations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.media.CardAudioClickListener;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.TranslationService;

import java.util.List;

public class CardListAdapter extends ArrayAdapter<Translation> {

    public static final int DISABLED_OPACITY = 235;
    public static final int DEFAULT_OPACITY = 255;
    public static final int DISABLED_BITMAP_OPACITY = 100;
    public static final int DEFAULT_BITMAP_OPACITY = 255;
    private TranslationsActivity translationsActivity;
    private TranslationService translationService;
    private DictionaryService dictionaryService;
    private DeckService deckService;

    public CardListAdapter(TranslationsActivity translationsActivity,
                           Context context, int resource, int textViewResourceId,
                           List<Translation> objects, TranslationService translationService, DictionaryService dictionaryService, DeckService deckService) {
        super(context, resource, textViewResourceId, objects);
        this.translationsActivity = translationsActivity;
        this.translationService = translationService;
        this.dictionaryService = dictionaryService;
        this.deckService = deckService;
    }

    @Override
    public View getView(int position, View translationItemView, ViewGroup parent) {
        Translation item = getItem(position);
        if (translationItemView == null) {
            translationItemView = inflateTranslationItemView(parent);
        }

        View translationChild = translationItemView.findViewById(R.id.translation_child);
        View indicatorIcon = translationItemView.findViewById(R.id.indicator_icon);
        boolean cardIsExpanded = translationService.cardIsExpanded(position);
        if (cardIsExpanded) {
            translationChild.setVisibility(View.VISIBLE);
            indicatorIcon.setBackgroundResource(R.drawable.collapse_arrow);
        } else {
            translationChild.setVisibility(View.GONE);
            indicatorIcon.setBackgroundResource(R.drawable.expand_arrow);
        }

        translationItemView.setOnClickListener(null);

        translationItemView.findViewById(R.id.translation_indicator_layout)
                .setOnClickListener(new CardIndicatorClickListener(translationItemView, position,
                        translationService, item));

        View editView = translationItemView.findViewById(R.id.translation_card_edit);
        View deleteView = translationItemView.findViewById(R.id.translation_card_delete);
        if (deckService.currentDeck().isLocked()) {
            editView.setVisibility(View.GONE);
            deleteView.setVisibility(View.GONE);
        } else {
            Intent intent = new Intent(translationsActivity, EnterSourcePhraseActivity.class);
            editView.setOnClickListener(new CardEditClickListener(translationsActivity, item, intent, dictionaryService, deckService));
            deleteView.setOnClickListener(new CardDeleteClickListener(translationsActivity, item, translationService, new AlertDialog.Builder(translationsActivity)));
        }

        String currentDictionaryLabel = LanguageService.getTitleCaseName(dictionaryService.currentDictionary().getLanguage());

        ProgressBar progressBar = (ProgressBar) translationItemView.findViewById(
                R.id.list_item_progress_bar);

        setCardTextView(item, translationItemView, currentDictionaryLabel, progressBar);
        setTranslationCardView(item, translationItemView, cardIsExpanded, position);
        setTranslatedTextView(item, translationItemView, currentDictionaryLabel);

        translationItemView.findViewById(R.id.translated_text_layout)
                .setOnClickListener(new CardAudioClickListener(item, progressBar,
                        translationsActivity.decoratedMediaManager, currentDictionaryLabel));

        return translationItemView;
    }

    @NonNull
    private View inflateTranslationItemView(ViewGroup parent) {
        View translationItemView = translationsActivity.getLayoutInflater().inflate(R.layout.translation_item,
                parent, false);
        translationItemView.findViewById(R.id.indicator_icon).setBackgroundResource(
                R.drawable.expand_arrow);
        return translationItemView;
    }

    private void setCardTextView(Translation item, View convertView, String currentDictionaryLabel,
                                 ProgressBar progressBar) {
        TextView originTranslatedText = (TextView) convertView.findViewById(
                R.id.origin_translation_text);
        TextView translatedText = (TextView) convertView.findViewById(
                R.id.translated_text);
        originTranslatedText.setText(item.getLabel());
        int cardTextColor = item.isAudioFilePresent() ? R.color.primaryTextColor : R.color.textDisabled;
        originTranslatedText.setTextColor(ContextCompat.getColor(translationsActivity, cardTextColor));
        originTranslatedText.setOnClickListener(new CardAudioClickListener(item, progressBar,
                translationsActivity.decoratedMediaManager, currentDictionaryLabel));
    }

    protected void setTranslationCardView(Translation item, View convertView, Boolean isCardExpanded, int position) {
        ImageView audioIcon = (ImageView) convertView.findViewById(R.id.audio_icon);
        View translationParent = convertView.findViewById(R.id.translation_card_parent);
        initializeTranslationViewExpansion(convertView, isCardExpanded, position, translationParent);

        LayerDrawable cardLabelBackground = (LayerDrawable)translationParent.getBackground();
        int cardLabelBackgroundId = isCardExpanded ? R.id.card_top_background_expanded : R.id.card_top_background;
        GradientDrawable cardLabelBackgroundDrawable = (GradientDrawable)   cardLabelBackground.findDrawableByLayerId(cardLabelBackgroundId);

        if(!item.isAudioFilePresent()){
            cardLabelBackgroundDrawable.setAlpha(DISABLED_OPACITY);
            audioIcon.setBackgroundResource(R.drawable.no_audio);
        }
        else{
            cardLabelBackgroundDrawable.setAlpha(DEFAULT_OPACITY);
            audioIcon.setBackgroundResource(R.drawable.audio);
        }
    }

    private void initializeTranslationViewExpansion(View convertView, Boolean isCardExpanded, int position, View translationParent) {
        View translationChild=convertView.findViewById(R.id.translation_child);
        int rightPadding= translationParent.getPaddingRight();
        int leftPadding= translationParent.getPaddingLeft();
        if(isCardExpanded){
            translationChild.setVisibility(View.VISIBLE);
            convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.collapse_arrow);
            translationService.expandCard(position);
            translationParent.setBackgroundResource(R.drawable.card_top_background_expanded);
        }
        else{
            translationChild.setVisibility(View.GONE);
            convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.expand_arrow);
            translationService.minimizeCard(position);
            translationParent.setBackgroundResource(R.drawable.card_top_background);
        }

        translationParent.setPadding(leftPadding, 0, rightPadding, 0);
    }

    private void setTranslatedTextView(Translation item, View convertView, String currentDictionaryLabel) {
        TextView translatedText = (TextView) convertView.findViewById(R.id.translated_text);
        if (item.getTranslatedText().isEmpty()) {
            translatedText.setText(String.format(translationsActivity.getString(R.string.translated_text_hint), currentDictionaryLabel));
            translatedText.setTextColor(ContextCompat.getColor(getContext(),
                    R.color.textDisabled));
            translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        } else {
            translatedText.setText(item.getTranslatedText());
            translatedText.setTextColor(ContextCompat.getColor(translationsActivity, R.color.primaryTextColor));
            translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        }
    }

    public void update() {
        clear();
        addAll(translationService.getCurrentTranslations());
        notifyDataSetChanged();
    }
}
