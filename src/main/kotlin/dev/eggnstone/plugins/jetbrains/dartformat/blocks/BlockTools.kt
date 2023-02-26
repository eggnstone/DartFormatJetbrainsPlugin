package dev.eggnstone.plugins.jetbrains.dartformat.blocks

import dev.eggnstone.plugins.jetbrains.dartformat.dotlin.DotlinLogger

class BlockTools
{
    companion object
    {
        fun printBlocks(blocks: List<IBlock>)
        {
            for (block in blocks)
                DotlinLogger.log(block.toString())
        }
    }
}
