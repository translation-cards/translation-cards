#!/bin/bash

EMULATOR_NAME=android23Emulator

$ANDROID_HOME/platform-tools/adb emu kill
$ANDROID_HOME/platform-tools/adb kill-server
$ANDROID_HOME/tools/android delete avd -n $EMULATOR_NAME

