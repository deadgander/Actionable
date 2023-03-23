package ir.mmd.intellijDev.Actionable.util.ext

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

/**
 * Copies the string to clipboard
 */
fun String.copyToClipboard() = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(this), null)

/**
 * Checks if all characters in the string are distinct characters
 */
fun String.isAllDistinct() = toCharArray().distinct().size == length

/**
 * DSL for building strings using [StringBuilder]
 */
inline fun stringBuilder(block: StringBuilder.() -> Unit) = StringBuilder().apply(block).toString()

/**
 * `in` operator for checking nullable character existence in a string
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun String.contains(char: Char?) = char != null && indexOf(char) >= 0

/**
 * transforms first letter to upper-case
 */
inline val String.titleCase: String get() = replaceFirstChar { c -> c.uppercaseChar() }

/**
 * Replaces all [ranges] in the string with the [replacement]
 *
 * **IMPORTANT:** The [ranges] must be sorted
 *
 * @return the new string and a new `ranges` that corresponds to replacement ranges in the resulting string
 */
fun String.replaceRanges(ranges: List<IntRange>, replacement: String): Pair<String, List<IntRange>> {
	val firstRange = ranges.firstOrNull() ?: return this to ranges
	val newRanges = ranges.toMutableList()
	val replacementLength = replacement.length
	val builder = StringBuilder()
	
	builder.append(substring(0, firstRange.first))
	builder.append(replacement)
	newRanges[0] = firstRange.first..firstRange.first + replacementLength
	
	if (ranges.size == 1) {
		builder.append(substring(firstRange.last))
		return builder.toString() to newRanges
	}
	
	for (i in 1 until ranges.lastIndex) {
		val text = substring(ranges[i - 1].last, ranges[i].first)
		builder.append(text)
		builder.append(replacement)
		
		val newRangeStart = newRanges[i - 1].last + text.length
		newRanges[i] = newRangeStart..newRangeStart + replacementLength
	}
	
	val lastRange = ranges.last()
	val text = substring(ranges[ranges.lastIndex - 1].last, lastRange.first)
	
	builder.append(text)
	builder.append(replacement)
	builder.append(substring(lastRange.last))
	
	val newRangeStart = newRanges[ranges.lastIndex - 1].last + text.length
	newRanges[ranges.lastIndex] = newRangeStart..newRangeStart + replacementLength
	
	return builder.toString() to newRanges
}

/**
 * `in` operator for bound checking an [Int] in a nullable [IntRange]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange?.contains(i: Int) = this != null && contains(i)

/**
 * Checks if the number is **greater** than zero
 */
inline val Int.isPositive: Boolean get() = this > 0

/**
 * Same as kotlin `run` but doesn't return anything
 */
inline fun <T> T.runOnly(block: T.() -> Unit) = block()

/**
 * Same as kotlin `let` but doesn't return anything
 */
inline fun <T> T.letOnly(block: (T) -> Unit) = block(this)

/**
 * Sugar syntax to check if something is not null
 */
inline val <T> T.isNotNull: Boolean get() = this != null

/**
 * like `forEach` but brings the receiver to the scope
 */
inline fun <T> Iterable<T>.withEach(block: T.() -> Unit) = forEach { it.block() }

/**
 * like `forEach` but iterates over those items that meet the [condition]
 */
inline fun <T> Iterable<T>.forEachIf(condition: T.() -> Boolean, block: (T) -> Unit) = forEach { if (it.condition()) block(it) }

/**
 * like `forEach` but maps items like what `map` function does
 */
inline fun <T, R> Iterable<T>.forEachMapped(map: (T) -> R, block: (R) -> Unit) = forEach { block(map(it)) }

/**
 * like [forEachMapped] but brings the receiver to the scope
 */
inline fun <T, R> Iterable<T>.withEachMapped(map: (T) -> R, block: R.() -> Unit) = forEach { map(it).block() }

/**
 * Returns [IntRange.first]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange.component1() = first

/**
 * Returns [IntRange.last]
 */
@Suppress("NOTHING_TO_INLINE")
inline operator fun IntRange.component2() = last
