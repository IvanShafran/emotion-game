package com.github.ivanshafran.emotiongame

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.ivanshafran.emotiongame.accept.AcceptPreferences

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val acceptPreferences = AcceptPreferences(this)
        val intent = if (acceptPreferences.shouldAccept()) {
            AcceptActivity.getIntent(this)
        } else {
            MainActivity.getIntent(this)
        }
        startActivity(intent)
        finish()
    }
}
