@~/.claude/flutter-conventions.md

# CLAUDE.md

## Project

JetBrains IDE plugin (Kotlin) for IntelliJ IDEA, Android Studio, etc. that acts as a client for the [`dart_format`](https://pub.dev/packages/dart_format) pub.dev package. The plugin talks HTTP to a local `dart_format` server (installed via `dart pub global activate dart_format`) to format Dart code from inside the IDE.

- Language: **Kotlin**. Entry point: `src/main/kotlin/dev/eggnstone/plugins/jetbrains/dartformat/`, built with Gradle + IntelliJ Platform Plugin SDK.
- Communication with the formatter happens in `plugin/DartFormatClient.kt` via Apache HttpClient.
- Marketplace: <https://plugins.jetbrains.com/plugin/21003-dartformat>.
- Sibling project: VSCode extension (same purpose, different IDE) — <https://github.com/eggnstone/DartFormatVSCodePlugin>.
- Formatter source: <https://github.com/eggnstone/dart_format>.
