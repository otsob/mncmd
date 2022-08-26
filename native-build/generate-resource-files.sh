#!/bin/sh

# Requires setting JAVA_HOME to GraalVM Home directory

lein uberjar
$JAVA_HOME/bin/java -agentlib:native-image-agent=config-output-dir=./native-build/ -jar ./target/mncmd-0.1.1-standalone.jar stat ./test/resources/multistaff_test_file.musicxml --counts --parts --ambitus --key-signature
