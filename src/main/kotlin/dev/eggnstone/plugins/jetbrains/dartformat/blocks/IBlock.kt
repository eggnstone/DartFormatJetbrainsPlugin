package dev.eggnstone.plugins.jetbrains.dartformat.blocks

interface IBlock
{
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String
}
