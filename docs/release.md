# Release Process

## Updating Version Number

- Increment the versionCode in app/build.gradle (note that this value is just an integer that goes up with each release).
- Update the versionName in app/build.gradle.
- Update the version number in FEEDBACK_URL in MyDecksActivity.java (end of the string).

## Tagging

After all code changes have been made, tag it (something like "git tag -a v1.2.3").
