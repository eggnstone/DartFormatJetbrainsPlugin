class ShortAssertError(private val messageText: String, private val toStringText: String, private val stackPos: Int) : Error()
{
    override val message: String
        get() = messageText

    override fun getStackTrace(): Array<StackTraceElement> = arrayOf(super.getStackTrace()[stackPos])

    override fun toString(): String = toStringText
}
