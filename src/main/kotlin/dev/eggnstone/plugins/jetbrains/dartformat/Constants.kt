package dev.eggnstone.plugins.jetbrains.dartformat

class Constants
{
    companion object
    {
        // Main switch for debug. Must be "false" for production.
        const val DEBUG = false

        // Fakes

        // Simulates "Can't load Kernel binary: Invalid kernel binary format version." after each
        // dart_format start, so the auto-recovery branch and its single-shot loop guard can both
        // be observed in one IDE session.
        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_FAKE_KERNEL_MISMATCH = DEBUG && false

        // Pretends a newer dart_format version is available, so the auto-update branch fires on
        // first iteration, and the loop guard skips it on the second.
        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_FAKE_NEW_VERSION = DEBUG && false

        // Inserts a cancelable 5-second wait before formatting the first file, so the modal
        // progress dialog stays visible long enough to click "Cancel" for testing.
        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_FAKE_FORMAT_DELAY = DEBUG && false

        // Injects Flutter's "Checking Dart SDK version..." / "Downloading Dart SDK from Flutter
        // engine ..." stderr lines into the handshake read loop before the real dart_format spawn
        // emits its JSON. Lets the SDK-bootstrap detection + one-shot info notification be
        // exercised without actually clearing engine-dart-sdk.stamp and re-downloading the SDK.
        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_FAKE_FLUTTER_SDK_DOWNLOAD = DEBUG && false

        // Forces the first-run welcome notification on every project open, ignoring (and not
        // updating) the persisted welcomeShown flag, so the keymap-aware message and Open Settings
        // link can be iterated on without resetting state between runs.
        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_FAKE_SHOW_WELCOME = DEBUG && false

        // Logging

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val LOG_VERBOSE = DEBUG && false

        // Debugging

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_COLLECT_VIRTUAL_FILES_ITERATOR = DEBUG && false

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_CONFIG = DEBUG && false

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_CONNECTION = DEBUG && false

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_FORMAT_ACTION = DEBUG && false

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_NOTIFICATION_TOOLS = DEBUG && false

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_SETTINGS_DIALOG = DEBUG && false

        // Other

        const val HTTP_CLIENT_CONNECT_TIMEOUT_IN_SECONDS = 5
        const val HTTP_CLIENT_CONNECTION_REQUEST_TIMEOUT_IN_SECONDS = 5

        // dart_format's web service rejects POSTs whose Content-Length exceeds 4 MiB.
        // Pre-check the input on this side so we fail fast with a helpful message
        // instead of streaming a doomed upload.
        const val MAX_FORMAT_INPUT_BYTES = 4 * 1024 * 1024

        const val MAX_STACK_TRACE_LENGTH = 5000

        const val REPO_NAME_DART_FORMAT = "DartFormat"
        const val REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN = "DartFormatJetBrainsPlugin"

        const val SHOW_SLOW_TIMINGS = DEBUG
        const val SHOW_TIMINGS_EVEN_AFTER_ERROR = DEBUG

        const val WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS = -1
        const val WAIT_FOR_JOIN_JOB_FORMAT_COMMAND_IN_SECONDS = 60
        const val WAIT_FOR_SEND_JOB_FORMAT_COMMAND_IN_SECONDS = 5
        const val WAIT_FOR_SEND_JOB_QUIT_COMMAND_IN_SECONDS = 5
        const val WAIT_INTERVAL_IN_MILLIS = 100

        val HTTP_CLIENT_SOCKET_TIMEOUT_IN_SECONDS = 30
            .coerceAtLeast(WAIT_FOR_JOIN_JOB_FORMAT_COMMAND_IN_SECONDS)
            .coerceAtLeast(WAIT_FOR_SEND_JOB_FORMAT_COMMAND_IN_SECONDS)
            .coerceAtLeast(WAIT_FOR_SEND_JOB_QUIT_COMMAND_IN_SECONDS)
    }
}
