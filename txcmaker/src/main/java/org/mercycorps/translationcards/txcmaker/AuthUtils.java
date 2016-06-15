package org.mercycorps.translationcards.txcmaker;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

class AuthUtils {

  private static final AppEngineDataStoreFactory DATA_STORE_FACTORY =
      AppEngineDataStoreFactory.getDefaultInstance();
  
  static final HttpTransport HTTP_TRANSPORT = new UrlFetchTransport();

  static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  static final Collection<String> SCOPES = Arrays.asList(
      DriveScopes.DRIVE_READONLY, DriveScopes.DRIVE_FILE);

  private static final String CLIENT_SECRETS_FILENAME = "/WEB-INF/client_secrets.json";

  static String getRedirectUri(HttpServletRequest req) {
    GenericUrl url = new GenericUrl(req.getRequestURL().toString());
    url.setRawPath("/oauth2callback");
    return url.build();
  }

  static Drive getDriveOrOAuth(
      ServletContext context, HttpServletRequest req, HttpServletResponse resp,
      String userId, boolean orOAuth)
      throws IOException {
    AuthorizationCodeFlow flow = AuthUtils.newFlow(context);
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

  static AuthorizationCodeFlow newFlow(ServletContext context) throws IOException {
    InputStream in = context.getResourceAsStream(CLIENT_SECRETS_FILENAME);
    GoogleClientSecrets secrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
    return new GoogleAuthorizationCodeFlow.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, secrets, SCOPES)
        .setDataStoreFactory(DATA_STORE_FACTORY)
        .setAccessType("offline")
        .build();
  }

  static Drive getDriveService(Credential credential) throws IOException {
    return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName("TXC Maker")
        .build();
  }
}
