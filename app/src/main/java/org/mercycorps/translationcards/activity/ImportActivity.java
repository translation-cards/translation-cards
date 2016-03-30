package org.mercycorps.translationcards.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.porting.ImportException;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.porting.TxcPortingUtility;

public class ImportActivity extends AppCompatActivity {

    private TxcPortingUtility portingUtility;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        portingUtility = new TxcPortingUtility();
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
                                attemptImport(source);
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

    private void attemptImport(Uri source) {
        try {
            TxcPortingUtility.ImportSpec importSpec =
                    portingUtility.prepareImport(ImportActivity.this, source);
            // Check if it's a deck we've already imported.
            if (false && portingUtility.isExistingDeck(this, importSpec)) {
                portingUtility.abortImport(this, importSpec);
                alertUserOfFailure(getString(R.string.import_failure_existing_deck));
                return;
            }
            // Check if it's a different version of a deck we've already imported.
            if (importSpec.externalId != null && !importSpec.externalId.isEmpty()) {
                long otherVersion = portingUtility.otherVersionExists(this, importSpec);
                if (otherVersion != -1) {
                    handleVersionOverride(importSpec, otherVersion);
                    return;
                }
            }
            portingUtility.executeImport(this, importSpec);
        } catch (ImportException e) {
            handleError(e);
            return;
        }
        goToMainScreen();
    }

    private void handleVersionOverride(
            final TxcPortingUtility.ImportSpec importSpec, final long otherVersion) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.import_version_override_title)
                .setItems(R.array.version_override_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                finish();
                                break;
                            case 1:
                                try {
                                    portingUtility.executeImport(ImportActivity.this, importSpec);
                                } catch (ImportException e) {
                                    handleError(e);
                                    return;
                                }
                                goToMainScreen();
                                break;
                            case 2:
                                try {
                                    portingUtility.executeImport(ImportActivity.this, importSpec);
                                } catch (ImportException e) {
                                    handleError(e);
                                    return;
                                }
                                DbManager dbm = new DbManager(ImportActivity.this);
                                dbm.deleteDeck(otherVersion);
                                goToMainScreen();
                                break;
                        }
                    }
                });
        builder.show();
    }

    private void handleError(ImportException e) {
        String errorMessage = getString(R.string.import_failure_default_error_message);
        if (e.getProblem() == ImportException.ImportProblem.FILE_NOT_FOUND) {
            errorMessage = getString(R.string.import_failure_file_not_found_error_message);
        } else if (e.getProblem() == ImportException.ImportProblem.NO_INDEX_FILE) {
            errorMessage = getString(R.string.import_failure_no_index_file_error_message);
        } else if (e.getProblem() == ImportException.ImportProblem.INVALID_INDEX_FILE) {
            errorMessage = getString(R.string.import_failure_invalid_index_file_error_message);
        } else if (e.getProblem() == ImportException.ImportProblem.READ_ERROR) {
            errorMessage = getString(R.string.import_failure_read_error_error_message);
        }
        alertUserOfFailure(errorMessage);
    }

    private void alertUserOfFailure(String errorMessage) {
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

    private void goToMainScreen() {
        Intent intent = new Intent(this, DecksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
