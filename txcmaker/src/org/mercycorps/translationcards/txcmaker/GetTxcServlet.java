package org.mercycorps.translationcards.txcmaker;

import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTxcServlet extends HttpServlet {

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  private static HttpTransport httpTransport;

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Drive drive = getDriveService();
    resp.getWriter().println("Hello. Here are some filenames.");
    List<File> files = drive.files().list()
        .setMaxResults(10)
        .setFields("nextPageToken, files(id, name)")
        .execute().getItems();
    for (File file : files) {
      resp.getWriter().println(file.getTitle());
    }
  }

  private Drive getDriveService() {
    AppIdentityCredential credential = new AppIdentityCredential(
        Arrays.asList(DriveScopes.DRIVE_READONLY));
    return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName("TXC Maker")
        .build();
  }
}
