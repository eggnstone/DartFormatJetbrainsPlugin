package dev.eggnstone.plugins.jetbrains.dartformat

class Constants
{
    companion object
    {
        const val CANCEL_PROCESSING_ON_ERROR = true

        const val DEBUG_FORMAT_ACTION = false
        const val DEBUG_NOTIFICATION_TOOLS = false
        const val DEBUG_SETTINGS_DIALOG = false

        const val HTTP_CLIENT_CONNECT_TIMEOUT_IN_SECONDS = 5
        const val HTTP_CLIENT_CONNECTION_REQUEST_TIMEOUT_IN_SECONDS = 5

        const val REPO_NAME_DART_FORMAT = "DartFormat"
        const val REPO_NAME_DART_FORMAT_JET_BRAINS_PLUGIN = "DartFormatJetBrainsPlugin"

        const val SHOW_TIMINGS_AFTER_ERROR = false
        const val SHOW_SLOW_TIMINGS = false

        const val WAIT_INTERVAL_IN_MILLIS = 100
        const val WAIT_FOR_FORMAT_IN_SECONDS = 60
        const val WAIT_FOR_EXTERNAL_DART_FORMAT_START_IN_SECONDS = -1

        val HTTP_CLIENT_SOCKET_TIMEOUT_IN_SECONDS = 30.coerceAtLeast(WAIT_FOR_FORMAT_IN_SECONDS)
    }
}
