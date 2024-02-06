package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.openapi.project.Project

data class NotificationInfo(
    val fileName: String? = null,
    val linkTitle: String? = null,
    val linkUrl: String? = null,
    val origin: String? = null,
    val project: Project? = null,
    val subtitle: String? = null
)
