package com.github.ivanshafran.emotiongame.resource

import android.content.Context
import android.support.annotation.DimenRes
import android.support.annotation.StringRes

class ResourceProvider(private val context: Context) {

    fun getString(@StringRes stringRes: Int, vararg args: Any?): String {
        return context.getString(stringRes, *args)
    }

    fun getDimen(@DimenRes dimenRes: Int): Int {
        return context.resources.getDimensionPixelSize(dimenRes)
    }

}
