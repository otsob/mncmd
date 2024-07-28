# mncmd

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![main](https://github.com/otsob/mncmd/actions/workflows/main.yaml/badge.svg)

Command line app for extracting information from music notation files.
Currently supports only reading MusicXML files.

## Requirements

Building `mncmd` requires:

- GraalVM and native image (tested with 22.0.2)
- Clojure (tested with version 1.11)

## Building/installing mncmd

To build a native executable, run `clj -T:build native-image`. The executable can be found in the `target` directory.
Place the executable where you want to keep it, and create a suitable alias for it.

If there are changes to the code that change the use of dependencies, the Graal native image resource files may need to be regenerated.
This is achieved by running `clj -T:build graal-config-files`.
Incrementing the version number of `mncmd` can be done using [bump2version](https://github.com/c4urself/bump2version/#installation).

## Usage

Type in `mncmd --help` to find out how to use `mncmd`.
