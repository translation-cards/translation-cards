package org.mercycorps.translationcards;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Class to handle constructing and reading .txc files.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class TxcPortingUtility {

    private static final String INDEX_FILENAME = "card_deck.csv";
    private static final int BUFFER_SIZE = 2048;

    public void writeTxcFile(Dictionary[] dictionaries, File file) throws ExportException {
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
}
