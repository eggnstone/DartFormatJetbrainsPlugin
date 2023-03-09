class ShortAssertError(private val messageText: String, private val toStringText: String, private val stackPos: Int) : Error()
{
    override val message: String
        get() = messageText

    override fun getStackTrace(): Array<StackTraceElement>
    {
        return arrayOf(super.getStackTrace()[stackPos])

        /*return arrayOf(
            StackTraceElement("declaringClass", "methodName", "fileName", stackPos),
            super.getStackTrace()[stackPos],
            super.getStackTrace()[stackPos + 1],
            super.getStackTrace()[stackPos + 2]
        )*/

        /*var list= mutableListOf<>()

        for ()
        arrayOf(super.getStackTrace()[stackPos])

        return list*/
    }

    override fun toString(): String = toStringText
}
