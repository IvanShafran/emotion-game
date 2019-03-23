package com.github.ivanshafran.emotiongame.game.drawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


object BitmapLoader {

    fun load(context: Context, filepath: String, width: Int, height: Int): Bitmap {
        val raw: Bitmap = context.assets.open(filepath).use {
            BitmapFactory.decodeStream(it)
        }

        return Bitmap.createScaledBitmap(raw, width, height, true)
    }

}
