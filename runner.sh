#!/usr/bin/env bash

appPath=build/distributions/kaminski-stemmer*/bin/kaminski-stemmer

if ! [ -a $appPath ] ;then
    ./gradlew distZip
    pushd build/distributions
    unzip kaminski-stemmer-*.zip
    popd
fi


$appPath "$@"
