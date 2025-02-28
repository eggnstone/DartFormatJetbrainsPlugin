package dev.eggnstone.plugins.jetbrains.dartformat.data

import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.vfs.VirtualFile
import dev.eggnstone.plugins.jetbrains.dartformat.Constants
import dev.eggnstone.plugins.jetbrains.dartformat.tools.Logger

class DataContextWithFiles(private val dataContext: DataContext, private val virtualFileArray: Array<VirtualFile>) : DataContext
{
    override fun getData(dataId: String): Any?
    {
        if (this.dataContext.getData(dataId) == null)
            return null

        if (Constants.DEBUG_DATA_CONTEXT_WITH_FILES) Logger.logDebug("DataContextWithFiles: $dataId")

        if (dataId == "virtualFileArray")
        {
            if (Constants.DEBUG_DATA_CONTEXT_WITH_FILES)
            {
                Logger.logDebug("  Returning virtualFileArray:")
                for (virtualFile in virtualFileArray)
                    Logger.logDebug("    $virtualFile")
            }

            return virtualFileArray
        }

        if (Constants.DEBUG_DATA_CONTEXT_WITH_FILES) Logger.logDebug("  Returning old value: ${this.dataContext.getData(dataId)}")
        return this.dataContext.getData(dataId)
    }
}