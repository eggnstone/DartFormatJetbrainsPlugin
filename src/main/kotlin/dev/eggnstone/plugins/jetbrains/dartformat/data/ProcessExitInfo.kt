package dev.eggnstone.plugins.jetbrains.dartformat.data

data class ProcessExitInfo(val stdOutTail: String, val stdErrTail: String, val exitCode: Int)
