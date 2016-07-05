package org.mercycorps.translationcards.activity.translations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.media.CardAudioClickListener;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
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
        if (translationService.cardIsExpanded(position)) {
            translationChild.setVisibility(View.VISIBLE);
            indicatorIcon.setBackgroundResource(R.drawable.collapse_arrow);
        } else {
            translationChild.setVisibility(View.GONE);
            indicatorIcon.setBackgroundResource(R.drawable.expand_arrow);
        }

        translationItemView.setOnClickListener(null);

        translationItemView.findViewById(R.id.translation_indicator_layout)
                .setOnClickListener(new CardIndicatorClickListener(translationItemView, position, translationService));

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

        String currentDictionaryLabel = dictionaryService.currentDictionary().getLanguage();

        ProgressBar progressBar = (ProgressBar) translationItemView.findViewById(
                R.id.list_item_progress_bar);

        setCardTextView(item, translationItemView, currentDictionaryLabel, progressBar);
        setTranslationCardView(item, translationItemView);
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

    private void setTranslationCardView(Translation item, View convertView) {
        LinearLayout translationCardParent = (LinearLayout) convertView.findViewById(R.id.translation_card_parent);

        ImageView audioIcon = (ImageView) convertView.findViewById(R.id.audio_icon);
        Drawable bg=audioIcon.getDrawable();

        View v = convertView.findViewById(R.id.translation_card_parent);
        LayerDrawable bgDrawable = (LayerDrawable)v.getBackground();
        final GradientDrawable shape = (GradientDrawable)   bgDrawable.findDrawableByLayerId(R.id.card_top_background);

        if(!item.isAudioFilePresent()){
            shape.setAlpha(DISABLED_OPACITY);
            bg.setAlpha(DISABLED_BITMAP_OPACITY);
        }
        else{
            shape.setAlpha(DEFAULT_OPACITY);
            bg.setAlpha(DEFAULT_BITMAP_OPACITY);
        }
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
