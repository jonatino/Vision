@file:JvmName("StringExtensions")

package org.anglur.vision.util

fun String.splitEvery(n: Int) = split(Regex("(?<=\\G${"." * n})")).joinToString(" ")

operator fun String.times(n: Int): String {
	var result = this
	if (n > 1) for (i in 1..n - 1) result += this
	return result
}