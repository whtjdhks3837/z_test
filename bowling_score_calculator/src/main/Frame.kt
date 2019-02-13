import java.util.*

const val TWICE_BOWLING = "C"
const val ONCE_BOWLING = "UC"
const val STRIKE = "ST"
const val UN_COMPUTED_STRIKE = "UST"
const val SPARE = "SP"
const val UN_COMPUTED_SPARE = "USP"
const val EMPTY = "EMP"
data class Frame(val stack: Stack<Int>, var case: String = EMPTY)