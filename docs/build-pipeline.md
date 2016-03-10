# Build Pipeline and Artifact Repository
[Build Pipeline](https://snap-ci.com/translation-cards/translation-cards/branch/master)

[Hosted APK](http://translation-cards.s3-website-us-east-1.amazonaws.com/app-release.apk)

# Snap CI
We use [Snap CI](https://snap-ci.com/) for our build pipeline. We chose this because it is very easy to set up, maintain, and is well documented.

The pipeline will trigger when a commit is pushed to the master branch. This will run our tests against our application, and give you the option in the end to manually trigger an upload of the APK to an artifact repository.

# Build Stages
### Android Initialize
This stage downloads the Android SDK, support libraries, and adds gradle as the build tool for the project.

### Assemble
This stage checks to see if our Android source code compiles.

### Test
This stage runs our unit tests.

### Sign APK
This stage generates a signed apk using our keystore for the Google Play store.

### S3Upload
This is an optional and manually triggered stage that uploads our apk to an artifact repository (S3).
