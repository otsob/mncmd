#!/bin/sh

# Requires setting GRAALVM_HOME to GraalVM Home directory
clj -T:build uberjar

MNCMD=./target/mncmd-0.2.1-standalone.jar

$GRAALVM_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./native-build/ -jar \
                    $MNCMD stat ./test/resources/multistaff_test_file.musicxml \
                    --counts --parts --ambitus --key-signature --time-signature --chroma --chroma-plot

$GRAALVM_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./native-build/ -jar \
                    $MNCMD lib create ./target/tmp-lib

$GRAALVM_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./native-build/ -jar \
                    $MNCMD lib add ./target/tmp-lib ./test/resources/multistaff_test_file.musicxml

$GRAALVM_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./native-build/ -jar \
                    $MNCMD lib remove ./target/tmp-lib ./target/tmp-lib/TestFile\ Composer/Multistaff\ test\ file.musicxml

clj -T:build clean
