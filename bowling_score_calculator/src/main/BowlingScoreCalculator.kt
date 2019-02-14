class BowlingScoreCalculator(private val bowlingScore: String) {
    private val bowlingFrame: BowlingFrame
    private val frames: List<Frame>

    init {
        BowlingScoreValidator.validate(bowlingScore)
        bowlingFrame = BowlingFrame()
        frames = bowlingFrame.toFrames(bowlingScore)
    }

    fun compute() {
        computeFrames()
        val computedFrames = sumEachFrameToList()
        printResult(computedFrames)

    }

    private fun computeFrames()  {
        val rule = BowlingRule(frames)
        frames.filter { !it.stack.empty() }
                .forEachIndexed { index, frame ->
                    when (frame.case) {
                        UN_COMPUTED_SPARE -> { rule.spare(index) }
                        UN_COMPUTED_STRIKE -> { rule.strike(index) }
                    }
                }
    }


    private fun sumEachFrameToList() =
            frames
                    .subList(0, BowlingRule.MAX_FRAME)
                    .asSequence()
                    .filter { it.case != ONCE_BOWLING }
                    .filter { it.case != UN_COMPUTED_STRIKE }
                    .filter { it.case != UN_COMPUTED_SPARE }
                    .filter { it.case != EMPTY }
                    .filter { !it.stack.empty() }
                    .map { it -> it.stack.sum() }
                    .toList()


    private fun printResult(frames: List<Int>) {
        var result = 0
        frames.forEach {
            result += it
            print("$result ")
        }
        println()
    }
}
