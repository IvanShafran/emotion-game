package com.github.ivanshafran.emotiongame.accept

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.support.annotation.WorkerThread
import com.github.ivanshafran.emotiongame.R
import com.github.ivanshafran.emotiongame.game.drawer.BitmapLoader

/** Create cat idle animation **/
@WorkerThread
fun createAcceptAnimation(context: Context): AnimationDrawable {
    val animationDrawable = AnimationDrawable().apply { isOneShot = false }

    val width = context.resources.getDimensionPixelSize(R.dimen.accept_animation_width)
    val height = context.resources.getDimensionPixelSize(R.dimen.accept_animation_height)
    for (i in 0 until 10) {
        val bitmap = BitmapLoader.load(context, "cat_idle/idle_$i.png", width, height)
        animationDrawable.addFrame(BitmapDrawable(context.resources, bitmap), 60)
    }

    return animationDrawable
}
