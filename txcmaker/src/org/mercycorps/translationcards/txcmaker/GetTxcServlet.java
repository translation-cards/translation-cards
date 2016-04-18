package org.mercycorps.translationcards.txcmaker;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.servlet.auth.oauth2.AbstractAuthorizationCodeServlet;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.extensions.appengine.auth.oauth2.AppIdentityCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTxcServlet extends AbstractAuthorizationCodeServlet {

  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
  private static final DataStoreFactory DATA_STORE_FACTORY =
      AppEngineDataStoreFactory.getDefaultInstance();

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Drive drive = getDriveService();
    if (drive == null) {
      resp.getWriter().println("Error connecting to Drive.");
      return;
    }
    resp.getWriter().println("Hello. Here are some filenames.");
    List<File> files = drive.files().list()
        .setMaxResults(10)
        .setFields("nextPageToken, items(id, title)")
        .execute().getItems();
    for (File file : files) {
      resp.getWriter().println(file.getTitle());
    }
  }

  @Override
  protected String getRedirectUri(HttpServletRequest req) throws ServletException, IOException {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/oauth2callback");
    return url.build();
  }

  private Drive getDriveService() {
    HttpTransport httpTransport;
    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
    } catch (GeneralSecurityException e) {
      return null;
    } catch (IOException e) {
      return null;
    }
    Collection<String> scopes = Arrays.asList(DriveScopes.DRIVE_READONLY);
    Credential credential;
    try {
        credential = getCredential(httpTransport, scopes);
    } catch (IOException e) {
      return null;
    }
    return new Drive.Builder(httpTransport, JSON_FACTORY, credential)
        .setApplicationName("TXC Maker")
        .build();
  }

  private Credential getCredential(HttpTransport httpTransport, Collection<String> scopes)
      throws IOException {
    InputStream in = GetTxcServlet.class.getResourceAsStream("/drive_client_secrets.json");
    GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    GoogleAuthorizationCodeFlow flow =
        new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, secrets, scopes)
        .setDataStoreFactory(DATA_STORE_FACTORY)
        .setAccessType("offline")
        .build();
  }
}
