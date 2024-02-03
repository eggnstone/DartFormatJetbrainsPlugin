package dev.eggnstone.plugins.jetbrains.dartformat.tools

import com.intellij.openapi.project.Project

data class NotificationInfo(
    val project: Project,
    val subtitle: String? = null,
    val linkTitle: String? = null,
    val linkUrl: String? = null
)
