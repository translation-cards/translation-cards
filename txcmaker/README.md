# TXC Maker

## Building and Running

Although you can run the app locally, it can't do anything interesting without connecting to Google Drive. That requires OAuth, which requires running on AppEngine (so you can give Drive an OAuth callback address).

Requirements:
*  Maven 3.1 or higher.

To get the app up and running on AppEngine:
1.  Register for an app on AppEngine.
2.  Register the app for the Drive API, and enable OAuth2 for your web app (when it asks for a callback URL, put in http://<APP-ID>.appspot.com/oauth2callback). Put the client_secrets.json file at src/main/webapp/WEB-INF/client_secrets.json (**don't** check that file in though; they're secrets, after all).
3.  Put your app ID in pom.xml in place of the string "APP-ID-HERE" (optionally, also set a different version).
4.  Run "mvn appengine:update".
