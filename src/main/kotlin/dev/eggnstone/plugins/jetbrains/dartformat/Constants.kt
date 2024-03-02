package dev.eggnstone.plugins.jetbrains.dartformat

class Constants
{
    companion object
    {
        private const val DEBUG = true

        const val CANCEL_PROCESSING_ON_ERROR = !DEBUG

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_FORMAT_ACTION = DEBUG && false

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_NOTIFICATION_TOOLS = DEBUG && false

        @Suppress("SimplifyBooleanWithConstants", "KotlinConstantConditions")
        const val DEBUG_SETTINGS_DIALOG = DEBUG && false

        const val HTTP_CLIENT_CONNECT_TIMEOUT_IN_SECONDS = 5
        const val HTTP_CLIENT_CONNECTION_REQUEST_TIMEOUT_IN_SECONDS = 5

        const val REPO_NAME_DART_FORMAT = "DartFormat"
        const val REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN = "DartFormatJetBrainsPlugin"

        const val SHOW_OPEN_FILE_IN_NOTIFICATION = DEBUG
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
