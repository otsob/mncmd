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
- GraalVM and native image (tested with 21.3.0)

## Building/installing mncmd

To build a native executable, run [native-build/build.sh](native-build/build.sh). The executable can be found in the `target` directory. Place the executable where you want to keep it, and create a suitable alias for it.

## Usage

Type in `mncmd --help` to find out how to use `mncmd`.
