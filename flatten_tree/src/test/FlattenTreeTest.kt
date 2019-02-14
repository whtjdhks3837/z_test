import org.junit.Before
import org.junit.Test
import java.io.File

class FlattenTreeTest {
    private val converter = JsonToArrayConverter()
    private val validJson = StringBuilder()
    private val colonInvalidJson = StringBuilder()
    private val bracketInvalidJson = StringBuilder()
    private val startBracketInvalidJson = StringBuilder()
    private val endOfDepthInvalidJson = StringBuilder()

    @Before
    fun setUp() {
        File("./src/resource/valid.json").forEachLine { validJson.append(it) }
        File("./src/resource/colonInvalid.json").forEachLine { colonInvalidJson.append(it) }
        File("./src/resource/bracketInvalid.json").forEachLine { bracketInvalidJson.append(it) }
        File("./src/resource/startBracketInvalid.json").forEachLine { startBracketInvalidJson.append(it) }
        File("./src/resource/endOfDepthInvalid.json").forEachLine { endOfDepthInvalidJson.append(it) }
    }

    @Test(expected = JsonFormatException::class)
    fun categoryAddTest() {
        converter.convert(colonInvalidJson.toString())
    }

    @Test(expected = JsonFormatException::class)
    fun bracketPopTest() {
        converter.convert(bracketInvalidJson.toString())
        converter.convert(startBracketInvalidJson.toString())
    }

    @Test(expected = JsonFormatException::class)
    fun endOfDepthTest() {
        converter.convert(endOfDepthInvalidJson.toString())
    }
}