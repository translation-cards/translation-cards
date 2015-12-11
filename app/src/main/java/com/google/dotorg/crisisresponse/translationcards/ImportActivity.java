package com.google.dotorg.crisisresponse.translationcards;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ImportActivity extends AppCompatActivity {

    private static final String TAG = "ImportActivity";

    private static final String INDEX_FILENAME = "index";
    private static final int BUFFER_SIZE = 2048;

    private TextView infoText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        importData(getIntent().getData());
    }

    private void initView() {
        setContentView(R.layout.activity_import);
        infoText = (TextView) findViewById(R.id.info);
    }

    private void importData(Uri source) {
        Log.d(TAG, source.toString());
        if (false) return;
        ZipInputStream zip = getZip(source);
        if (zip == null) {
            // TODO(nworden): dialog to tell user about this
            Log.d(TAG, "Failed to get txc file; aborting import.");
            return;
        }
        String filename = source.getLastPathSegment();
        File targetDir = getTargetDirectory(filename);
        if (!readFiles(zip, targetDir)) {
            Log.d(TAG, "Could not read txc file.");
            return;
        }
        List<ImportItem> index = getIndex(targetDir);
        // TODO(nworden): ask user for confirmation
        if (loadData(targetDir, index)) {
            infoText.setText(String.format(
                    "Loaded %d translations from %s", index.size(), filename));
        } else {
            infoText.setText("Failure");
        }
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

    private boolean readFiles(ZipInputStream zip, File targetDir) {
        boolean foundIndex = false;
        FileOutputStream outputStream = null;
        try {
            ZipEntry zipEntry = null;
            while ((zipEntry = zip.getNextEntry()) != null) {
                String name = zipEntry.getName();
                if (INDEX_FILENAME.equals(name)) {
                    foundIndex = true;
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
                return false;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    return false;
                }
            }
        }
        if (foundIndex) {
            return true;
        } else {
            targetDir.delete();
            Log.d(TAG, "Failed to find index.");
            return false;
        }
    }

    private List<ImportItem> getIndex(File dir) {
        List<ImportItem> results = new ArrayList<>();
        Scanner s = null;
        try {
            s = new Scanner(new File(dir, "index"));
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

    private boolean loadData(File dir, List<ImportItem> index) {
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
        return true;
    }

    private Map<String, Long> getDictionaryLookup(DbManager dbm) {
        Map<String, Long> results = new HashMap<>();
        for (Dictionary dictionary : dbm.getAllDictionaries()) {
            results.put(dictionary.getLabel().toLowerCase(), dictionary.getDbId());
        }
        return results;
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
