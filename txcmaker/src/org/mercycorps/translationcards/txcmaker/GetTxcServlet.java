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
    Drive drive = getDriveOrOAuth(req, resp, true);
    if (drive == null) {
      // We've already redirected.
      return;
    }
    handleRequest(drive, req, resp);
  }

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Drive drive = getDriveOrOAuth(req, resp, false);
    if (drive == null) {
      resp.getWriter().println("You haven't provided Drive authentication.");
      return;
    }
    handleRequest(drive, req, resp);
  }

  private void handleRequest(Drive drive, HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    String spreadsheetFileId = req.getParameter("docid");
    if (spreadsheetFileId == null) {
      displayForm(resp);
    } else {
      produceTxcJson(drive, spreadsheetFileId, resp);
    }
  }

  private void displayForm(HttpServletResponse resp) throws IOException {
    resp.getWriter().println(
        "<form method=\"post\">" +
        "<p>Doc ID: <input type=\"text\" name=\"docid\" />" +
        "<input type=\"submit\" />" +
        "</form>"
    );
  }

  private void produceTxcJson(Drive drive, String spreadsheetFileId, HttpServletResponse resp)
      throws IOException {
    Drive.Files.Export sheetExport = drive.files().export(spreadsheetFileId, CSV_EXPORT_TYPE);
    Reader reader = new InputStreamReader(sheetExport.executeMediaAsInputStream());
    CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
    try {
      for (CSVRecord row : parser) {
        resp.getWriter().println(String.format("%s -- %s -- %s -- %s",
            row.get("Language"), row.get("Label"), row.get("Translation"), row.get("Filename")));
        resp.getWriter().println(row.toString());
      }
    } finally {
      parser.close();
      reader.close();
    }
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
