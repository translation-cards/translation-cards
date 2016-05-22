package org.mercycorps.translationcards.txcmaker;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.ChildList;
import com.google.api.services.drive.model.ChildReference;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.ParentReference;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.channels.Channels;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class TxcBuilderTaskHandler extends HttpServlet {

  private static final String CSV_EXPORT_TYPE = "text/csv";

  private static final String SRC_HEADER_LANGUAGE = "Language";
  private static final String SRC_HEADER_LABEL = "Label";
  private static final String SRC_HEADER_TRANSLATION_TEXT = "Translation";
  private static final String SRC_HEADER_FILENAME = "Filename";

  private static final int BUFFER_SIZE = 1024;
  private static final String GCS_BUCKET_NAME = "nworden-txcmaker2.google.com.a.appspot.com";

  private byte[] buffer = new byte[BUFFER_SIZE];
  private GcsService gcsService = GcsServiceFactory.createGcsService();

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    produceTxcJson(req, resp);
  }

  private void produceTxcJson(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    Drive drive = getDriveOrOAuth(req, resp, req.getParameter("userid"), false);
    Random random = new Random();
    GcsFilename gcsFilename = new GcsFilename(
        GCS_BUCKET_NAME, String.format("tmp-txc-%d", random.nextInt()));
    OutputStream gcsOutput = Channels.newOutputStream(
        gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance()));
    TxcPortingUtility.ExportSpec exportSpec = new TxcPortingUtility.ExportSpec()
        .setDeckLabel(req.getParameter("deckName"))
        .setPublisher(req.getParameter("publisher"))
        .setDeckId(req.getParameter("deckId"))
        .setLicenseUrl(req.getParameter("licenseUrl"))
        .setLocked(req.getParameter("locked") != null);
    String audioDirId = req.getParameter("audioDirId");
    ChildList audioList = drive.children().list(audioDirId).execute();
    Map<String, String> audioFileIds = new HashMap<String, String>();
    for (ChildReference audioRef : audioList.getItems()) {
      File audioFile = drive.files().get(audioRef.getId()).execute();
      audioFileIds.put(audioFile.getOriginalFilename(), audioRef.getId());
    }
    String spreadsheetFileId = req.getParameter("docId");
    Drive.Files.Export sheetExport = drive.files().export(spreadsheetFileId, CSV_EXPORT_TYPE);
    Reader reader = new InputStreamReader(sheetExport.executeMediaAsInputStream());
    CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
    Set<String> includedAudioFiles = new HashSet<String>();
    ZipOutputStream zipOutput = new ZipOutputStream(gcsOutput);
    try {
      for (CSVRecord row : parser) {
        String language = row.get(SRC_HEADER_LANGUAGE);
        String filename = row.get(SRC_HEADER_FILENAME);
        TxcPortingUtility.CardSpec card = new TxcPortingUtility.CardSpec()
            .setLabel(row.get(SRC_HEADER_LABEL))
            .setFilename(filename)
            .setTranslationText(row.get(SRC_HEADER_TRANSLATION_TEXT));
        exportSpec.addCard(language, card);
        if (includedAudioFiles.contains(filename)) {
          continue;
        }
        includedAudioFiles.add(filename);
        zipOutput.putNextEntry(new ZipEntry(filename));
        drive.files().get(audioFileIds.get(filename)).executeMediaAndDownloadTo(zipOutput);
        zipOutput.closeEntry();
      }
      zipOutput.putNextEntry(new ZipEntry("index.json"));
      zipOutput.write(TxcPortingUtility.buildTxcJson(exportSpec).getBytes());
      zipOutput.closeEntry();
    } finally {
      parser.close();
      reader.close();
      zipOutput.close();
    }
    File targetFileInfo = new File();
    targetFileInfo.setTitle("yourgreattxc.txc");
    targetFileInfo.setParents(Collections.singletonList(new ParentReference().setId(audioDirId)));
    InputStream txcContentStream = Channels.newInputStream(
        gcsService.openPrefetchingReadChannel(gcsFilename, 0, BUFFER_SIZE));
    drive.files().insert(targetFileInfo, new InputStreamContent(null, txcContentStream)).execute();
    resp.getWriter().println("Your file should arrive in Drive shortly.");
  }

  private Drive getDriveOrOAuth(
      HttpServletRequest req, HttpServletResponse resp, String userId, boolean orOAuth)
      throws IOException {
    AuthorizationCodeFlow flow = AuthUtils.newFlow(getServletContext());
    Credential credential = flow.loadCredential(userId);
    if (credential == null) {
      if (orOAuth) {
        String url = flow.newAuthorizationUrl()
            .setRedirectUri(AuthUtils.getRedirectUri(req))
            .build();
        resp.sendRedirect(url);
      }
      return null;
    } else {
      return AuthUtils.getDriveService(credential);
    }
  }
}

