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

    private var startTimeInMillis: Long = NO_TIME
    private var timeInMillis: Long = NO_TIME
    private val gameState: GameState = getInitializedGameState(config, resourceProvider)
    private lateinit var faceFeatures: FaceFeatures

    private var speedMultiplierIndex = 0

    private val playerSpeedPerMillis by lazy {
        resourceProvider.getDimen(config.playerConfig.speedDimenRes) / 1000f
    }

    fun doNextStep(timeInMillis: Long, faceFeatures: FaceFeatures): GameState {
        this.faceFeatures = faceFeatures

        if (this.timeInMillis == NO_TIME) {
            this.startTimeInMillis = timeInMillis
            this.timeInMillis = timeInMillis
        } else {
            doNextStepByDelta(timeInMillis - this.timeInMillis)
            this.timeInMillis = timeInMillis
        }

        return gameState
    }

    private fun doNextStepByDelta(deltaTimeInMillis: Long) {
        val playerStep = deltaTimeInMillis * playerSpeedPerMillis * getSpeedMultiplier()
        doBackgroundStep(playerStep)
    }

    private fun doBackgroundStep(step: Float) {
        doRoadLinesStep(step)
    }

    private fun doRoadLinesStep(step: Float) {
        val lineDividers = gameState.road.lineDividers
        for (index in lineDividers.indices) {
            val rect = lineDividers[index].rect
            rect.x -= step
            if (rect.x + rect.width < 0) {
                val lastIndex = if (index == 0) lineDividers.size - 1 else index - 1
                val lineSkip = config.canvasConfig.width * config.roadConfig.lineSkipFraction
                rect.x = lineDividers[lastIndex].rect.x + rect.width + lineSkip
            }
        }
    }

    private fun getSpeedMultiplier(): Float {
        val multipliers = config.speedMultiplier.multipliers
        if (multipliers.size != speedMultiplierIndex + 1) {
            if (multipliers[speedMultiplierIndex].timeFromStartInMillis > startTimeInMillis) {
                speedMultiplierIndex += 1
            }
        }

        return multipliers[speedMultiplierIndex].multiplier
    }

}
