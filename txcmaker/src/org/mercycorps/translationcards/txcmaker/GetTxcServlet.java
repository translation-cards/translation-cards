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
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class GetTxcServlet extends HttpServlet {

  private static final String CSV_EXPORT_TYPE = "text/csv";

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Drive drive = getDriveOrOAuth(req, resp);
    if (drive == null) {
      // We've already redirected.
      return;
    }
    String spreadsheetFileId = "1VxUfQG5ewP5o7QCJCde1_a1fXt7NoMyTnW9mAAVui2E";
    File spreadsheet = drive.files().get(spreadsheetFileId).execute();
    Map<String, String> exportLinks = spreadsheet.getExportLinks();
    URL url = new URL(exportLinks.get(CSV_EXPORT_TYPE));
    Reader reader = new InputStreamReader(url.openStream());
    //CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
    //try {
    //  for (CSVRecord row : parser) {
    //    //resp.getWriter().println(String.format("%s -- %s -- %s -- %s",
    //    //    row.get("Language"), row.get("Label"), row.get("Translation"), row.get("Filename")));
    //    resp.getWriter().println(row.toString());
    //  }
    //} finally {
    //  parser.close();
    //  reader.close();
    //}
    resp.getWriter().println(url.toString());
    BufferedReader br = new BufferedReader(reader);
    String line;
    while ((line = br.readLine()) != null) {
      resp.getWriter().println(line);
    }
  }

  private Drive getDriveOrOAuth(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    Credential credential = getCredentialOrOAuth(req, resp);
    return (credential == null) ? null : AuthUtils.getDriveService(credential);
  }

  private Credential getCredentialOrOAuth(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();
    AuthorizationCodeFlow flow = AuthUtils.newFlow(getServletContext());
    Credential credential = flow.loadCredential(userId);
    if (credential == null) {
      String url = flow.newAuthorizationUrl()
          .setRedirectUri(AuthUtils.getRedirectUri(req))
          .build();
      resp.sendRedirect(url);
      return null;
    }
    return credential;
  }
}
