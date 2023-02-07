class ShortAssertError(private val messageText: String, private val toStringText: String) : Error()
{
    override val message: String
        get() = messageText

    override fun getStackTrace(): Array<StackTraceElement> = arrayOf(super.getStackTrace()[1])

    override fun toString(): String = toStringText
}
