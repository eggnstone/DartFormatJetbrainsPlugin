# ProjectSync

The contract between this repo and `/project-sync`. One per repo — never one per package.

## Settings

| Key | Value | Notes |
|---|---|---|
| `visibility` | `public` | GitHub + JetBrains Marketplace. **Every file here is read by strangers**: no local paths, no personal or business context, no prose referencing private config. The `@~/.claude/…` import lines are fine — directives, not narrative. |
| `asyncGitMode` | `ignored` | **Mandatory for a public repo** — committing `.async/` would publish raw thought-dumps. Consequence: async state does not sync PC↔laptop here. Correct trade. |
| `projectType` | `kotlin` | Gradle / IntelliJ Platform plugin. |
| `packages` | `.` (kotlin) | Single Gradle project. |

## Deviations

- **Uses `session-start-minimal.sh`, not the Dart hook.** No Dart/Flutter toolchain is
  needed; the hook only wires the sibling `Claude` repo into `~/.claude` so the
  `@~/.claude/…` imports resolve in web sessions. Without it a web session silently
  runs with no conventions at all.

- **No `analysis_options.yaml`, no lints, no conventions plugin.** Kotlin project —
  the Dart baseline does not apply. Conventions come from
  `@~/.claude/kotlin-conventions.md`, enforced by review, not by an analyzer.

- **Sibling-parity rule with `DartFormatVSCodePlugin`.** Features land in both plugins
  in the same release window; naming, settings and behaviour should match. Neither is
  synced from the other by `/project-sync` — that is a human decision.
