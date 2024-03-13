package dev.eggnstone.plugins.jetbrains.dartformat.data

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

data class NotificationInfo(
    val content: String?,
    val links: List<LinkInfo>?,
    val origin: String?,
    val project: Project?,
    val title: String,
    val virtualFile: VirtualFile?
)
