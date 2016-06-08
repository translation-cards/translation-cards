package org.mercycorps.translationcards.porting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.MyDecksActivity;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Translation;

import java.io.File;

/**
 * Created by njimenez on 4/26/16.
 */
public class ExportTask extends AsyncTask<Void, Void, Boolean> {

    private final String exportedDeckName;
    private File targetFile;
    private ProgressDialog loadingDialog;
    private Context context = org.mercycorps.translationcards.MainApplication.getContextFromMainApp();
    private Deck deck;
    private Activity activity;

    public ExportTask(String exportedDeckName, Deck deck, Activity activity) {
        this.exportedDeckName = exportedDeckName;
        this.deck = deck;
        this.activity = activity;
    }

    protected void onPreExecute() {
        loadingDialog = ProgressDialog.show(activity, context.getString(R.string.export_progress_dialog_title),
                context.getString(R.string.export_progress_dialog_message), true);
    }

    protected Boolean doInBackground(Void... params) {

        targetFile = new File(context.getExternalCacheDir(), context.getString(R.string.export_filename));
        if (targetFile.exists()) {
            targetFile.delete();
        }
        TxcPortingUtility portingUtility = new TxcPortingUtility();
        try {
            portingUtility.exportData(deck, exportedDeckName, deck.getDictionaries(), targetFile);
        } catch (final ExportException e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    alertUserOfExportFailure(e);
                }
            });
            return false;
        }
        return true;
    }

    protected void onPostExecute(Boolean result) {
        loadingDialog.cancel();
        if (result) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(targetFile));
            activity.startActivity(intent);
        }
    }

    private void alertUserOfExportFailure(ExportException error) {
        String errorMessage = context.getString(R.string.import_failure_default_error_message);
        if (error.getProblem() == ExportException.ExportProblem.TARGET_FILE_NOT_FOUND) {
            errorMessage = context.getString(R.string.export_failure_target_file_not_found_error_message);
        } else if (error.getProblem() == ExportException.ExportProblem.WRITE_ERROR) {
            errorMessage = context.getString(R.string.export_failure_write_error_error_message);
        } else if (error.getProblem() ==
                ExportException.ExportProblem.TOO_MANY_DUPLICATE_FILENAMES) {
            errorMessage = context.getString(
                    R.string.export_failure_too_many_duplicate_filenames_error_message);
        }
        new AlertDialog.Builder(activity)
                .setTitle(R.string.import_failure_alert_title)
                .setMessage(errorMessage)
                .show();
    }
}
