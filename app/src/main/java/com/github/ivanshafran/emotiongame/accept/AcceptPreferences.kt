package com.github.ivanshafran.emotiongame.accept

import android.content.Context

class AcceptPreferences(context: Context) {

    companion object {
        private const val FILENAME = "ACCEPT_PREFERENCES"
        private const val VERSION = 1
        private const val VERSION_KEY = "VERSION_KEY"
    }

    private val preferences = context.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)

    fun shouldAccept(): Boolean {
        return VERSION != preferences.getInt(VERSION_KEY, 0)
    }

    fun setAccepted() {
        preferences.edit().putInt(VERSION_KEY, VERSION).apply()
    }
}
