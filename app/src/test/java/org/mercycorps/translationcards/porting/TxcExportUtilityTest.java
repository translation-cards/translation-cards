package org.mercycorps.translationcards.porting;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.service.LanguageService;
import org.mockito.ArgumentCaptor;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TxcExportUtilityTest {

    private InputStreamBuilder inputStreamBuilder;
    private FileInputStream fileInputStream;
    private ZipOutputStream zipOutputStream;
    private TxcExportUtility txcExportUtility;
    private LanguageService languageService;

    @Before
    public void setUp() throws Exception {
        inputStreamBuilder = mock(InputStreamBuilder.class);
        fileInputStream = mock(FileInputStream.class);
        zipOutputStream = mock(ZipOutputStream.class);
        txcExportUtility = new TxcExportUtility(inputStreamBuilder);
    }

    @Test
    public void shouldAddDeckJSONToZipStream() throws Exception {
        JSONObject deckJSON = mock(JSONObject.class);
        when(deckJSON.toString()).thenReturn("JSON representation of a deck");
        Deck deck = mock(Deck.class);
        when(deck.toJSON(any(String.class))).thenReturn(deckJSON);

        txcExportUtility.writeDeckToZipStream(deck, "Export Name", zipOutputStream);

        ArgumentCaptor<ZipEntry> zipEntryCaptor = ArgumentCaptor.forClass(ZipEntry.class);
        verify(zipOutputStream).putNextEntry(zipEntryCaptor.capture());
        assertEquals(TxcExportUtility.SPEC_FILENAME, zipEntryCaptor.getValue().getName());
        verify(zipOutputStream).write(deckJSON.toString().getBytes());
        verify(zipOutputStream).closeEntry();
    }

    @Test
    public void shouldRetrieveAssetAudioFilesFromAssetStream() throws Exception {
        String assetName = "anAssetName";
        when(fileInputStream.read(any(byte[].class))).thenReturn(-1);
        when(inputStreamBuilder.getAssetInputStream(assetName)).thenReturn(fileInputStream);
        HashMap<String, Boolean> filePaths = new HashMap<>();
        filePaths.put(assetName, true);

        txcExportUtility.addAudioFilesToZip(filePaths, zipOutputStream);

        verify(inputStreamBuilder).getAssetInputStream(assetName);
    }

    @Test
    public void shouldRetrieveAudioFilesFromFileInputStream() throws Exception {
        String filePath = "aFilePath";
        when(fileInputStream.read(any(byte[].class))).thenReturn(-1);
        when(inputStreamBuilder.getFileInputStream(filePath)).thenReturn(fileInputStream);
        HashMap<String, Boolean> filePaths = new HashMap<>();
        filePaths.put(filePath, false);

        txcExportUtility.addAudioFilesToZip(filePaths, zipOutputStream);

        verify(inputStreamBuilder).getFileInputStream(filePath);
    }

    @Test
    public void shouldWriteAssetTranslationAudioToZipOutputStream() throws Exception {
        String assetName = "anAssetName";
        when(fileInputStream.read(any(byte[].class))).thenReturn(15).thenReturn(-1);
        when(inputStreamBuilder.getAssetInputStream(assetName)).thenReturn(fileInputStream);
        HashMap<String, Boolean> filePaths = new HashMap<>();
        filePaths.put(assetName, true);

        txcExportUtility.addAudioFilesToZip(filePaths, zipOutputStream);

        ArgumentCaptor<byte[]> bufferCaptor = ArgumentCaptor.forClass(byte[].class);
        ArgumentCaptor<ZipEntry> zipEntryCaptor = ArgumentCaptor.forClass(ZipEntry.class);
        verify(fileInputStream, times(2)).read(bufferCaptor.capture());
        verify(zipOutputStream).putNextEntry(zipEntryCaptor.capture());
        assertEquals(assetName, zipEntryCaptor.getValue().getName());
        verify(zipOutputStream).write(bufferCaptor.getValue(), 0, 15);
    }
}