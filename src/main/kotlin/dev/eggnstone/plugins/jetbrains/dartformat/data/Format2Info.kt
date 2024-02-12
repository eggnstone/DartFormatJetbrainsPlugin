package dev.eggnstone.plugins.jetbrains.dartformat.data

data class Format2Info(
    val lastFileName: String?,
    val encounteredError: Boolean,
    val changedFiles: Int
)
