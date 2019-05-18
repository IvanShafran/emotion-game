package com.github.ivanshafran.emotiongame

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback
import android.support.v4.app.ShareCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.github.ivanshafran.emotiongame.camera.CameraSource
import com.github.ivanshafran.emotiongame.camera.CameraSourcePreview
import com.github.ivanshafran.emotiongame.camera.GraphicOverlay
import com.github.ivanshafran.emotiongame.camera.facedetection.FaceDetectionProcessor
import com.github.ivanshafran.emotiongame.camera.facedetection.FaceFeatures
import com.github.ivanshafran.emotiongame.camera.facedetection.FaceFeaturesListener
import com.github.ivanshafran.emotiongame.game.Emotions
import com.github.ivanshafran.emotiongame.game.GameEndListener
import com.github.ivanshafran.emotiongame.game.GameSurfaceView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException

class MainActivity :
    AppCompatActivity(),
    OnRequestPermissionsResultCallback,
    View.OnClickListener,
    FaceFeaturesListener,
    GameEndListener {

    companion object {
        private const val TAG = "MainActivity"
        private const val CAMERA_PERMISSION_REQUEST_CODE = 1
        private const val SMILE_THRESHOLD = 0.7f
        private const val EYE_BLINK_THRESHOLD = 0.2f

        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)

    }

    private var cameraSource: CameraSource? = null
    private var graphicOverlay: GraphicOverlay? = null

    private lateinit var cameraPreview: CameraSourcePreview
    private lateinit var gameSurfaceView: GameSurfaceView
    private lateinit var needPermissionViews: List<View>
    private var isPermissionRequestedAutomatically: Boolean = false

    private enum class GameStatus {
        IDLE,
        RUNNING,
        PAUSED
    }

    private var gameStatus: GameStatus = GameStatus.IDLE
    private var lastScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraPreview = findViewById(R.id.cameraPreview)
        graphicOverlay = findViewById(R.id.fireFaceOverlay)
        gameSurfaceView = findViewById(R.id.gameSurfaceView)
        gameSurfaceView.setGameEndListener(this)
        needPermissionViews = listOf(
            findViewById(R.id.needPermissionButton),
            findViewById(R.id.needPermissionImageView),
            findViewById(R.id.needPermissionTextView)
        )

        playButton.setOnClickListener(this)
        infoButton.setOnClickListener(this)
        restartButton.setOnClickListener(this)
        pauseButton.setOnClickListener(this)
        infoButton.setOnClickListener(this)
        shareButton.setOnClickListener(this)
        contentLayout.setOnClickListener(object : View.OnClickListener {
            // Trick to fix navigation UI hiding after appearing
            // cause it leads to contentLayout click
            private var tapCount: Int = 0

            override fun onClick(v: View?) {
                ++tapCount
                if (tapCount % 2 == 0) {
                    hideSystemUI()
                }
            }
        })

        findViewById<View>(R.id.needPermissionButton).setOnClickListener(this)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        val decorView = window.decorView
        decorView.systemUiVisibility = (
            View.SYSTEM_UI_FLAG_IMMERSIVE
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                // Hide the nav bar and status bar
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
            )
    }

    public override fun onResume() {
        super.onResume()
        tryStartCamera()
    }

    override fun onPause() {
        super.onPause()
        if (gameStatus == GameStatus.RUNNING) {
            onPauseClick()
        }
        stopCamera()
    }

    private fun tryStartCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted()
        } else {
            hideCameraViews()
            showPermissionViews()
            // Request permission once to fix request -> onResume loop
            if (!isPermissionRequestedAutomatically) {
                requestCameraPermission()
                isPermissionRequestedAutomatically = true
            }
        }
    }

    private fun hideCameraViews() {
        cameraPreview.visibility = View.GONE
        smileReactionTextView.visibility = View.GONE
        blinkReactionTextView.visibility = View.GONE
        rulesBlinkTextView.visibility = View.GONE
        rulesNotSmileTextView.visibility = View.GONE
        rulesSmileTextView.visibility = View.GONE
    }

    private fun onPermissionsGranted() {
        startCameraSource()
        showMainMenu()
    }

    private fun stopCamera() {
        cameraPreview.stop()

        cameraSource?.release()
        cameraSource = null
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
    }

    private fun startCameraSource() {
        cameraPreview.visibility = View.VISIBLE
        smileReactionTextView.visibility = View.VISIBLE
        blinkReactionTextView.visibility = View.VISIBLE
        rulesBlinkTextView.visibility = View.VISIBLE
        rulesNotSmileTextView.visibility = View.VISIBLE
        rulesSmileTextView.visibility = View.VISIBLE

        val cameraSource = this.cameraSource ?: CameraSource(this, graphicOverlay)
        cameraSource.setMachineLearningFrameProcessor(FaceDetectionProcessor(this))

        try {
            cameraPreview.start(cameraSource, graphicOverlay)
            cameraSource.setFacing(CameraSource.CAMERA_FACING_FRONT)
            this.cameraSource = cameraSource
        } catch (e: IOException) {
            Log.e(TAG, "Unable to start camera source.", e)
            cameraSource.release()
            this.cameraSource = null
        }
    }

    override fun onFaceFeatures(features: FaceFeatures) {
        val emotions = Emotions(
            isSmile = features.smileProbability > SMILE_THRESHOLD,
            isBlink = features.leftEyeOpenProbability < EYE_BLINK_THRESHOLD ||
                features.rightEyeOpenProbability < EYE_BLINK_THRESHOLD
        )
        gameSurfaceView.setEmotions(emotions)

        smileReactionTextView.text = getString(
            if (emotions.isSmile) {
                R.string.smile_reaction
            } else {
                R.string.not_smile_reaction
            }
        )

        blinkReactionTextView.text = getString(
            if (emotions.isBlink) {
                R.string.blink_reaction
            } else {
                R.string.not_blink_reaction
            }
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hidePermissionViews()
                onPermissionsGranted()
            }
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.needPermissionButton -> onNeedPermissionButtonClick()
            R.id.playButton -> onPlayButtonClick()
            R.id.pauseButton -> onPauseClick()
            R.id.restartButton -> onRestartClick()
            R.id.infoButton -> onInfoClick()
            R.id.shareButton -> onShareClick()
            else -> {
                // Do nothing
            }
        }
    }

    private fun onNeedPermissionButtonClick() {
        requestCameraPermission()
    }

    private fun onPlayButtonClick() {
        hideMainMenu()
        when (gameStatus) {
            GameStatus.PAUSED -> gameSurfaceView.resumeGame()
            GameStatus.IDLE -> gameSurfaceView.startNewGame()
            else -> {
                throw IllegalStateException("Play button clicked in RUNNING state")
            }
        }

        gameStatus = GameStatus.RUNNING
        showGame()
    }

    private fun onPauseClick() {
        gameSurfaceView.pauseGame()
        gameStatus = GameStatus.PAUSED
        hideGame()
        showMainMenu()
    }

    private fun onRestartClick() {
        hideMainMenu()
        gameSurfaceView.startNewGame()
        showGame()
    }

    override fun onGameEnd(score: Int) {
        hideGame()
        gameStatus = GameStatus.IDLE
        showMainMenu()

        scoreTextView.visibility = View.VISIBLE
        scoreTextView.text = getString(R.string.score_menu, score)
        lastScore = score
    }

    private fun onInfoClick() {
        startActivity(Intent(this, InfoActivity::class.java))
    }

    private fun onShareClick() {
        ShareCompat.IntentBuilder
            .from(this)
            .setText(
                if (lastScore == 0) {
                    getString(R.string.share_game_text)
                } else {
                    getString(R.string.share_result_text, lastScore)
                }
            )
            .setType("text/plain")
            .startChooser()
    }

    private fun showPermissionViews() {
        needPermissionViews.forEach { it.visibility = View.VISIBLE }
    }

    private fun hidePermissionViews() {
        needPermissionViews.forEach { it.visibility = View.GONE }
    }

    private fun showMainMenu() {
        playButton.visibility = View.VISIBLE
        playLabelTextView.visibility = View.VISIBLE
        infoButton.visibility = View.VISIBLE
        infoLabelTextView.visibility = View.VISIBLE
        if (gameStatus == GameStatus.PAUSED) {
            restartButton.visibility = View.VISIBLE
            restartLabelTextView.visibility = View.VISIBLE
        }
        shareButton.visibility = View.VISIBLE
        shareLabelTextView.visibility = View.VISIBLE
    }

    private fun hideMainMenu() {
        playButton.visibility = View.GONE
        playLabelTextView.visibility = View.GONE
        infoButton.visibility = View.GONE
        infoLabelTextView.visibility = View.GONE
        restartButton.visibility = View.GONE
        restartLabelTextView.visibility = View.GONE
        scoreTextView.visibility = View.GONE
        shareButton.visibility = View.GONE
        shareLabelTextView.visibility = View.GONE
    }

    private fun showGame() {
        gameSurfaceView.visibility = View.VISIBLE
        pauseButton.visibility = View.VISIBLE
        pauseLabelTextView.visibility = View.VISIBLE
    }

    private fun hideGame() {
        gameSurfaceView.visibility = View.INVISIBLE
        pauseButton.visibility = View.GONE
        pauseLabelTextView.visibility = View.GONE
    }
}
