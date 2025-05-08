package sh.kau.karabiner

import java.math.BigInteger
import java.nio.file.Path
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

object Utils {
  fun forEachLine(fileName: String, lineAction: (String) -> Unit) {
    path(fileName).readLines().forEach { lineAction.invoke(it) }
  }

  fun allLinesAsString(fileName: String): String {
    val lines = mutableListOf<String>()
    forEachLine(fileName) { line -> lines.add(line) }
    return lines.joinToString("\n")
  }

  fun rowColumnSizeOf(fileName: String): Pair<Int, Int> {
    var row = 0
    var column = 0

    forEachLine(fileName) { line ->
      row++
      column = maxOf(column, line.length)
    }
    return Pair(row, column)
  }

  /** Converts string to md5 hash. */
  fun String.md5() =
      BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
          .toString(16)
          .padStart(32, '0')

  private fun path(fileName: String): Path {
    // First try loading as a resource
    val resourceUrl = javaClass.classLoader.getResource(fileName)
    if (resourceUrl != null) {
      return Path(resourceUrl.path)
    }

    // Fallback to direct file path
    return Path(fileName)
  }
}

/** The cleaner shorthand for printing output. */
fun Any?.println() = println(this)
