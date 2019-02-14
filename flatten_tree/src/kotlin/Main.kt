import java.io.File
import java.lang.StringBuilder
import java.util.*

fun main(args: Array<String>) {
    val json = StringBuilder()
    File("./src/resource/categories.json").forEachLine { json.append(it) }
    JsonToArrayConverter().convert(json.toString()).forEach {
        println(it.substring(0, it.length - 1))
    }
}

class JsonToArrayConverter {
    companion object {
        const val JSON_FORMAT_ERROR = "json format exception"
    }
    fun convert(json: String): List<String> {
        val bracketStack = Stack<Char>()
        val categories = mutableListOf<String>()
        val result = mutableListOf<String>()
        val categoryTmp = StringBuilder()
        json.forEachIndexed { _, it ->
            when {
                it == '{' -> { bracketStack.push(it) }
                it == '}' -> {
                    if (bracketStack.isEmpty())
                        throw JsonFormatException(JSON_FORMAT_ERROR)
                    if (bracketStack.pop() == '{' && bracketStack.isNotEmpty()) {
                        if (categories.size != bracketStack.size)
                            throw JsonFormatException(JSON_FORMAT_ERROR)
                        val category = StringBuilder()
                        for (i in 0 until bracketStack.size)
                            category.append(categories[i])
                        categories.removeAt(bracketStack.size -1)
                        result.add(category.toString())
                    }
                }
                it == ':' -> {
                    if (categories.size < bracketStack.lastIndex)
                        throw JsonFormatException(JSON_FORMAT_ERROR)
                    categories.add(bracketStack.lastIndex, "$categoryTmp>")
                    categoryTmp.setLength(0)
                }
                bracketStack.isNotEmpty() -> {
                    if (it != '"' && it != ':' && it != ',')
                        categoryTmp.append(it)
                }
                else -> { throw JsonFormatException(JSON_FORMAT_ERROR) }
            }
        }
        return result
    }
}

class JsonFormatException(message: String) : Exception(message)