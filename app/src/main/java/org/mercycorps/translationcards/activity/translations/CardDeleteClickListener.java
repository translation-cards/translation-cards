package org.mercycorps.translationcards.activity.translations;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.TranslationService;

public class CardDeleteClickListener implements View.OnClickListener {

    private Activity translationsActivity;
    private Translation translation;
    private TranslationService translationService;
    private AlertDialog.Builder alertDialogBuilder;

    public CardDeleteClickListener(Activity translationsActivity, Translation translation, TranslationService translationService, AlertDialog.Builder alertDialogBuilder) {
        this.translationsActivity = translationsActivity;
        this.translation = translation;
        this.translationService = translationService;
        this.alertDialogBuilder = alertDialogBuilder;
    }

    @Override
    public void onClick(View view) {
        alertDialogBuilder.setTitle(R.string.delete_dialog_title);
        alertDialogBuilder.setMessage(R.string.delete_dialog_message);
        alertDialogBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                deleteTranslation();
            }
        });
        alertDialogBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialogBuilder.show();
    }

    public void deleteTranslation() {
        translationService.deleteTranslation(translation.getLabel());
        //how can i trigger this in a built in event method?
        ((TranslationsActivity)translationsActivity).updateView();
    }
}
