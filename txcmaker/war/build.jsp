<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>

<% BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>

<!DOCTYPE html>

<html>

<head>
  <title>Build</title>
</head>

<body>
  <h1>Build a TXC File</h1>

  <form action="<%= blobstoreService.createUploadUrl("/get-txc") %>" method="post" enctype="multipart/form-data">
    <table>
      <tr>
        <td>Deck Name:</td>
        <td><input type="text" name="deckName" /></td>
      <tr>
        <td>Spec file:</td>
        <td><input type="file" name="specFile"></td>
      </tr>
      <tr>
        <td><input type="submit" value="Submit" /></td>
      </tr>
    </table>
  </form>
</body>

</html>
