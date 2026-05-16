# TODO
- Make it an option: stop when error encountered
- Show error when data received on stdin
- Check for process death while processing
- Notification inside the editor like original including hint to open settings
- Add setting for how many warnings to show (currently 1)
- Add "Change shortcut action" to settings
- Add welcome message / Hint: press <hotkey> to format ...
- Separate config from settings
- Handle unexpected texts in StdOut
- Use original keyboard shortcut
- Expand patterns of files to ignore
- Make ignore patterns editable
- Project based configs
- Update screenshots (fixe spaces)

# DONE
- Add plugin and dart_format versions to reports
- Report operating system details
- Add link to VSCode extension
- Don't send empty text, test result for empty text
- Install and update and auto-update for dart_format (https://pub.dartlang.org/packages/dart_format.json)
- Go to error location
- Icon in shortcut
- Add progress dialog (cancelable modal, file-by-file progress with indicator)
- Auto-recover from "StdErr: kernel binary something" / "Invalid SDK hash" by re-running `dart pub global activate dart_format` once
- Mirror dart_format 2.2.0's log-file lifecycle: rotate at 10 MiB into a sibling `.log.old`, and on startup remove `DartFormatPlugin_*.log` / `.log.old` files in `java.io.tmpdir` older than 30 days. Best-effort: skip files locked by other plugin instances; never touch the current PID's file.
- Recognize Flutter's bootstrap stderr ("Checking Dart SDK version", "Downloading Dart SDK from Flutter engine") during the dart_format handshake. Show a "Flutter is updating its Dart SDK" info notification once per startup attempt, and swap the misleading "did you install dart_format?" hint in the failure notifications for "wait for Flutter to finish, then retry" when that stderr is present.
