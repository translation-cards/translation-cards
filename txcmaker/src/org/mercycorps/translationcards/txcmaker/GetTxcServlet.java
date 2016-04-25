package org.mercycorps.translationcards.txcmaker;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class GetTxcServlet extends HttpServlet {

  private static final String CSV_EXPORT_TYPE = "text/csv";

  private static final String SRC_HEADER_LANGUAGE = "Language";
  private static final String SRC_HEADER_LABEL = "Label";
  private static final String SRC_HEADER_TRANSLATION_TEXT = "Translation";
  private static final String SRC_HEADER_FILENAME = "Filename";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    // We don't actually need the Drive service yet, but we authenticate in advance because
    // otherwise OAuth will send them back here anyway.
    Drive drive = getDriveOrOAuth(req, resp, true);
    if (drive == null) {
      // We've already redirected.
      return;
    }
    displayForm(resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Drive drive = getDriveOrOAuth(req, resp, false);
    if (drive == null) {
      resp.getWriter().println("You haven't provided Drive authentication.");
      return;
    }
    produceTxcJson(drive, req, resp);
  }

  private void displayForm(HttpServletResponse resp) throws IOException {
    resp.getWriter().println(
        "<form method=\"post\">" +
        "<p>Doc ID: <input type=\"text\" name=\"docId\" /><br />" +
        "Deck name: <input type=\"text\" name=\"deckName\" /><br />" +
        "Publisher: <input type=\"text\" name=\"publisher\" /><br />" +
        "Deck ID: <input type=\"text\" name=\"deckId\" /><br />" +
        "License URL: <input type=\"text\" name=\"licenseUrl\" /><br />" +
        "Locked: <input type=\"checkbox\" name=\"locked\" /><br />" +
        "<input type=\"submit\" /></p>" +
        "</form>"
    );
  }

  private void produceTxcJson(Drive drive, HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    TxcPortingUtility.ExportSpec exportSpec = new TxcPortingUtility.ExportSpec()
        .setDeckLabel(req.getParameter("deckName"))
        .setPublisher(req.getParameter("publisher"))
        .setDeckId(req.getParameter("deckId"))
        .setLicenseUrl(req.getParameter("licenseUrl"))
        .setLocked(req.getParameter("locked") != null);
    String spreadsheetFileId = req.getParameter("docId");
    Drive.Files.Export sheetExport = drive.files().export(spreadsheetFileId, CSV_EXPORT_TYPE);
    Reader reader = new InputStreamReader(sheetExport.executeMediaAsInputStream());
    CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
    try {
      for (CSVRecord row : parser) {
        String language = row.get(SRC_HEADER_LANGUAGE);
        TxcPortingUtility.CardSpec card = new TxcPortingUtility.CardSpec()
            .setLabel(row.get(SRC_HEADER_LABEL))
            .setFilename(row.get(SRC_HEADER_FILENAME))
            .setTranslationText(row.get(SRC_HEADER_TRANSLATION_TEXT));
        exportSpec.addCard(language, card);
      }
    } finally {
      parser.close();
      reader.close();
    }
    resp.getWriter().println(TxcPortingUtility.buildTxcJson(exportSpec));
  }

  private Drive getDriveOrOAuth(HttpServletRequest req, HttpServletResponse resp, boolean orOAuth)
      throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();
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
