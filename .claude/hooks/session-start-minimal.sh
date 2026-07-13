#!/bin/bash
# SessionStart hook for Claude Code on the web - NON-DART repos (Kotlin, TypeScript, ...).
# Managed by /project-sync.
#
# A fresh cloud container has an empty ~/.claude, so every `@~/.claude/...` import
# in this repo's CLAUDE.md would dangle SILENTLY and the session would run with no
# conventions at all. This wires the sibling `Claude` config repo into place.
#
# It does NOT install a toolchain - use session-start.sh for Dart/Flutter repos.
#
# Web only: on a local checkout CLAUDE_CODE_REMOTE is unset and this exits at once.
# Repos arrive as SIBLINGS (selected in the web repo-picker), so paths are derived
# from $CLAUDE_PROJECT_DIR's parent. Nothing is hardcoded.
set -euo pipefail

if [ "${CLAUDE_CODE_REMOTE:-}" != "true" ]; then
  exit 0
fi

PROJECT_DIR="${CLAUDE_PROJECT_DIR:-$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)}"
SIBLINGS="$(dirname "$PROJECT_DIR")"

CLAUDE_REPO=""
for candidate in "$SIBLINGS/Claude" "$SIBLINGS/claude"; do
  [ -f "$candidate/CLAUDE.md" ] && CLAUDE_REPO="$candidate" && break
done

if [ -z "$CLAUDE_REPO" ]; then
  echo "SessionStart: WARNING - no sibling 'Claude' repo found." >&2
  echo "  Every '@~/.claude/...' import in this project's CLAUDE.md will resolve to" >&2
  echo "  NOTHING and the session runs without conventions. Add the Claude repo in" >&2
  echo "  the repo picker and restart the session." >&2
  exit 0
fi

mkdir -p "$HOME/.claude/commands"

# Link a file only if the target is absent, or is itself a stale symlink.
# A real file already there is the harness's - leave it.
link_file()
{
  local src="$1" target="$2"
  [ -f "$src" ] || return 0
  if [ -e "$target" ] && [ ! -L "$target" ]; then
    return 0
  fi
  ln -sfn "$src" "$target"
}

for f in "$CLAUDE_REPO"/*.md; do
  link_file "$f" "$HOME/.claude/$(basename "$f")"
done
for f in "$CLAUDE_REPO"/commands/*.md; do
  link_file "$f" "$HOME/.claude/commands/$(basename "$f")"
done
if [ -d "$CLAUDE_REPO/templates" ] && [ ! -e "$HOME/.claude/templates" ]; then
  ln -sfn "$CLAUDE_REPO/templates" "$HOME/.claude/templates"
fi

echo "SessionStart: global Claude config linked from $CLAUDE_REPO"
