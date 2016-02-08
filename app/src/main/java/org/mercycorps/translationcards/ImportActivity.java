package org.mercycorps.translationcards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ImportActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        confirmAndLoadData(getIntent().getData());
    }

    private void confirmAndLoadData(final Uri source) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.import_confirm_alert_title)
                .setMessage(getString(R.string.import_confirm_alert_message))
                .setPositiveButton(R.string.import_confirm_alert_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TxcPortingUtility portingUtility = new TxcPortingUtility();
                                try {
                                    TxcPortingUtility.ImportInfo importInfo =
                                            portingUtility.prepareImport(
                                                    ImportActivity.this, source);
                                    portingUtility.executeImport(ImportActivity.this, importInfo);
                                } catch (ImportException e) {
                                    alertUserOfFailure(e);
                                    return;
                                }
                                Intent intent = new Intent(
                                        ImportActivity.this, DecksActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        })
                .setNegativeButton(R.string.import_confirm_alert_negative,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ImportActivity.this.finish();
                            }
                        })
                .show();
    }

    private void alertUserOfFailure(ImportException error) {
        String errorMessage = getString(R.string.import_failure_default_error_message);
        if (error.getProblem() == ImportException.ImportProblem.FILE_NOT_FOUND) {
            errorMessage = getString(R.string.import_failure_file_not_found_error_message);
        } else if (error.getProblem() == ImportException.ImportProblem.NO_INDEX_FILE) {
            errorMessage = getString(R.string.import_failure_no_index_file_error_message);
        } else if (error.getProblem() == ImportException.ImportProblem.INVALID_INDEX_FILE) {
            errorMessage = getString(R.string.import_failure_invalid_index_file_error_message);
        } else if (error.getProblem() == ImportException.ImportProblem.READ_ERROR) {
            errorMessage = getString(R.string.import_failure_read_error_error_message);
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.import_failure_alert_title)
                .setMessage(errorMessage)
                .setNeutralButton(R.string.misc_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImportActivity.this.finish();
                    }
                })
                .show();
    }
}
