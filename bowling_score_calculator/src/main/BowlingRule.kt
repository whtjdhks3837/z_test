
class BowlingRule(private val frames: List<Frame>) {
    companion object {
        const val FIRST_BOWLING = 0
        const val SECOND_BOWLING = 1
        const val MAX_FRAME = 10
    }

    fun strike(current: Int, next: Int = current + 1): Int {
        val idx = next - current
        return when {
            isNextNull(next) -> 0
            frames[next].stack.isEmpty() -> 0
            idx > 2 -> 0
            frames[next].case == UN_COMPUTED_STRIKE -> {
                frames[current].case = STRIKE
                frames[current].stack.push(frames[next].stack.sum())
                strike(current, next + 1)
            }
            frames[next].case == TWICE_BOWLING || frames[next].case == UN_COMPUTED_SPARE -> {
                frames[current].case = STRIKE
                frames[current].stack.push(frames[next].stack.sum())
            }
            else -> 0
        }
    }

    fun spare(current: Int, next: Int = current + 1) {
        if (!isNextNull(current)) {
            if (frames[next].case == TWICE_BOWLING ||
                    frames[next].case == UN_COMPUTED_SPARE ||
                    frames[next].case == UN_COMPUTED_STRIKE) {
                frames[current].stack.push(frames[next].stack[FIRST_BOWLING])
                frames[current].case = SPARE
            }
        }
    }

    private fun isNextNull(current: Int, next: Int = current + 1)
            = frames.getOrNull(next) == null
}