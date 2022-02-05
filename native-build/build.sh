#!/bin/sh

lein uberjar
native-image --report-unsupported-elements-at-runtime \
             --initialize-at-build-time \
             --no-server \
             --allow-incomplete-classpath \
             -H:Log=registerResource: \
             -H:ResourceConfigurationFiles=./native-build/resource-config.json \
             -H:ReflectionConfigurationFiles=./native-build/reflect-config.json \
             -jar ./target/mncmd-0.1.0-SNAPSHOT-standalone.jar \
             -H:Name=./target/mncmd
