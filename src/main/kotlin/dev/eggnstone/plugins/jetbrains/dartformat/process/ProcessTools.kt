package dev.eggnstone.plugins.jetbrains.dartformat.process

class ProcessTools
{
    companion object
    {
        fun createProcessBuilder(processBuilderInfo: ProcessBuilderInfo): ProcessBuilder
        {
            return ProcessBuilder(processBuilderInfo.shell, processBuilderInfo.shellParam, processBuilderInfo.command)
        }
    }
}
