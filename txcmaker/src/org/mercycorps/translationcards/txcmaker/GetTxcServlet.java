package org.mercycorps.translationcards.txcmaker;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTxcServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    UserService userService = UserServiceFactory.getUserService();
    String userId = userService.getCurrentUser().getUserId();
    AuthorizationCodeFlow flow = AuthUtils.newFlow(getServletContext());
    Credential credential = flow.loadCredential(userId);
    if (credential == null) {
      String url = flow.newAuthorizationUrl()
          .setRedirectUri(AuthUtils.getRedirectUri(req))
          .build();
      resp.sendRedirect(url);
      return;
    }
    Drive drive = AuthUtils.getDriveService(credential);
    resp.getWriter().println("Hello. Here are some files. Or not. Who knows.");
    List<File> files = drive.files().list()
        .setMaxResults(10)
        .setFields("nextPageToken, items(id, title)")
        .execute().getItems();
    for (File file : files) {
      resp.getWriter().println(file.getTitle());
    }
  }
}
