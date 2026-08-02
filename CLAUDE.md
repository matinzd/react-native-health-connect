# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

`react-native-health-connect` — an Android-only React Native wrapper around
`androidx.health.connect:connect-client`.

## Detailed docs

Read the relevant one before working in that area; they hold the context that requires reading
many files to reconstruct.

| Doc | When to read it |
| --- | --- |
| [.claude/docs/architecture.md](.claude/docs/architecture.md) | Any native change. Layering, the old/new-arch spec split, the record adapter pattern, error-code mapping. |
| [.claude/docs/adding-a-record-type.md](.claude/docs/adding-a-record-type.md) | Adding or modifying a Health Connect record type — a pattern repeated across three Kotlin registries and three TS unions. |
| [.claude/plans/activity-result-without-main-activity.md](.claude/plans/activity-result-without-main-activity.md) | Anything touching permissions or the exercise-route dialog. Includes the approach that looks correct but crashes on Android 14+. |
| [.claude/docs/expo-and-linking.md](.claude/docs/expo-and-linking.md) | Touching `app.plugin.js`, `example-expo/`, or the CI linking jobs. |

## Commands

```sh
yarn                        # root install (workspaces: example, docs — NOT example-expo)
yarn lint                   # eslint ./src   (--fix to format)
yarn typecheck              # tsc --noEmit
yarn test                   # jest
yarn test -t "<name>"       # single test by name
yarn prepack                # build the package with react-native-builder-bob

yarn example start          # Metro
yarn example android        # bare example
ORG_GRADLE_PROJECT_newArchEnabled=true yarn example android   # new arch
yarn clean                  # remove build dirs (required when switching architecture)
```

Expo example (separate project, see the Expo doc):

```sh
cd example-expo && yarn install && npx expo prebuild --clean --platform android && npx expo run:android
```

Kotlin-only iteration is faster through the example's Gradle wrapper:

```sh
cd example/android && ./gradlew :react-native-health-connect:compileDebugKotlin
```

## Conventions

- Conventional commits (`feat:`, `fix:`, `refactor:`, `docs:`, `test:`, `chore:`) — enforced by a
  commitlint pre-commit hook, and the changelog/release is generated from them.
- Kotlin in this repo uses 2-space indent, matching the TS side.
- Adding a native method means touching four files: `src/NativeHealthConnect.ts`,
  `android/src/oldarch/HealthConnectSpec.kt`, `android/src/newarch/HealthConnectSpec.kt`, and
  `HealthConnectModule.kt` (which should only delegate to `HealthConnectManager`).
- User-facing docs live in two places that drift easily: `README.md` and the Docusaurus site under
  `docs/docs/`. Update both.
