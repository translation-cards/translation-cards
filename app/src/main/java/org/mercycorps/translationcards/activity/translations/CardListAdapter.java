package org.mercycorps.translationcards.activity.translations;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.service.TranslationService;
import org.mercycorps.translationcards.view.TranslationCardItem;

import java.util.ArrayList;
import java.util.List;

public class CardListAdapter extends ArrayAdapter<Translation> {

    private TranslationService translationService;
    private DictionaryService dictionaryService;
    private DeckService deckService;
    private Context context;

    public CardListAdapter(Context context, int resource, int textViewResourceId,
                           List<Translation> objects, TranslationService translationService,
                           DictionaryService dictionaryService, DeckService deckService) {
        super(context, resource, textViewResourceId, objects);
        this.translationService = translationService;
        this.dictionaryService = dictionaryService;
        this.deckService = deckService;
        this.context = context;
    }

    @Override
    public View getView(int position, View translationItemView, ViewGroup parent) {
        Translation item = getItem(position);
        TranslationCardItem cardItem;
        if (translationItemView == null) {
            cardItem = new TranslationCardItem(context);
            cardItem.setShowDeleteAndEditOptions(true);
            cardItem.setGreyOutIfNoAudio(true);
        } else {
            cardItem = (TranslationCardItem) translationItemView;
        }
        String currentDictionaryLabel = LanguageService.getTitleCaseName(dictionaryService.currentDictionary().getLanguage());
        cardItem.setTranslation(item, currentDictionaryLabel, position);
        if (!deckService.currentDeck().isLocked()) {
            cardItem.setEditClickListener(getOnEditListener(item));
            cardItem.setDeleteClickListener(getOnDeleteListener(item));
        }
        return cardItem;
    }

    private void deleteTranslation(Translation translation){
        translationService.deleteTranslation(translation.getLabel());
        this.update();
    }

    private View.OnClickListener getOnDeleteListener(final Translation translation){
        final AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(context);
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogBuilder.setTitle(R.string.delete_dialog_title);
                dialogBuilder.setMessage(R.string.delete_dialog_message);
                dialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTranslation(translation);
                    }
                });
                dialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                dialogBuilder.show();
            }
        };
    }

    private View.OnClickListener getOnEditListener(final Translation translation){

        return new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EnterSourcePhraseActivity.class);
                intent.putExtra(AddTranslationActivity.CONTEXT_INTENT_KEY, createAddNewTranslationContext(translation));
                intent.putExtra(DeckService.INTENT_KEY_DECK, deckService.currentDeck());
                context.startActivity(intent);
            }

            private AddNewTranslationContext createAddNewTranslationContext(Translation translation) {
                List<NewTranslation> newTranslations = new ArrayList<>();
                for (Dictionary dictionary : dictionaryService.getDictionariesForCurrentDeck()) {
                    newTranslations.add(new NewTranslation(dictionary, translation, TranslationsActivity.IS_EDIT));
                }

                return new AddNewTranslationContext(newTranslations, TranslationsActivity.IS_EDIT);
            }
        };
    }

    public void update() {
        clear();
        addAll(translationService.getCurrentTranslations());
        notifyDataSetChanged();
    }
}
