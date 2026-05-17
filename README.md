# DartFormat

## A configurable formatter for Dart and Flutter — JetBrains plugin

[![Build](https://github.com/eggnstone/DartFormatJetBrainsPlugin/actions/workflows/gradle.yaml/badge.svg)](https://github.com/eggnstone/DartFormatJetBrainsPlugin/actions)
[![GitHub Issues](https://img.shields.io/github/issues/eggnstone/DartFormatJetBrainsPlugin.svg)](https://github.com/eggnstone/DartFormatJetBrainsPlugin/issues)
[![GitHub Stars](https://img.shields.io/github/stars/eggnstone/DartFormatJetBrainsPlugin.svg)](https://github.com/eggnstone/DartFormatJetBrainsPlugin/stargazers)

A drop-in alternative to `dart format` / `dartfmt` with the same one-shortcut workflow — but you choose the rules.
Works in IntelliJ IDEA, Android Studio, and other JetBrains IDEs on Windows, macOS, and Linux.

**Keeps your line breaks.** No forced wrapping at a line-length limit, no inserted or removed line breaks — unless you explicitly enable an option that adds them. Your layout stays yours.

### What you can configure

- Newlines before / after `{` and `}` (Allman braces)
- Newline after `;` (one statement per line)
- Trailing newline at end of file
- Fix spaces (normalize around operators / keywords)
- Indent width (spaces per level)
- Max consecutive empty lines
- Strip trailing commas

Every option is off by default, so the formatter only changes what you ask it to.

### Shortcuts

- `Ctrl/Cmd`+`Alt`+`,` — format the active file or the selected Dart files/folders
- `Ctrl/Cmd`+`Alt`+`L` — format the selection (Dart via DartFormat, other file types via the built-in formatter)

### How it works

Formatting runs locally — the plugin talks to a `dart_format` server on `localhost`, installed automatically from [pub.dev](https://pub.dev/packages/dart_format).

### Install

JetBrains Marketplace: <https://plugins.jetbrains.com/plugin/21003-dartformat>

### Related

- VS Code extension: <https://marketplace.visualstudio.com/items?itemName=eggnstone.DartFormat> ([source](https://github.com/eggnstone/DartFormatVSCodePlugin))
- `dart_format` package: <https://pub.dev/packages/dart_format> ([source](https://github.com/eggnstone/dart_format))
