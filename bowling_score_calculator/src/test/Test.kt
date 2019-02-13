import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class Test {
    @Test
    fun bowlingScoreValidateTest() {
        for (score in '!'..'~') {
            if (score in '0'..'9' || score == 'A') {
                assertEquals(true, BowlingScoreValidator.validate(score))
            }
            if (score in ':'..'@' || score in 'B'..'~') {
                assertEquals(false, BowlingScoreValidator.validate(score))
            }
        }
    }

    @Test
    fun frameCaseTest() {
        val score = "5182A913730A1"
        val frames = BowlingFrame().toFrames(score)
        assertEquals(TWICE_BOWLING, frames[0].case)
        assertEquals(ONCE_BOWLING, frames[7].case)
        assertEquals(UN_COMPUTED_STRIKE, frames[2].case)
        assertEquals(UN_COMPUTED_SPARE, frames[1].case)
        assertEquals(EMPTY, frames[8].case)
    }

    @Test
    fun threeFrameContinuousStrikeTest() {
        val score = "AAA12"
        val frames = BowlingFrame().toFrames(score)
        BowlingRule(frames).strike(0)

        assertEquals(STRIKE, frames[0].case)
        assertEquals(30, frames[0].stack.sum())
    }

    @Test
    fun twoFrameContinuousStrikeTest() {
        val score = "AA7112"
        val frames = BowlingFrame().toFrames(score)
        BowlingRule(frames).strike(0)

        assertEquals(STRIKE, frames[0].case)
        assertEquals(28, frames[0].stack.sum())
    }

    @Test
    fun oneFrameStrikeTest() {
        val score = "A513312"
        val frames = BowlingFrame().toFrames(score)
        BowlingRule(frames).strike(0)

        assertEquals(STRIKE, frames[0].case)
        assertEquals(16, frames[0].stack.sum())
    }

    @Test
    fun notStrikeTest() {
        val score = "5182A9150"
        val frames = BowlingFrame().toFrames(score)

        assertNotEquals(UN_COMPUTED_STRIKE, frames[0].case)
        assertNotEquals(UN_COMPUTED_STRIKE, frames[3].case)
    }

    @Test
    fun spareNextFrameTwiceBowlingTest() {
        val score = "5182A9150"
        val frames = BowlingFrame().toFrames(score)
        BowlingRule(frames).spare(3)

        assertEquals(SPARE, frames[3].case)
        assertEquals(15, frames[3].stack.sum())
    }

    @Test
    fun spareNextFrameOnceBowlingTest() {
        val score = "5182A915"
        val frames = BowlingFrame().toFrames(score)
        BowlingRule(frames).spare(3)

        assertEquals(UN_COMPUTED_SPARE, frames[3].case)
        assertEquals(10, frames[3].stack.sum())
    }

    @Test
    fun spareNextFrameSpareTest() {
        val score = "5182A913730"
        val frames = BowlingFrame().toFrames(score)
        BowlingRule(frames).spare(3)

        assertEquals(SPARE, frames[3].case)
        assertEquals(13, frames[3].stack.sum())
    }

    @Test
    fun spareNextFrameStrikeTest() {
        val score = "5182A91A30"
        val frames = BowlingFrame().toFrames(score)
        BowlingRule(frames).spare(3)

        assertEquals(SPARE, frames[3].case)
        assertEquals(20, frames[3].stack.sum())
    }

    @Test
    fun notSpareTest() {
        val score = "5182A31A30"
        val frames = BowlingFrame().toFrames(score)

        assertNotEquals(SPARE, frames[3].case)
    }
}