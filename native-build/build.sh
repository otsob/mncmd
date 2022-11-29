#!/bin/sh

lein uberjar
native-image --report-unsupported-elements-at-runtime \
             --initialize-at-build-time \
             --no-server \
             --no-fallback \
             --allow-incomplete-classpath \
             -H:Log=registerResource: \
             -H:ResourceConfigurationFiles=./native-build/resource-config.json \
             -H:ResourceConfigurationFiles=./native-build/wmn4j-resource-config.json \
             -H:ReflectionConfigurationFiles=./native-build/reflect-config.json \
             -jar ./target/mncmd-0.1.4-standalone.jar \
             -H:Name=./target/mncmd
