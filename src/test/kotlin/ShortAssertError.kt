class ShortAssertError(private val text: String) : Error()
{
    override val message: String
        get() = text

    override fun getStackTrace(): Array<StackTraceElement> = arrayOf(super.getStackTrace()[1])

    override fun toString(): String = ""
}
