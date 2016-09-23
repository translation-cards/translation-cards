package org.mercycorps.translationcards.porting;


import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.service.LanguageService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class TxcExportUtility {

    static final String SPEC_FILENAME = "card_deck.json";
    private static final int BUFFER_SIZE = 2048;
    private InputStreamBuilder inputStreamBuilder;

    public TxcExportUtility(InputStreamBuilder inputStreamBuilder) {
        this.inputStreamBuilder = inputStreamBuilder;
    }

    public void exportDeck(Deck deck, String exportedDeckName, File file)
            throws ExportException {
        ZipOutputStream zos = null;
        try {
            OutputStream os;
            os = new FileOutputStream(file);
            zos = new ZipOutputStream(os);
            writeDeckToZipStream(deck, exportedDeckName, zos);
            addAudioFilesToZip(deck.getAudioFilePaths(), zos);
        } catch (FileNotFoundException e) {
            throw new ExportException(ExportException.ExportProblem.TARGET_FILE_NOT_FOUND, e);
        } catch (ExportException e) {
            try {
                zos.close();
            } catch (IOException ignored) {
                // Do nothing, we've failed already anyway.
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

    protected void writeDeckToZipStream(Deck deck, String exportedDeckName, ZipOutputStream zipOutputStream) throws ExportException {
        try {
            JSONObject deckAsJSON = deck.toJSON(exportedDeckName);
            zipOutputStream.putNextEntry(new ZipEntry(SPEC_FILENAME));
            zipOutputStream.write(deckAsJSON.toString().getBytes());
            zipOutputStream.closeEntry();
        } catch (IOException | JSONException e) {
            throw new ExportException(ExportException.ExportProblem.WRITE_ERROR, e);
        }
    }

    protected void addAudioFilesToZip(Map<String, Boolean> filePaths, ZipOutputStream zipOutputStream) throws ExportException {
        try {
            for (Map.Entry<String, Boolean> entry : filePaths.entrySet()) {
                String baseFilename = new File(entry.getKey()).getName();
                zipOutputStream.putNextEntry(new ZipEntry(baseFilename));
                FileInputStream translationAudioInput = getFileInputStream(entry);
                byte[] buffer = new byte[BUFFER_SIZE];
                int read;
                while ((read = translationAudioInput.read(buffer)) != -1) {
                    zipOutputStream.write(buffer, 0, read);
                }
                translationAudioInput.close();
                zipOutputStream.closeEntry();
            }
        } catch (IOException e) {
            throw new ExportException(ExportException.ExportProblem.WRITE_ERROR, e);
        }
    }

    private FileInputStream getFileInputStream(Map.Entry<String, Boolean> fileToAssetEntry) throws IOException {
        FileInputStream translationInput;
        if (fileToAssetEntry.getValue()) {
            translationInput = inputStreamBuilder.getAssetInputStream(fileToAssetEntry.getKey());
        } else {
            translationInput = inputStreamBuilder.getFileInputStream(fileToAssetEntry.getKey());
        }

        return translationInput;
    }
}
