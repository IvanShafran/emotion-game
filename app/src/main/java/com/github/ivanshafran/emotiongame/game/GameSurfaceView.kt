package com.github.ivanshafran.emotiongame.game

import android.content.Context
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.github.ivanshafran.emotiongame.game.drawer.Drawer
import com.github.ivanshafran.emotiongame.resource.ResourceProvider
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

// Use only constructor for xml
class GameSurfaceView(
    context: Context,
    attrs: AttributeSet
) : SurfaceView(
    context,
    attrs
), SurfaceHolder.Callback, Runnable {

    private val drawer: Drawer = Drawer(context)
    private val surfaceHolder: SurfaceHolder = this.holder

    private val lock = ReentrantLock()
    private var isGameInProgress = false
    private var stepProcessor: StepProcessor? = null
    @Volatile
    private var isThreadRunning = false
    @Volatile
    private var emotions = Emotions(isSmile = false, isBlink = false)

    init {
        surfaceHolder.addCallback(this)
        setZOrderOnTop(true)
    }

    fun setEmotions(emotions: Emotions) {
        this.emotions = emotions
    }

    fun startNewGame() = lock.withLock {
        val config = GameConfig(
            CanvasConfig(width, height),
            defaultSkyConfig,
            defaultRoadConfig,
            defaultPlayerConfig,
            defaultEnemyConfig,
            defaultScoreConfig,
            defaultLifeConfig,
            defaultBonusConfig,
            defaultSpeedMultiplierConfig
        )

        stepProcessor = StepProcessor(config, ResourceProvider(context))
        isGameInProgress = true
    }

    fun pauseGame() = lock.withLock {
        isGameInProgress = false
    }

    fun resumeGame() = lock.withLock {
        isGameInProgress = true
    }

    override fun surfaceCreated(surfaceHolder: SurfaceHolder) {
        val thread = Thread(this)
        thread.start()
        isThreadRunning = true
    }

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        // Set thread running flag to false when Surface is destroyed.
        // Then the thread will jump out the while loop and complete.
        isThreadRunning = false
    }

    override fun run() {
        while (isThreadRunning) {
            val stepProcessor = stepProcessor
            if (isGameInProgress && stepProcessor != null) {
                val canvas = surfaceHolder.lockCanvas()

                val gameState = stepProcessor.doNextStep(
                    System.currentTimeMillis(),
                    emotions
                )
                drawer.draw(canvas, gameState)

                surfaceHolder.unlockCanvasAndPost(canvas)

                fpsSleep()
            }
        }
    }

    private fun fpsSleep() {
        try {
            Thread.sleep(16)
        } catch (ex: InterruptedException) {
            isThreadRunning = false
        }
    }
}
