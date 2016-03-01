#!/usr/bin/env bash

echo no | android create avd --force -n test -t android-23 --abi armeabi-v7a
echo booting
emulator -avd test -no-skin -no-audio -no-window -no-boot-anim &
echo waiting
./android-wait-for-emulator.sh # see https://github.com/gildegoma/chef-android-sdk/blob/master/files/default/android-wait-for-emulator
adb shell input keyevent 82 &
./gradlew connectedAndroidTest