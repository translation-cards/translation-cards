package org.mercycorps.translationcards.activity.translations;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.service.TranslationService;

class CardDeleteClickListener implements View.OnClickListener {

    private TranslationsActivity translationsActivity;
    private Translation translation;
    private TranslationService translationService;

    public CardDeleteClickListener(TranslationsActivity translationsActivity, Translation translation, TranslationService translationService) {
        this.translationsActivity = translationsActivity;
        this.translation = translation;
        this.translationService = translationService;
    }

    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(translationsActivity)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        translationService.deleteTranslation(translation.getLabel());
                        translationsActivity.dictionaries = translationsActivity.dbManager.getAllDictionariesForDeck(translationsActivity.deck.getDbId());
                        translationsActivity.setDictionary(translationsActivity.currentDictionaryIndex);
                        translationsActivity.listAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
