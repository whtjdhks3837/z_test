import java.lang.Exception

class BowlingScoreValidator {
    companion object {
        private const val MAX_ONE_FRAME_SCORE = 10

        fun validate(bowlingScores: String) {
            bowlingScores.forEach {
                if (!validate(it))
                    throw Exception("잘못된 점수입니다.")
            }
        }

        fun validate(bowlingOnceScore: Char) =
                bowlingOnceScore in '0'..'9' || bowlingOnceScore == 'A'

        fun validate(frameScore: Int) {
            if (frameScore > MAX_ONE_FRAME_SCORE)
                throw Exception("잘못된 점수입니다.")
        }
    }
}