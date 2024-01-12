#!/bin/bash

# Access commit SHA instead of tag
tag=${{ github.sha }}

build_type='release'

# Determine flavor based on commit SHA prefix
if [[ $tag == 'uat'* ]]; then
  flavor='uat'
else
  flavor='prd'
fi

# Run Gradle build with flavor and build type
./gradlew "assemble$flavor$build_type"

# Determine APK path and copy to TaskUnity.apk
apkDir="app/build/outputs/apk/$flavor/$build_type"
apkMetaFile="$apkDir/output-metadata.json"
apkFile=$(cat $apkMetaFile | jq -r '.elements[0].outputFile')
cp "$apkDir/$apkFile" "TaskUnity.apk"
