# mncmd

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![pull_request](https://github.com/otsob/mncmd/actions/workflows/pull_request.yaml/badge.svg)

Command line app for extracting information from music notation files.
Currently supports only reading MusicXML files.

## Requirements

Building `mncmd` requires:

- Java (tested with version 17)
- Clojure (tested with version 1.11)
- Leiningen (tested with version 2.9.8)
- GraalVM and native image (tested with 22.1.0)
- [Dependencies](./project.clj)

## Building/installing mncmd

To build a native executable, run [native-build/build.sh](native-build/build.sh). The executable can be found in the `target` directory. Place the executable where you want to keep it, and create a suitable alias for it.

If there are changes to the code, the Graal native image resource files may need to be regenerated.
This is achieved by setting `export JAVA_HOME=<path/to/your/graalvm/home/dir>` and running [native-build/generate-resource-files.sh](./native-build/generate-resource-files.sh).
Incrementing the version number of `mncmd` is done using [bump2version](https://github.com/c4urself/bump2version/#installation).

## Usage

Type in `mncmd --help` to find out how to use `mncmd`.
