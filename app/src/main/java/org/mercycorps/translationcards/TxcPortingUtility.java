package org.mercycorps.translationcards;

import android.content.Context;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Class to handle constructing and reading .txc files.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class TxcPortingUtility {

    private static final String INDEX_FILENAME = "card_deck.csv";
    private static final String ALT_INDEX_FILENAME = "card_deck.txt";
    private static final int BUFFER_SIZE = 2048;

    public void exportData(Deck deck, Dictionary[] dictionaries, File file) throws ExportException {
        ZipOutputStream zos = null;
        try {
            OutputStream os;
            try {
                os = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new ExportException(ExportException.ExportProblem.TARGET_FILE_NOT_FOUND, e);
            }
            zos = new ZipOutputStream(os);
            Map<String, Dictionary.Translation> translationFilenames =
                    buildIndex(deck, dictionaries, zos);
            for (String filename : translationFilenames.keySet()) {
                addFileToZip(filename, translationFilenames.get(filename), zos);
            }
        } catch (ExportException e) {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException ignored) {
                    // Do nothing, we've failed already anyway.
                }
            }
            file.delete();
            throw e;
        }
        try {
            zos.close();
        } catch (IOException e) {
            throw new ExportException(ExportException.ExportProblem.WRITE_ERROR, e);
        }
    }

    private Map<String, Dictionary.Translation> buildIndex(
            Deck deck, Dictionary[] dictionaries, ZipOutputStream zos) throws ExportException {
        Map<String, Dictionary.Translation> translationFilenames = new HashMap<>();
        try {
            zos.putNextEntry(new ZipEntry(INDEX_FILENAME));
            String metaLine = String.format("META:%s|%s\n", deck.getLabel(), deck.getPublisher());
            zos.write(metaLine.getBytes());
            for (Dictionary dictionary : dictionaries) {
                String language = dictionary.getLabel();
                for (int i = 0; i < dictionary.getTranslationCount(); i++) {
                    Dictionary.Translation translation = dictionary.getTranslation(i);
                    String translationFilename = buildUniqueFilename(
                            translation, translationFilenames);
                    translationFilenames.put(translationFilename, translation);
                    String line = String.format("%s|%s|%s\n",
                            translation.getLabel(), translationFilename, language);
                    zos.write(line.getBytes());
                }
            }
            zos.closeEntry();
        } catch (IOException e) {
            throw new ExportException(ExportException.ExportProblem.WRITE_ERROR, e);
        }
        return translationFilenames;
    }

    private String buildUniqueFilename(
            Dictionary.Translation translation,
            Map<String, Dictionary.Translation> translationFilenames) throws ExportException {
        String baseName = new File(translation.getFilename()).getName();
        if (!translationFilenames.containsKey(baseName)) {
            return baseName;
        }
        int appendage = 2;
        while (appendage < 100) {
            // We have to have this cut off at some point. If someone has 100 files of the same name
            // somehow, this is going to fail for them.
            String name = String.format("%s-%d", baseName, appendage);
            if (!translationFilenames.containsKey(name)) {
                return name;
            }
            appendage++;
        }
        throw new ExportException(ExportException.ExportProblem.TOO_MANY_DUPLICATE_FILENAMES, null);
    }

    private void addFileToZip(
            String filename, Dictionary.Translation translation, ZipOutputStream zos)
            throws ExportException {
        try {
            zos.putNextEntry(new ZipEntry(filename));
            FileInputStream translationInput =
                    new FileInputStream(new File(translation.getFilename()));
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = translationInput.read(buffer)) != -1) {
                zos.write(buffer, 0, read);
            }
            translationInput.close();
            zos.closeEntry();
        } catch (IOException e) {
            throw new ExportException(ExportException.ExportProblem.WRITE_ERROR, e);
        }
    }

    public ImportInfo prepareImport(Context context, Uri source) throws ImportException {
        String hash = getFileHash(context, source);
        ZipInputStream zip = getZip(context, source);
        String filename = source.getLastPathSegment();
        File targetDir = getImportTargetDirectory(context, filename);
        String indexFilename = readFiles(zip, targetDir);
        return getIndex(targetDir, indexFilename, filename, hash);
    }

    public void executeImport(Context context, ImportInfo importInfo) throws ImportException {
        loadData(context, importInfo);
    }

    private String getFileHash(Context context, Uri source) throws ImportException {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(source);
        } catch (FileNotFoundException e) {
            throw new ImportException(ImportException.ImportProblem.FILE_NOT_FOUND, e);
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new ImportException(null, e);
        }
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                md.update(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new ImportException(ImportException.ImportProblem.READ_ERROR, e);
        }
        return (new BigInteger(md.digest())).toString(16);
    }

    private ZipInputStream getZip(Context context, Uri source) throws ImportException {
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(source);
        } catch (FileNotFoundException e) {
            throw new ImportException(ImportException.ImportProblem.FILE_NOT_FOUND, e);
        }
        return new ZipInputStream(inputStream);
    }

    private File getImportTargetDirectory(Context context, String filename) {
        File recordingsDir = new File(context.getFilesDir(), "recordings");
        File targetDir = new File(recordingsDir, String.format("%s-%d",
                filename, (new Random()).nextInt()));
        targetDir.mkdirs();
        return targetDir;
    }

    private String readFiles(ZipInputStream zip, File targetDir) throws ImportException {
        String indexFilename = null;
        FileOutputStream outputStream = null;
        Exception readError = null;
        try {
            ZipEntry zipEntry;
            while ((zipEntry = zip.getNextEntry()) != null) {
                String name = zipEntry.getName();
                if (INDEX_FILENAME.equals(name) || ALT_INDEX_FILENAME.equals(name)) {
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
            readError = e;
        } finally {
            try {
                zip.close();
            } catch (IOException e) {
                readError = e;
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    readError = e;
                }
            }
        }
        if (readError != null) {
            throw new ImportException(ImportException.ImportProblem.READ_ERROR, readError);
        }
        if (indexFilename == null) {
            targetDir.delete();
            throw new ImportException(ImportException.ImportProblem.NO_INDEX_FILE, null);
        }
        return indexFilename;
    }

    private ImportInfo getIndex(File dir, String indexFilename, String defaultLabel, String hash)
            throws ImportException {
        String label = defaultLabel;
        String publisher = null;
        String externalId = null;
        String version = null;
        List<ImportItem> items = new ArrayList<>();
        Scanner s;
        try {
            s = new Scanner(new File(dir, indexFilename));
        } catch (FileNotFoundException e) {
            throw new ImportException(ImportException.ImportProblem.NO_INDEX_FILE, e);
        }
        boolean isFirstLine = true;
        while (s.hasNextLine()) {
            String line = s.nextLine();
            if (isFirstLine) {
                isFirstLine = false;
                // It was the first line; see if it's meta information.
                if (line.startsWith("META:")) {
                    String[] metaLine = line.substring(5).split("|");
                    label = metaLine[0];
                    publisher = metaLine[1];
                    externalId = metaLine[2];
                    version = metaLine[3];
                    continue;
                }
            }
            String[] split = line.trim().split("\\|");
            if (split.length == 3) {
                items.add(new ImportItem(split[0], split[1], split[2], ""));
            } else if (split.length == 4) {
                items.add(new ImportItem(split[0], split[1], split[2], split[3]));
            } else {
                s.close();
                throw new ImportException(ImportException.ImportProblem.INVALID_INDEX_FILE, null);
            }
        }
        s.close();
        return new ImportInfo(label, publisher, externalId, version, hash, items, dir);
    }

    private void loadData(Context context, ImportInfo importInfo) {
        DbManager dbm = new DbManager(context);
        long creationTime = (new Date()).getTime() / 1000;
        long deckId = dbm.addDeck(importInfo.label, importInfo.publisher, creationTime,
                importInfo.externalId, importInfo.version, importInfo.hash);
        Map<String, Long> dictionaryLookup = new HashMap<>();
        int dictionaryIndex = 0;
        // Iterate backwards through the list, because we're adding each translation at the top of
        // the list and want them to appear in the correct order.
        for (int i = importInfo.items.size() - 1; i >= 0; i--) {
            ImportItem item = importInfo.items.get(i);
            File targetFile = new File(importInfo.dir, item.name);
            String dictionaryLookupKey = item.language.toLowerCase();
            if (!dictionaryLookup.containsKey(dictionaryLookupKey)) {
                long dictionaryId = dbm.addDictionary(item.language, dictionaryIndex, deckId);
                dictionaryIndex++;
                dictionaryLookup.put(dictionaryLookupKey, dictionaryId);
            }
            long dictionaryId = dictionaryLookup.get(dictionaryLookupKey);
            dbm.addTranslationAtTop(dictionaryId, item.text, false, targetFile.getAbsolutePath(),
                    item.translatedText);
        }
    }

    public class ImportInfo {

        public final String label;
        public final String publisher;
        public final String externalId;
        public final String version;
        public final String hash;
        public final List<ImportItem> items;
        public final File dir;

        public ImportInfo(String label, String publisher, String externalId, String version,
                          String hash, List<ImportItem> items, File dir) {
            this.label = label;
            this.publisher = publisher;
            this.externalId = externalId;
            this.version = version;
            this.hash = hash;
            this.items = items;
            this.dir = dir;
        }
    }

    private class ImportItem {

        public final String text;
        public final String name;
        public final String language;
        public final String translatedText;


        public ImportItem(String text, String name, String language, String translatedText) {
            this.text = text;
            this.name = name;
            this.language = language;
            this.translatedText = translatedText;
        }
    }
}
