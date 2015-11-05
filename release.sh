#!/bin/sh

cd `dirname "$0"`

if [ $# -eq 2 ]; then
	releaseVersion=$1
	newVersion=$2
else
	echo "Bad number of arguments => release.sh <releaseVersion> <newVersion>"
	exit 1
fi

./gradlew clean release -Prelease.useAutomaticVersion=true -PreleaseVersion=${releaseVersion} -PnewVersion=${newVersion}



