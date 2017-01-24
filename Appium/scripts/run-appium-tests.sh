#!/usr/bin/env bash

path_to_script=$(dirname $0)
${path_to_script}/push-apk-to-sauce.sh

./gradlew -b Appium/build.gradle test
