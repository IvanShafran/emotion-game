package com.github.ivanshafran.emotiongame.game.game_object

import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes

/** Abstract drawable on canvas */
sealed class CanvasDrawable

/** Should be drawn as bitmap */
data class BitmapDrawable(
    @DrawableRes val bitmapDrawableRes: Int
) : CanvasDrawable()

/** Should be drawn as color */
data class ColorDrawable(
    @ColorRes val colorRes: Int
) : CanvasDrawable()

/** Should be drawn as frame animation */
data class AnimatedBitmapDrawable(
    private val drawableResList: List<Int>,
    private val frameSkipBeforeNewBitmap: Int
) : CanvasDrawable() {

    init {
        if (drawableResList.isEmpty()) {
            throw IllegalArgumentException("CanvasDrawable res list size is zero")
        }
    }

    private var index: Int = 0
    private var frameSkipCounter: Int = 0

    @DrawableRes
    fun getNextDrawableRes(): Int {
        val res = drawableResList[index]

        frameSkipCounter += 1
        if (frameSkipCounter == frameSkipBeforeNewBitmap) {
            frameSkipCounter = 0
            index = (index + 1) % drawableResList.size
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
