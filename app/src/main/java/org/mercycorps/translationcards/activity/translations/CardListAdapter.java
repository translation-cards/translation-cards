package org.mercycorps.translationcards.activity.translations;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.CardAudioClickListener;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.TranslationService;

import java.util.List;

/**
 * Created by agarrard on 5/18/16.
 */
class CardListAdapter extends ArrayAdapter<Translation> {

    private TranslationsActivity translationsActivity;
    private TranslationService translationService;
    private DictionaryService dictionaryService;

    public CardListAdapter(TranslationsActivity translationsActivity,
                           Context context, int resource, int textViewResourceId,
                           List<Translation> objects, TranslationService translationService, DictionaryService dictionaryService) {
        super(context, resource, textViewResourceId, objects);
        this.translationsActivity = translationsActivity;
        this.translationService = translationService;
        this.dictionaryService = dictionaryService;
    }

    @Override
    public View getView(int position, View translationItemView, ViewGroup parent) {
        if (translationItemView == null) {
            translationItemView = inflateTranslationItemView(parent);
        }

        if (translationsActivity.translationCardStates.get(position)) {
            translationItemView.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);
            translationItemView.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.collapse_arrow);
        } else {
            translationItemView.findViewById(R.id.translation_child).setVisibility(View.GONE);
            translationItemView.findViewById(R.id.indicator_icon).setBackgroundResource(
                    R.drawable.expand_arrow);
        }

        translationItemView.setOnClickListener(null);

        translationItemView.findViewById(R.id.translation_indicator_layout)
                .setOnClickListener(new CardIndicatorClickListener(translationsActivity, translationItemView, position));

        View editView = translationItemView.findViewById(R.id.translation_card_edit);
        View deleteView = translationItemView.findViewById(R.id.translation_card_delete);
        if (translationsActivity.deck.isLocked()) {
            editView.setVisibility(View.GONE);
            deleteView.setVisibility(View.GONE);
        } else {
            editView.setOnClickListener(new CardEditClickListener(translationsActivity, getItem(position), dictionaryService));
            deleteView.setOnClickListener(new CardDeleteClickListener(translationsActivity, getItem(position), translationService, dictionaryService));
        }

        String currentDictionaryLabel = dictionaryService.currentDictionary().getLabel();

        ProgressBar progressBar = (ProgressBar) translationItemView.findViewById(
                R.id.list_item_progress_bar);

        setCardTextView(position, translationItemView, currentDictionaryLabel, progressBar);

        setTranslatedTextView(position, translationItemView, currentDictionaryLabel);

        translationItemView.findViewById(R.id.translated_text_layout)
                .setOnClickListener(new CardAudioClickListener(getItem(position), progressBar,
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

    private void setCardTextView(int position, View convertView, String currentDictionaryLabel,
                                 ProgressBar progressBar) {
        TextView cardTextView = (TextView) convertView.findViewById(
                R.id.origin_translation_text);
        cardTextView.setText(getItem(position).getLabel());
        int cardTextColor = getItem(position).isAudioFilePresent() ? R.color.primaryTextColor : R.color.textDisabled;
        cardTextView.setTextColor(ContextCompat.getColor(translationsActivity, cardTextColor));
        cardTextView.setOnClickListener(new CardAudioClickListener(getItem(position), progressBar,
                translationsActivity.decoratedMediaManager, currentDictionaryLabel));
    }

    private void setTranslatedTextView(int position, View convertView, String currentDictionaryLabel) {
        TextView translatedText = (TextView) convertView.findViewById(R.id.translated_text);
        if (getItem(position).getTranslatedText().isEmpty()) {
            translatedText.setText(String.format(translationsActivity.getString(R.string.translated_text_hint), currentDictionaryLabel));
            translatedText.setTextColor(ContextCompat.getColor(getContext(),
                    R.color.textDisabled));
            translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
        } else {
            translatedText.setText(getItem(position).getTranslatedText());
            translatedText.setTextColor(ContextCompat.getColor(getContext(),
                    R.color.primaryTextColor));
            translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        }
    }
}
