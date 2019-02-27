package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.camera.FaceFeatures
import com.github.ivanshafran.emotiongame.resource.ResourceProvider


class StepProcessor(
    private val config: GameConfig,
    private val resourceProvider: ResourceProvider
) {

    companion object {
        private const val NO_TIME = -1L
    }

    private var timeInMillis: Long = NO_TIME
    private val gameState: GameState = getInitializedGameState(config, resourceProvider)
    private lateinit var faceFeatures: FaceFeatures

    fun doNextStep(timeInMillis: Long, faceFeatures: FaceFeatures): GameState {
        this.faceFeatures = faceFeatures

        if (this.timeInMillis == NO_TIME) {
            this.timeInMillis = timeInMillis
        } else {
            doNextStepByDelta(timeInMillis - this.timeInMillis)
            this.timeInMillis = timeInMillis
        }

        return gameState
    }

    private fun doNextStepByDelta(deltaTimeInMillis: Long) {

    }

}
