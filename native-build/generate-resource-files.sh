#!/bin/sh

clj -T:build uberjar

MNCMD=./target/mncmd-0.2.3-standalone.jar

java -agentlib:native-image-agent=config-merge-dir=./native-build/ -jar \
                    $MNCMD stat ./test/resources/multistaff_test_file.musicxml \
                    --counts --parts --ambitus --key-signature --time-signature --chroma --chroma-plot

java -agentlib:native-image-agent=config-merge-dir=./native-build/ -jar \
                    $MNCMD lib create ./target/tmp-lib

java -agentlib:native-image-agent=config-merge-dir=./native-build/ -jar \
                    $MNCMD lib add ./target/tmp-lib ./test/resources/multistaff_test_file.musicxml

java -agentlib:native-image-agent=config-merge-dir=./native-build/ -jar \
                    $MNCMD lib remove ./target/tmp-lib ./target/tmp-lib/TestFile\ Composer/Multistaff\ test\ file.musicxml

clj -T:build clean
