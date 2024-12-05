package org.example

import kotlin.math.abs

class Day1(private val filename: String) {
  operator fun invoke(): Int {
    var result: Array<Array<Int>> = arrayOf()
    Utils.forEachLine(filename) { line ->
      val numbers: List<Int> = line.split("\\s+".toRegex()).map { it.toInt() }
      result += numbers.toTypedArray()
    }

    // get distance
    val column1 = result.map { it[0] }.sorted()
    val column2 = result.map { it[1] }.sorted()

    var distance = 0
    for (i in column1.indices) {
      distance += abs(column1[i] - column2[i])
    }
    return distance
  }
}
