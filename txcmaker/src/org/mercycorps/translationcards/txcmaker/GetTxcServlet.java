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

public class GetTxcServlet extends HttpServlet {

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
    String userid = getUserId();
    Queue queue = QueueFactory.getQueue("queue-txc-building");
    queue.add(TaskOptions.Builder.withUrl("/tasks/txc-build")
        .param("userid", userid)
        .param("deckName", req.getParameter("deckName"))
        .param("publisher", req.getParameter("publisher"))
        .param("deckId", req.getParameter("deckId"))
        .param("docId", req.getParameter("docId"))
        .param("audioDirId", req.getParameter("audioDirId"))
        .param("licenceUrl", req.getParameter("licenseUrl")));
    resp.getWriter().println("Your file should arrive in Drive shortly.");
  }

  private void displayForm(HttpServletResponse resp) throws IOException {
    resp.getWriter().println(
        "<form method=\"post\">" +
        "<p>Doc ID: <input type=\"text\" name=\"docId\" /><br />" +
        "Audio directory ID: <input type=\"text\" name=\"audioDirId\" /><br />" +
        "Deck name: <input type=\"text\" name=\"deckName\" /><br />" +
        "Publisher: <input type=\"text\" name=\"publisher\" /><br />" +
        "Deck ID: <input type=\"text\" name=\"deckId\" /><br />" +
        "License URL: <input type=\"text\" name=\"licenseUrl\" /><br />" +
        "Locked: <input type=\"checkbox\" name=\"locked\" /><br />" +
        "<input type=\"submit\" /></p>" +
        "</form>"
    );
  }

  private String getUserId() {
    UserService userService = UserServiceFactory.getUserService();
    return userService.getCurrentUser().getUserId();
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
