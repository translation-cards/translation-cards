package org.mercycorps.translationcards.txcmaker;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetTxcServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
    List<BlobKey> blobKeys = blobs.get("specFile");
    if (blobKeys == null || blobKeys.isEmpty()) {
      res.sendRedirect("/");
    } else {
      res.sendRedirect("/serve?blob-key=" + blobKeys.get(0).getKeyString());
    }
    resp.getWriter().println("Hello. Here is your TXC.");
  }
}
