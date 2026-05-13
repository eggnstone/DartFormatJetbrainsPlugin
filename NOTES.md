# Notes

Long-lived gotchas and design decisions for this plugin. Not a TODO list — entries here describe how things work, not work that's pending.

## Notification balloons render HTML

IntelliJ's `Notification` content is rendered as HTML. `StringTools.toTextWithHtmlBreaks` (used by `NotificationTools.notifyByToolWindowBalloon` for the balloon body) converts newlines to `<br/>` but does **not** escape HTML metacharacters. As a result:

- Any literal `<...>` in the title or content is interpreted as an HTML tag and stripped from view.
- `&` and `>` are also unsafe.

Implications:

- Placeholder strings shown in notifications use round brackets, not angle brackets: `(unknown version)`, `(no response)`, etc.
- Stderr / stdout text from `dart_format` or shell shims may contain `<` or `>` characters. If those are ever piped into a balloon body (currently they're only piped into the prefilled GitHub report body, which is plain Markdown text on github.com and renders correctly), they'd disappear from the balloon.
- The plugin intentionally emits `<pre>dart pub global activate dart_format</pre>` in some notification bodies. That's why we can't just escape everything in `toTextWithHtmlBreaks` — doing so would break those intentional HTML fragments.

If a future change needs to surface untrusted text in a balloon, escape it at the call site (`text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")`) rather than centrally.
