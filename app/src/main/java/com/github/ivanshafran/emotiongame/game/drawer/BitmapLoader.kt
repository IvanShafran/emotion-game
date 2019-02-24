package com.github.ivanshafran.emotiongame.game.drawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.annotation.DrawableRes

object BitmapLoader {

    fun load(context: Context, @DrawableRes res: Int, width: Int, height: Int): Bitmap {
        val raw = BitmapFactory.decodeResource(context.resources, res)
        return Bitmap.createScaledBitmap(raw, width, height, true)
    }

}
