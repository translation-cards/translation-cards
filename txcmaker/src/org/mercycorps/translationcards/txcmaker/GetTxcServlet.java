package org.mercycorps.translationcards.txcmaker;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTxcServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Drive drive = AuthUtils.getDriveService(getServletContext());
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
