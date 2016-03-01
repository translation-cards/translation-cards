#!/usr/bin/env bash

echo no | android create avd --force -n test -t android-10 --abi armeabi-v7a
emulator -avd test -no-skin -no-audio -no-window &
./android-wait-for-emulator.sh # see https://github.com/gildegoma/chef-android-sdk/blob/master/files/default/android-wait-for-emulator
adb shell input keyevent 82 &
./gradlew connectedAndroidTest