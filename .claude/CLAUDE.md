@~/.claude/coding-conventions.md
@~/.claude/kotlin-conventions.md

# DartFormatJetBrainsPlugin

JetBrains IDE plugin (Kotlin) for IntelliJ IDEA, Android Studio, etc. — a client for the [`dart_format`](https://pub.dev/packages/dart_format) pub.dev package. The plugin talks HTTP to a local `dart_format` server (installed via `dart pub global activate dart_format`) to format Dart code from inside the IDE.

- Language: **Kotlin**.
- Build: Gradle + IntelliJ Platform Plugin SDK (`gradle-intellij-plugin`).
- Marketplace: <https://plugins.jetbrains.com/plugin/21003-dartformat>.
- Sibling project: VSCode extension — <https://github.com/eggnstone/DartFormatVSCodePlugin>.
- Formatter source: <https://github.com/eggnstone/dart_format>.

## Project context

- Stability: feature-stable.
- Breaking changes: none within a major version.
- Coupling — critical: this plugin's reason for existing is to wrap the `dart_format` HTTP wire protocol. Any change in the dart_format CLI flags or `POST /format` request/response shape requires a lockstep update here. Conversely, this plugin must never depend on dart_format internals — only the CLI and wire protocol.
- Sibling parity: features added in this plugin should usually also land in the VSCode sibling, and vice versa. Naming, settings, and behavior should match where possible to keep user mental models aligned.
- Public surface: the plugin's "public API" is the user-facing actions, settings UI, and `plugin.xml` action/extension declarations. Breaking changes to those affect users and require a major version bump.

## Architecture

Entry point: `src/main/kotlin/dev/eggnstone/plugins/jetbrains/dartformat/`.

Key components:

- `plugin/DartFormatClient.kt` — Apache HttpClient wrapper that talks to the local `dart_format` server. Owns request construction, response parsing, error mapping, and timeout handling. **This is the protocol boundary.** Any wire-protocol change starts and ends here.
- [Actions/ — Format-on-save action, Format-selection action, etc. — fill in the actual folder names]
- [Settings/ — plugin settings UI, persisted config — fill in]
- [Listeners/ — IDE event hooks if any — fill in]
- [Process/ — dart_format server lifecycle management (spawn, port discovery, shutdown) — fill in]

`src/main/resources/META-INF/plugin.xml` — JetBrains plugin descriptor. Declares actions, IntelliJ Platform version range (`<idea-version since-build="..." until-build="..."/>`), platform dependencies, and extension points. Any new action or extension lands here.

`build.gradle.kts` — IntelliJ Platform plugin Gradle config. Declares the IDE version to build against, Kotlin version, plugin dependencies.

### Build and run

- `./gradlew runIde` — launches a sandboxed IDE instance with the plugin installed. Primary dev loop. It does **not** produce a publishable artifact — it only lays out a sandbox.
- `./gradlew clean buildPlugin` — produces the marketplace-ready zip in `build/distributions/`.
- `./gradlew publishPlugin` — uploads to JetBrains Marketplace (requires a marketplace token; never commit it).
- `./gradlew verifyPlugin` — runs the JetBrains Plugin Verifier against the declared IDE version range; surfaces API compatibility issues before users hit them.

**The upload artifact is `build/distributions/<name>-<ver>.zip` — NEVER a bare jar from `build/libs/`.** The zip bundles the *instrumented* plugin jar plus the pinned `kotlin-stdlib`, `annotations` and `searchableOptions`; a bare `build/libs/*.jar` has none of them, and the plain `*.jar` there is the *non*-instrumented one. "Instrumented" does not mean a debug build — it means post-compile `@NotNull`/`@Nullable` runtime-check weaving, i.e. it *is* the production jar. Uploading the bare jar has been done before by mistake.

### Testing

[Fill in: unit tests with `LightPlatformCodeInsightFixtureTestCase` / `BasePlatformTestCase` in `src/test/kotlin/`? Manual sandbox testing via `runIde`? Both?]

## Recurring tasks

### When the `dart_format` wire protocol changes

1. Read the dart_format changelog for the new release.
2. Update `plugin/DartFormatClient.kt` to match the new request/response shape (multipart parts, JSON fields, status codes).
3. If the CLI flags changed (e.g. `--port`, `--web`), update the server-spawn logic accordingly.
4. Bump the minimum required dart_format version in the plugin's user-facing docs / settings hint.
5. Test against a freshly-activated dart_format from pub.dev: `dart pub global activate dart_format` then `./gradlew runIde`.
6. Mirror the same wire-protocol fix in the VSCode sibling plugin in the same release window.

### When publishing a new version

1. Bump the version in `build.gradle.kts` (or wherever it lives).
2. Update `CHANGELOG.md` — past-tense, brief, per the global changelog style.
3. `./gradlew verifyPlugin` — must pass before upload.
4. `./gradlew buildPlugin` — verify the zip locally.
5. `./gradlew publishPlugin` (or upload `build/distributions/*.zip` manually via marketplace.jetbrains.com).
6. JetBrains Marketplace review lag is typically ~1–2 days. Don't announce the release until it's approved and visible to users.
7. Tag the release in Git after marketplace approval.
8. If this release tracks a new dart_format version, note the minimum compatible dart_format version in the marketplace description.

### When the IntelliJ Platform version range changes

1. Update `<idea-version since-build="..." until-build="..."/>` in `plugin.xml`.
2. Update the `intellij { version = "..." }` block in `build.gradle.kts` to build against the lowest still-supported version (so newer-API calls fail at compile time, not at user runtime).
3. Run `./gradlew verifyPlugin` against the new range — it tests against every IDE version in the declared range.
4. Manual smoke test in the oldest and newest declared versions via `runIde`.
5. If dropping support for an older IDE version, note it prominently in the changelog — users on those IDEs will stop getting updates.

### When the VSCode sibling plugin gets a new feature

1. Determine if the feature is wire-protocol-driven (lives in `dart_format`) or plugin-only (UI/UX).
2. If wire-protocol: it's automatically available here once `DartFormatClient.kt` handles the new shape — just expose it via an action or setting.
3. If plugin-only: re-implement the equivalent action/setting in Kotlin, matching the VSCode version's naming, default behavior, and settings keys where reasonable.
4. Update both plugin marketplaces in the same release window so users on either IDE see the feature land together.

### When debugging "can't connect to dart_format server" reports

1. Reproduce: `dart pub global activate dart_format` + `./gradlew runIde`.
2. Check the server-spawn logs — `dart_format --web` prints a JSON line with the chosen port on stdout. The plugin must read that line, not assume a port.
3. Common causes: stale background `dart_format` process holding a port (kill via `/quit` endpoint or OS-level), PATH issues finding the `dart` executable on Windows vs macOS, firewall blocking 127.0.0.1 traffic (rare but happens on locked-down corp machines).
4. If the request shape changed in a recent dart_format release, the symptom looks like a connection error but is actually a 4xx response — check `DartFormatClient.kt` error mapping.