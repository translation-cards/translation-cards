package com.google.dotorg.crisisresponse.translationcards;

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

    private static final String TAG = "ImportActivity";

    private static final String INDEX_FILENAME_1 = "card_deck.csv";
    private static final String INDEX_FILENAME_2 = "card_deck.txt";
    private static final int BUFFER_SIZE = 2048;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        importData(getIntent().getData());
    }

    private void importData(Uri source) {
        ZipInputStream zip = getZip(source);
        if (zip == null) {
            alertUserOfFailure();
            return;
        }
        String filename = source.getLastPathSegment();
        File targetDir = getTargetDirectory(filename);
        String indexFilename = readFiles(zip, targetDir);
        if (indexFilename == null) {
            alertUserOfFailure();
            return;
        }
        List<ImportItem> index = getIndex(targetDir, indexFilename);
        if (index == null) {
            alertUserOfFailure();
            return;
        }
        confirmAndLoadData(targetDir, index);
    }

    private ZipInputStream getZip(Uri source) {
        InputStream inputStream = null;
        try {
            inputStream = getContentResolver().openInputStream(source);
        } catch (FileNotFoundException e) {
            return null;
        }
        return new ZipInputStream(inputStream);
    }

    private File getTargetDirectory(String filename) {
        File recordingsDir = new File(getFilesDir(), "recordings");
        File targetDir = new File(recordingsDir, String.format("%s-%d",
                filename, (new Random()).nextInt()));
        targetDir.mkdirs();
        return targetDir;
    }

    private String readFiles(ZipInputStream zip, File targetDir) {
        String indexFilename = null;
        FileOutputStream outputStream = null;
        try {
            ZipEntry zipEntry = null;
            while ((zipEntry = zip.getNextEntry()) != null) {
                String name = zipEntry.getName();
                if (INDEX_FILENAME_1.equals(name) || INDEX_FILENAME_2.equals(name)) {
                    indexFilename = name;
                }
                outputStream = new FileOutputStream(new File(targetDir, name));
                byte[] buffer = new byte[BUFFER_SIZE];
                int read;
                while ((read = zip.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
                outputStream.close();
                outputStream = null;
            }
        } catch (IOException e) {
        } finally {
            try {
                zip.close();
            } catch (IOException e) {
                return null;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    return null;
                }
            }
        }
        if (indexFilename == null) {
            targetDir.delete();
            Log.d(TAG, "Failed to find index.");
        }
        return indexFilename;
    }

    private List<ImportItem> getIndex(File dir, String indexFilename) {
        List<ImportItem> results = new ArrayList<>();
        Scanner s;
        try {
            s = new Scanner(new File(dir, indexFilename));
        } catch (FileNotFoundException e) {
            return null;
        }
        while (s.hasNextLine()) {
            String line = s.nextLine();
            String[] split = line.trim().split("\\|");
            if (split.length != 3) {
                Log.d(TAG, String.format("Improper index line: " + line));
                s.close();
                return null;
            }
            results.add(new ImportItem(split[0], split[1], split[2]));
        }
        s.close();
        return results;
    }

    private void loadData(File dir, List<ImportItem> index) {
        DbManager dbm = new DbManager(this);
        Map<String, Long> dictionaryLookup = getDictionaryLookup(dbm);
        // Iterate backwards through the list, because we're adding each translation at the top of
        // the list and want them to appear in the correct order.
        for (int i = index.size() - 1; i >= 0; i--) {
            ImportItem item = index.get(i);
            File targetFile = new File(dir, item.name);
            long dictionaryId = dictionaryLookup.get(item.language.toLowerCase());
            dbm.addTranslationAtTop(dictionaryId, item.text, false, targetFile.getAbsolutePath());
        }
    }

    private Map<String, Long> getDictionaryLookup(DbManager dbm) {
        Map<String, Long> results = new HashMap<>();
        for (Dictionary dictionary : dbm.getAllDictionaries()) {
            results.put(dictionary.getLabel().toLowerCase(), dictionary.getDbId());
        }
        return results;
    }

    private void alertUserOfFailure() {
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

    private void confirmAndLoadData(final File targetDir, final List<ImportItem> index) {
        new AlertDialog.Builder(this)
                .setTitle(R.string.import_confirm_alert_title)
                .setMessage(getString(R.string.import_confirm_alert_message, index.size()))
                .setPositiveButton(R.string.import_confirm_alert_positive,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData(targetDir, index);
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
