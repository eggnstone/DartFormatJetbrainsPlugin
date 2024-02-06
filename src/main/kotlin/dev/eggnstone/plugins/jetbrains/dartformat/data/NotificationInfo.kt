package dev.eggnstone.plugins.jetbrains.dartformat.data

import com.intellij.openapi.project.Project

data class NotificationInfo(
    val content: String?,
    val fileName: String?,
    val links: List<LinkInfo>?,
    val origin: String?,
    val project: Project?,
    val title: String
)
