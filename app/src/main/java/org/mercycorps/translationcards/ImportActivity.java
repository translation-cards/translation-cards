package org.mercycorps.translationcards;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
                                    portingUtility.importData(ImportActivity.this, source);
                                } catch (TxcPortingUtility.ImportException e) {
                                    alertUserOfFailure(e);
                                    return;
                                }
                                Intent intent = new Intent(ImportActivity.this, MainActivity.class);
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

    private void alertUserOfFailure(TxcPortingUtility.ImportException error) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.import_failure_alert_title)
                .setMessage(R.string.import_failure_alert_message)
                .setNeutralButton(R.string.misc_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImportActivity.this.finish();
                    }
                })
                .show();
    }

    private class ImportItem {

        public final String text;
        public final String name;
        public final String language;

        public ImportItem(String text, String name, String language) {
            this.text = text;
            this.name = name;
            this.language = language;
        }
    }
}
