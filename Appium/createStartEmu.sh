#!/bin/bash

EMULATOR_NAME=android23Emulator
TARGET_NAME=android-23
#TARGET_ABI=x86
TARGET_ABI=default/x86_64

echo "y" | $ANDROID_HOME/tools/android update sdk -a --no-ui --filter 75
echo no | $ANDROID_HOME/tools/android create avd -f -n $EMULATOR_NAME -t $TARGET_NAME --abi $TARGET_ABI
$ANDROID_HOME/platform-tools/adb start-server
$ANDROID_HOME/tools/emulator @$EMULATOR_NAME -no-window &
./wait-for-emulator.sh

exit