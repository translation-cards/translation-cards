package org.mercycorps.translationcards.activity.translations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.TranslationService;
import org.mercycorps.translationcards.view.TranslationCardItem;

import java.util.List;

public class CardListAdapter extends ArrayAdapter<Translation> {

    public static final int DISABLED_OPACITY = 235;
    public static final int DEFAULT_OPACITY = 255;
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
        boolean isCardLocked = deckService.currentDeck().isLocked();
        String currentDictionaryLabel = LanguageService.getTitleCaseName(dictionaryService.currentDictionary().getLanguage());
        TranslationCardItem translationCardItemView = (TranslationCardItem)(translationItemView.findViewById(R.id.translation_card_list_item));
        translationCardItemView.setTranslation(item,currentDictionaryLabel, isCardLocked, position);

        if (!isCardLocked) {
            Intent intent = new Intent(translationsActivity, EnterSourcePhraseActivity.class);
            translationCardItemView.setEditAndDeleteClickListeners(translationsActivity, intent);
        }


        //setCardTextView(item, translationItemView, currentDictionaryLabel, progressBar);
        setTranslatedTextView(item, translationCardItemView);

//        translationItemView.findViewById(R.id.translated_text_layout)
//                .setOnClickListener(new CardAudioClickListener(item, progressBar,
//                        translationsActivity.decoratedMediaManager, currentDictionaryLabel));

        return translationItemView;
    }

    @NonNull
    private View inflateTranslationItemView(ViewGroup parent) {
        View translationItemView = translationsActivity.getLayoutInflater().inflate(R.layout.translation_item,
                parent, false);
        return translationItemView;
    }

//    private void setCardTextView(Translation item, View convertView, String currentDictionaryLabel,
//                                 ProgressBar progressBar) {
//        TextView originTranslatedText = (TextView) convertView.findViewById( R.id.origin_translation_text);
//        originTranslatedText.setOnClickListener(new CardAudioClickListener(item, progressBar,
//                translationsActivity.decoratedMediaManager, currentDictionaryLabel));
//    }

    private void setTranslatedTextView(Translation translation, TranslationCardItem translationCard) {
        if (translation.getTranslatedText().isEmpty()) {
            translationCard.setTranslationTextSize(18f);
        } else {
            translationCard.setTranslationTextSize(20f);
        }
    }

    public void update() {
        clear();
        addAll(translationService.getCurrentTranslations());
        notifyDataSetChanged();
    }
}
