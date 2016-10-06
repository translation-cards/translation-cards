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

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.myDecks.MyDecksActivity;
import org.mercycorps.translationcards.porting.ImportException;
import org.mercycorps.translationcards.porting.TxcImportUtility;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.repository.DictionaryRepository;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.service.LanguageService;

import java.io.File;

import javax.inject.Inject;

public class ImportActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_EXTERNAL_WRITE = 1;
    private TxcImportUtility portingUtility;
    private Uri source;
    private BroadcastReceiver onDownloadComplete;
    private AlertDialog downloadDialog;

    @Inject LanguageService languageService;
    @Inject TranslationRepository translationRepository;
    @Inject DictionaryRepository dictionaryRepository;
    @Inject DeckRepository deckRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);

        portingUtility = createImportUtility();

        source = getIntent().getData();

        onDownloadComplete = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                downloadDialog.dismiss();
                unregisterReceiver(onDownloadComplete);
                importDeck();
            }
        };
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        requestPermissionsAndLoadData();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_EXTERNAL_WRITE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loadDataAndImport();
                } else {
                    unregisterReceiver(onDownloadComplete);
                    finish();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private TxcImportUtility createImportUtility() {
        return new TxcImportUtility(languageService, deckRepository, translationRepository, dictionaryRepository);
    }

    protected void requestPermissionsAndLoadData() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                loadDataAndImport();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_EXTERNAL_WRITE);
            }
        } else {
            loadDataAndImport();
        }
    }

    private void loadDataAndImport() {
        if (sourceIsURL()) {
            downloadFile();
        } else {
            importDeck();
        }
    }

    private void downloadFile() {
        String filename = getFilenameFromURL();
        showDownloadAlertDialog(filename);

        String uniqueFilename = filename + "." + System.currentTimeMillis();
        DownloadManager.Request request = new DownloadManager.Request(source);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uniqueFilename);

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        downloadManager.enqueue(request);
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + uniqueFilename;
        File downloadedDeck = new File(path);
        this.source = Uri.fromFile(downloadedDeck);
    }

    private String getFilenameFromURL() {
        String[] parsedURL = source.toString().split("/");
        return parsedURL[parsedURL.length - 1];
    }

    private void showDownloadAlertDialog(String filename) {
        downloadDialog = new AlertDialog.Builder(ImportActivity.this)
                .setTitle(R.string.file_download_title)
                .setMessage(filename)
                .show();
    }

    private boolean sourceIsURL() {
        return source.getScheme().equals("http") || source.getScheme().equals("https");
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
            TxcImportUtility.ImportSpec importSpec =
                    portingUtility.prepareImport(ImportActivity.this, source);
            // Check if it's a deck we've already imported.
            if (false && portingUtility.isExistingDeck(importSpec)) {
                portingUtility.abortImport(importSpec);
                alertUserOfFailure(getString(R.string.import_failure_existing_deck));
                return;
            }
            // Check if it's a different version of a deck we've already imported.
            if (importSpec.externalId != null && !importSpec.externalId.isEmpty()) {
                long otherVersion = portingUtility.getExistingDeckId(importSpec);
                if (otherVersion != -1) {
                    handleVersionOverride(importSpec, otherVersion);
                    return;
                }
            }
            portingUtility.executeImport(importSpec);
        } catch (ImportException e) {
            handleError(e);
            return;
        }
        goToMainScreen();
    }

    private void handleVersionOverride(
            final TxcImportUtility.ImportSpec importSpec, final long otherVersion) {
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
                                    portingUtility.executeImport(importSpec);
                                } catch (ImportException e) {
                                    handleError(e);
                                    return;
                                }
                                goToMainScreen();
                                break;
                            case 2:
                                try {
                                    portingUtility.executeImport(importSpec);
                                } catch (ImportException e) {
                                    handleError(e);
                                    return;
                                }
                                deckRepository.deleteDeck(otherVersion);
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
        Intent intent = new Intent(this, MyDecksActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
