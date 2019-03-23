package com.github.ivanshafran.emotiongame.game.game_object

import android.support.annotation.ColorRes
import android.support.annotation.DimenRes

/** Abstract drawable on canvas */
sealed class CanvasDrawable

/** Should be drawn as bitmap */
data class BitmapDrawable(
    var filepath: String
) : CanvasDrawable()

/** Should be drawn as color */
data class ColorDrawable(
    @ColorRes val colorRes: Int
) : CanvasDrawable()

/** Should be drawn as frame animation */
data class AnimatedBitmapDrawable(
    private val imageFilepath: List<String>,
    private val frameSkipBeforeNewBitmap: Int
) : CanvasDrawable() {

    init {
        if (imageFilepath.isEmpty()) {
            throw IllegalArgumentException("CanvasDrawable res list size is zero")
        }
    }

    private var index: Int = 0
    private var frameSkipCounter: Int = 0

    fun getNextImageFilepath(): String {
        val res = imageFilepath[index]

        frameSkipCounter += 1
        if (frameSkipCounter == frameSkipBeforeNewBitmap) {
            frameSkipCounter = 0
            index = (index + 1) % imageFilepath.size
        }

        return res
    }

}

/** Should be drawn as text */
data class TextDrawable(
    var text: String,
    @DimenRes val textSize: Int,
    @ColorRes val textColor: Int
) : CanvasDrawable()
