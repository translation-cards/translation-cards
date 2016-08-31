#!/bin/bash

EMULATOR_NAME=android23Emulator
TARGET_NAME=android-23
TARGET_ABI=google_apis/x86
#TARGET_ABI=armeabi-v7a

echo no | $ANDROID_HOME/tools/android create avd -n $EMULATOR_NAME -t $TARGET_NAME --abi $TARGET_ABI
$ANDROID_HOME/platform-tools/adb start-server
$ANDROID_HOME/tools/emulator @$EMULATOR_NAME -no-window &
$ANDROID_HOME/platform-tools/adb wait-for-device

exit
