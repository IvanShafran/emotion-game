package com.github.ivanshafran.emotiongame

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import com.github.ivanshafran.emotiongame.accept.AcceptPreferences
import com.github.ivanshafran.emotiongame.accept.createAcceptAnimation
import kotlinx.android.synthetic.main.activity_accept.*

@SuppressLint("StaticFieldLeak")
class AcceptActivity : AppCompatActivity() {

    companion object {
        fun getIntent(context: Context) = Intent(context, AcceptActivity::class.java)
    }

    private var createAnimationAsyncTask: AsyncTask<Unit, Unit, AnimationDrawable>? = null

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

        createAnimationAsyncTask = object : AsyncTask<Unit, Unit, AnimationDrawable>() {
            override fun doInBackground(vararg params: Unit?): AnimationDrawable {
                return createAcceptAnimation(this@AcceptActivity)
            }

            override fun onPostExecute(result: AnimationDrawable) {
                acceptAnimationView.background = result
                result.start()
            }
        }
        createAnimationAsyncTask?.execute()
    }

    override fun onDestroy() {
        super.onDestroy()
        createAnimationAsyncTask?.cancel(false)
    }
}
