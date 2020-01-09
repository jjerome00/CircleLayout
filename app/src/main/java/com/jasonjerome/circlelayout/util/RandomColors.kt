package com.jasonjerome.circlelayout.util

import java.util.*

/**
 * Original idea from this post: https://stackoverflow.com/a/55334738/103131
 * - thanks ucmedia !
 * I switched the `recycle` variable to a Set
 */
class RandomColors(var shuffle: Boolean = true) {
    private val recycle = mutableSetOf<Long>()
    private val colors: Stack<Long> = Stack()

    init {
        colors.addAll(
            listOf(
                0xfff44336, 0xffe91e63, 0xff9c27b0, 0xff673ab7,
                0xff3f51b5, 0xff2196f3, 0xff03a9f4, 0xff00bcd4,
                0xff009688, 0xff4caf50, 0xff8bc34a, 0xffcddc39,
                0xffffeb3b, 0xffffc107, 0xffff9800, 0xffff5722,
                0xff795548, 0xff9e9e9e, 0xff607d8b, 0xff333333
            )
        )
        if (shuffle) {
            Collections.shuffle(colors)
        }
    }

    fun setColorList(newColors: List<Long>, shuffle: Boolean = true) {
        colors.removeAllElements()
        recycle.clear()
        this.shuffle = shuffle
        colors.addAll(newColors)
        if (shuffle) {
            Collections.shuffle(colors)
        }
    }

    val color: Int
        get() {
            if (colors.isEmpty()) {
                recycleColors()
            }
            val c = colors.pop()
            recycle.add(c)
            return c.toInt()
        }

    private fun recycleColors() {
        colors.addAll(recycle.toList())
        recycle.clear()
        if (shuffle) Collections.shuffle(colors)
    }
}