package org.mercycorps.translationcards.porting;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;

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


public class TxcExportUtility {

    private static final String SPEC_FILENAME = "card_deck.json";
    private static final int BUFFER_SIZE = 2048;

    public void exportData(Deck deck, String exportedDeckName, Dictionary[] dictionaries, File file)
            throws ExportException {
        ZipOutputStream zos = null;
        try {
            OutputStream os;
            try {
                os = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                throw new ExportException(ExportException.ExportProblem.TARGET_FILE_NOT_FOUND, e);
            }
            zos = new ZipOutputStream(os);
            Map<String, Translation> translationFilenames =
                    buildSpec(deck, exportedDeckName, dictionaries, zos);
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

    private Map<String, Translation> buildSpec(
            Deck deck, String exportedDeckName, Dictionary[] dictionaries, ZipOutputStream zos)
            throws ExportException {
        Map<String, Translation> translationFilenames = new HashMap<>();
        JSONObject json = new JSONObject();
        try {
            json.put(JsonKeys.DECK_LABEL, exportedDeckName);
            json.put(JsonKeys.PUBLISHER, deck.getAuthor());
            if (deck.getExternalId() != null) {
                json.put(JsonKeys.EXTERNAL_ID, deck.getExternalId());
            }
            json.put(JsonKeys.TIMESTAMP, deck.getTimestamp());
            json.put(JsonKeys.SOURCE_LANGUAGE, deck.getSourceLanguageIso());
            json.put(JsonKeys.LOCKED, deck.isLocked());
            JSONArray dictionariesJson = new JSONArray();
            for (Dictionary dictionary : dictionaries) {
                JSONObject dictionaryJson = new JSONObject();
                dictionaryJson.put(
                        JsonKeys.DICTIONARY_DEST_ISO_CODE, dictionary.getDestLanguageIso());
                JSONArray cardsJson = new JSONArray();
                for (int i = 0; i < dictionary.getTranslationCount(); i++) {
                    Translation translation = dictionary.getTranslation(i);
                    String translationFilename = "";
                    if(translation.isAudioFilePresent()) {
                        translationFilename = buildUniqueFilename(translation, translationFilenames);
                        translationFilenames.put(translationFilename, translation);
                    }
                    JSONObject cardJson = new JSONObject();
                    cardJson.put(JsonKeys.CARD_LABEL, translation.getLabel());
                    cardJson.put(JsonKeys.CARD_DEST_AUDIO, translationFilename);
                    cardJson.put(JsonKeys.CARD_DEST_TEXT, translation.getTranslatedText());
                    cardsJson.put(cardJson);
                }
                dictionaryJson.put(JsonKeys.CARDS, cardsJson);
                dictionariesJson.put(dictionaryJson);
            }
            json.put(JsonKeys.DICTIONARIES, dictionariesJson);
        } catch (JSONException e) {
            throw new ExportException(ExportException.ExportProblem.WRITE_ERROR, e);
        }
        try {
            zos.putNextEntry(new ZipEntry(SPEC_FILENAME));
            zos.write(json.toString().getBytes());
            zos.closeEntry();
        } catch (IOException e) {
            throw new ExportException(ExportException.ExportProblem.WRITE_ERROR, e);
        }
        return translationFilenames;
    }

    private String buildUniqueFilename(
            Translation translation,
            Map<String, Translation> translationFilenames) throws ExportException {
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
            String filename, Translation translation, ZipOutputStream zos)
            throws ExportException {
        try {
            zos.putNextEntry(new ZipEntry(filename));
            FileInputStream translationInput = getFileInputStream(translation);
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

    private FileInputStream getFileInputStream(Translation translation) throws IOException {
        FileInputStream translationInput;
        if (translation.getIsAsset()) {
            translationInput = MainApplication.getContextFromMainApp().getAssets().openFd(translation.getFilename()).createInputStream();
        } else {
            translationInput = new FileInputStream(new File(translation.getFilename()));
        }
        return translationInput;
    }
}
