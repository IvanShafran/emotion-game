package com.github.ivanshafran.emotiongame

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import com.github.ivanshafran.emotiongame.accept.AcceptPreferences
import kotlinx.android.synthetic.main.activity_accept.*

class AcceptActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, AcceptActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accept)
        acceptMainText.movementMethod = LinkMovementMethod.getInstance()
        acceptCheckbox.setOnCheckedChangeListener { _, isChecked -> acceptButton.isEnabled = isChecked }
        acceptButton.setOnClickListener {
            AcceptPreferences(this@AcceptActivity).setAccepted()
            startActivity(MainActivity.getIntent(this@AcceptActivity))
            finish()
        }
    }
}
