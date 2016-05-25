package org.mercycorps.translationcards.activity.translations;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;

class CardDeleteClickListener implements View.OnClickListener {

    private TranslationsActivity translationsActivity;
    Translation translation;

    public CardDeleteClickListener(TranslationsActivity translationsActivity, Translation translation) {
        this.translationsActivity = translationsActivity;
        this.translation = translation;
    }

    @Override
    public void onClick(View view) {
        new AlertDialog.Builder(translationsActivity)
                .setTitle(R.string.delete_dialog_title)
                .setMessage(R.string.delete_dialog_message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for (Dictionary dictionary : translationsActivity.dictionaries) {
                            Translation translationBySourcePhrase = dictionary.getTranslationBySourcePhrase(translation.getLabel());
                            translationsActivity.dbManager.deleteTranslation(translationBySourcePhrase.getDbId());
                        }
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
