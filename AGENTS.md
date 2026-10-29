# AGENTS.md

Clojure CLI app that reads MusicXML scores, prints statistics, and manages a
SQLite-backed score library. Shipped as a GraalVM native image. `README.md` covers
user-facing usage. This file covers working on the code.

## Commands

Run from the repo root.

- Lint: `clj-kondo --lint src test deps.edn build.clj`
- Test all: `clojure -X:test`
- Test one namespace: `clojure -X:test :nses '[mncmd.score-test]'`
- Test one var: `clojure -X:test :vars '[mncmd.score-test/test-counts]'`
- Smoke test: `./target/mncmd stat test/resources/multistaff_test_file.musicxml`

PR CI runs lint, then `clojure -X:test`, then `clj -T:build native-image` plus the
smoke test. A change is not verified until the native build passes.

## Build and versioning

- `clj -T:build native-image` produces `target/mncmd`. It needs GraalVM
  (`.sdkmanrc` pins `22.0.2-graalce`, enable with `sdk env`).
- `clj -T:build uberjar` builds the jar only. Use it for quick checks.
- `clj -T:build graal-config-files` regenerates the checked-in `native-build/*.json`
  reflection and resource config by running the app under the native-image agent.
  Run it only when dependency usage changes, and commit the regenerated files.
- The version lives in three places: `build.clj`, `mncmd.core/app-config`, and
  `.bumpversion.cfg`. Change it with `bumpversion`, never by hand. CI bumps the patch
  version on every push to `main`, so do not bump it in a PR.

## Native-image constraints

These break the native build but not the JVM run, so they are easy to miss.

- Every namespace carries `(:gen-class)`. Keep that convention, because the build
  relies on `clj_easy.graal_build_time.InitClojureClasses`.
- `build.clj` passes fixed `--initialize-at-build-time` and
  `--initialize-at-run-time` arguments. A new dependency may need its packages added
  there, and `clojure.java.jdbc__init` must stay initialization-at-run-time.
