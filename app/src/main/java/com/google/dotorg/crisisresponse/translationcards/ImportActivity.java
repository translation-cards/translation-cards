package com.google.dotorg.crisisresponse.translationcards;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
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

public class ImportActivity extends AppCompatActivity {

    private static final String TAG = "ImportActivity";

    private static final String INDEX_FILENAME = "index";
    private static final int BUFFER_SIZE = 2048;

    private TextView infoText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        importData(getIntent().getScheme(), getIntent().getData());
    }

    private void initView() {
        setContentView(R.layout.activity_import);
        infoText = (TextView) findViewById(R.id.info);
    }

    private void importData(String scheme, Uri source) {
        ZipFile zip = getZip(scheme, source);
        if (zip == null) {
            // TODO(nworden): dialog to tell user about this
            Log.d(TAG, "Failed to obtain zip file; aborting import.");
            return;
        }
        String filename = source.getLastPathSegment();
        List<ImportItem> index = readIndex(zip);
        // TODO(nworden): ask user for confirmation
        if (loadData(zip, index)) {
            infoText.setText(String.format(
                    "Loaded %d translations from %s", index.size(), filename));
        } else {
            infoText.setText("Failure");
        }
    }

    private ZipFile getZip(String scheme, Uri source) {
        if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            try {
                return new ZipFile(new File(source.getPath()));
            } catch (ZipException e) {
                Log.d(TAG, "Error handling zip file.");
            } catch (IOException e) {
                Log.d(TAG, "Error opening file.");
            }
            return null;
        } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            return null;
        } else {
            Log.d(TAG, String.format("Unexpected scheme: %s", scheme));
            return null;
        }
    }

    private List<ImportItem> readIndex(ZipFile zip) {
        InputStream inputStream = null;
        try {
            // TODO(nworden): handle the possibility of everything being wrapped up in a directory
            inputStream = zip.getInputStream(zip.getEntry(INDEX_FILENAME));
        } catch (IOException e) {
            Log.d(TAG, "Failed to open index.");
            return null;
        }
        List<ImportItem> results = new ArrayList<>();
        Scanner s = new Scanner(inputStream);
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

    private boolean loadData(ZipFile zip, List<ImportItem> index) {
        DbManager dbm = new DbManager(this);
        Map<String, Long> dictionaryLookup = getDictionaryLookup(dbm);
        File recordingsDir = new File(getFilesDir(), "recordings");
        File targetDir = new File(recordingsDir, String.format("%s-%d",
                zip.getName().replace('.', '-'), (new Random()).nextInt()));
        targetDir.mkdirs();
        // Iterate backwards through the list, because we're adding each translation at the top of
        // the list and want them to appear in the correct order.
        for (int i = index.size() - 1; i >= 0; i--) {
            ImportItem item = index.get(i);
            File targetFile = new File(targetDir, item.name);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = zip.getInputStream(zip.getEntry(item.name));
                outputStream = new FileOutputStream(targetFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int read;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
            } catch(IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        return false;
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        return false;
                    }
                }
            }
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
