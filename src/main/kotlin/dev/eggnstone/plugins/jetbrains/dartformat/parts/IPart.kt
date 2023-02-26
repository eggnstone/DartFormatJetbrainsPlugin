package dev.eggnstone.plugins.jetbrains.dartformat.parts

interface IPart
{
    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
    override fun toString(): String
}
