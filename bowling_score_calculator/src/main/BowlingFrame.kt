import java.util.*

class BowlingFrame {
    private val frames = listOf(
            Frame(Stack()), Frame(Stack()), Frame(Stack()), Frame(Stack()),
            Frame(Stack()), Frame(Stack()), Frame(Stack()), Frame(Stack()),
            Frame(Stack()), Frame(Stack()), Frame(Stack()), Frame(Stack()),
            Frame(Stack()), Frame(Stack()), Frame(Stack()), Frame(Stack()))

    fun toFrames(bowlingScores: String): List<Frame> {
        var idx = 0
        bowlingScores.forEach { it ->
            val score = Character.getNumericValue(it)
            frames[idx].stack.push(score)
            when {
                isOnceBowlingInFrame(frames[idx].stack) -> {
                    BowlingScoreValidator.validate(frames[idx].stack.sum())
                    frames[idx].case = ONCE_BOWLING
                }
                isSpare(frames[idx].stack) -> {
                    BowlingScoreValidator.validate(frames[idx].stack.sum())
                    frames[idx].case = UN_COMPUTED_SPARE
                    idx++
                }
                isTwiceBowlingInFrame(frames[idx].stack) -> {
                    BowlingScoreValidator.validate(frames[idx].stack.sum())
                    frames[idx].case = TWICE_BOWLING
                    idx++
                }
                isStrike(frames[idx].stack) -> {
                    BowlingScoreValidator.validate(frames[idx].stack.peek())
                    frames[idx].case = UN_COMPUTED_STRIKE
                    idx++
                }
            }
        }
        return frames
    }

    private fun isTwiceBowlingInFrame(stack: Stack<Int>) = stack.size == 2
    private fun isOnceBowlingInFrame(stack: Stack<Int>) = !isStrike(stack) && stack.size == 1
    private fun isStrike(stack: Stack<Int>) = stack.peek() == 10 && stack.size == 1
    private fun isSpare(stack: Stack<Int>) = stack.sum() == 10 && stack.size == 2
}