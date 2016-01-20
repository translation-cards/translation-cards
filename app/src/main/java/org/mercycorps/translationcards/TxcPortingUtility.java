package org.mercycorps.translationcards;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
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

    public void exportData(Dictionary[] dictionaries, File file) throws ExportException {
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
                    buildIndex(dictionaries, zos);
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
            Dictionary[] dictionaries, ZipOutputStream zos) throws ExportException {
        Map<String, Dictionary.Translation> translationFilenames = new HashMap<>();
        try {
            zos.putNextEntry(new ZipEntry(INDEX_FILENAME));
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

    public void importData(Context context, Uri source) throws ImportException {
        ZipInputStream zip = getZip(context, source);
        String filename = source.getLastPathSegment();
        File targetDir = getImportTargetDirectory(context, filename);
        String indexFilename = readFiles(zip, targetDir);
        List<ImportItem> index = getIndex(targetDir, indexFilename);
        loadData(context, targetDir, index);
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

    private List<ImportItem> getIndex(File dir, String indexFilename) throws ImportException {
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
                s.close();
                throw new ImportException(ImportException.ImportProblem.INVALID_INDEX_FILE, null);
            }
            results.add(new ImportItem(split[0], split[1], split[2]));
        }
        s.close();
        return results;
    }

    private void loadData(Context context, File dir, List<ImportItem> index) {
        DbManager dbm = new DbManager(context);
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

    public static class ExportException extends Exception {

        public enum ExportProblem {
            TARGET_FILE_NOT_FOUND,
            WRITE_ERROR,
            TOO_MANY_DUPLICATE_FILENAMES
        }

        private final ExportProblem problem;

        public ExportException(ExportProblem problem, Throwable cause) {
            super(cause);
            this.problem = problem;
        }

        public ExportProblem getProblem() {
            return problem;
        }
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

    public static class ImportException extends Exception {

        public enum ImportProblem {
            FILE_NOT_FOUND,
            READ_ERROR,
            NO_INDEX_FILE,
            INVALID_INDEX_FILE
        }

        private final ImportProblem problem;

        public ImportException(ImportProblem problem, Throwable cause) {
            super(cause);
            this.problem = problem;
        }

        public ImportProblem getProblem() {
            return problem;
        }
    }
}
