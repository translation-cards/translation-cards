package org.mercycorps.translationcards.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.porting.ImportException;
import org.mercycorps.translationcards.porting.TxcPortingUtility;

import java.io.File;

public class ImportActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_EXTERNAL_WRITE = 1;
    private TxcPortingUtility portingUtility;
    private Uri source;
    private BroadcastReceiver onComplete;
    private AlertDialog downloadDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        portingUtility = new TxcPortingUtility();
        source = getIntent().getData();

        onComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                downloadDialog.hide();
                confirmAndLoadData();
                unregisterReceiver(onComplete);
            }
        };
        registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        if (sourceIsURL()) {
            downloadFile(source);
        } else {
            confirmAndLoadData();
        }
    }

    private void downloadFile(Uri source) {
        String[] parsedURL = source.toString().split("/");
        String filename = parsedURL[parsedURL.length - 1];
        showDownloadAlertDialog(filename);

        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + filename;
        File downloadedDeck = new File(path);
        this.source = Uri.fromFile(downloadedDeck);
    }

    private void showDownloadAlertDialog(String filename) {
        downloadDialog = new AlertDialog.Builder(ImportActivity.this)
                .setTitle(R.string.file_download_title)
                .setMessage(filename)
                .show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_REQUEST_EXTERNAL_WRITE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            importDeck();
        }
    }

    private boolean sourceIsURL() {
        return source.toString().contains("http");
    }

    private void confirmAndLoadData() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                importDeck();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_WRITE);
            }
        } else {
            importDeck();
        }
    }

    private void importDeck() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.import_confirm_alert_title)
                .setMessage(getString(R.string.import_confirm_alert_message))
                .setPositiveButton(R.string.import_confirm_alert_positive,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                attemptImport();
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

    private void attemptImport() {
        try {
            TxcPortingUtility.ImportInfo importInfo =
                    portingUtility.prepareImport(ImportActivity.this, source);
            // Check if it's a deck we've already imported.
            if (false && portingUtility.isExistingDeck(this, importInfo)) {
                portingUtility.abortImport(this, importInfo);
                alertUserOfFailure(getString(R.string.import_failure_existing_deck));
                return;
            }
            // Check if it's a different version of a deck we've already imported.
            if (importInfo.externalId != null && !importInfo.externalId.isEmpty()) {
                long otherVersion = portingUtility.otherVersionExists(this, importInfo);
                if (otherVersion != -1) {
                    handleVersionOverride(importInfo, otherVersion);
                    return;
                }
            }
            portingUtility.executeImport(this, importInfo);
        } catch (ImportException e) {
            handleError(e);
            return;
        }
        goToMainScreen();
    }

    private void handleVersionOverride(
            final TxcPortingUtility.ImportInfo importInfo, final long otherVersion) {
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
                                    portingUtility.executeImport(ImportActivity.this, importInfo);
                                } catch (ImportException e) {
                                    handleError(e);
                                    return;
                                }
                                goToMainScreen();
                                break;
                            case 2:
                                try {
                                    portingUtility.executeImport(ImportActivity.this, importInfo);
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
