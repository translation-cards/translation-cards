#!/bin/bash

# raise an error if any command fails!
set -e

# existance of this file indicates that all dependencies were previously installed, and any changes to this file will use a different filename.
INITIALIZATION_FILE="$ANDROID_HOME/.initialized-dependencies-$(git log -n 1 --format=%h -- $0)"

if [ ! -e ${INITIALIZATION_FILE} ]; then
  # fetch and initialize $ANDROID_HOME
  download-android
  # Use the latest android sdk and platform tools
  echo y | android update sdk -a --filter tools,platform-tools -u > /dev/null

  # The BuildTools version used by your project
  echo y | android update sdk --no-ui --filter build-tools-23.0.2 --all > /dev/null

  # The SDK version used to compile your project
  echo y | android update sdk --no-ui --filter android-23 > /dev/null

  # uncomment to install the Extra/Android Support Library
   echo y | android update sdk --no-ui --filter extra-android-support --all > /dev/null

  # uncomment these if you are using maven/gradle to build your android project
   echo y | android update sdk --no-ui --filter extra-google-m2repository --all > /dev/null
   echo y | android update sdk --no-ui --filter extra-android-m2repository --all > /dev/null

   # Specify at least one system image if you want to run emulator tests
   echo y | android update sdk --no-ui --filter sys-img-armeabi-v7a-android-23 --all > /dev/null

   (wget http://dl.google.com/android/android-sdk_r23-linux.tgz -O - | tar zx -C $ANDROID_HOME --strip-components 1); echo
   echo 'y' | $ANDROID_HOME/tools/android --silent update sdk --no-ui --force --all --obsolete --filter platform-tools

  touch ${INITIALIZATION_FILE}
fi
