package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.camera.FaceFeatures
import com.github.ivanshafran.emotiongame.game.game_object.Rect
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
    private var deltaTimeInMillis: Long = NO_TIME

    private val gameState: GameState = getInitializedGameState(config, resourceProvider)
    private lateinit var faceFeatures: FaceFeatures

    private var speedMultiplierIndex = 0

    fun doNextStep(timeInMillis: Long, faceFeatures: FaceFeatures): GameState {
        this.faceFeatures = faceFeatures

        if (this.timeInMillis == NO_TIME) {
            this.startTimeInMillis = timeInMillis
            this.timeInMillis = timeInMillis
        } else {
            deltaTimeInMillis = timeInMillis - this.timeInMillis
            this.timeInMillis = timeInMillis
            doNextStep()
        }

        return gameState
    }

    private fun doNextStep() {
        doRoadLinesStep()
        doSkyCloudsStep()
    }

    private fun doRoadLinesStep() {
        val playerStep = calculateStep(gameState.player.speedPerMillis)

        val lineDividers = gameState.road.lineDividers
        for (index in lineDividers.indices) {
            val rect = lineDividers[index].rect
            rect.x -= playerStep
            if (isRectOutOfScreen(rect)) {
                val lastIndex = if (index == 0) lineDividers.size - 1 else index - 1
                val lineSkip = config.canvasConfig.width * config.roadConfig.lineSkipFraction
                rect.x = lineDividers[lastIndex].rect.x + rect.width + lineSkip
            }
        }
    }

    private fun doSkyCloudsStep() {
        for (cloud in gameState.sky.clouds) {
            val rect = cloud.gameObject.rect
            rect.x -= calculateStep(cloud.speedPerMillis)
            if (isRectOutOfScreen(rect)) {
                rect.x = config.canvasConfig.width.toFloat()
            }
        }
    }

    private fun isRectOutOfScreen(rect: Rect) = rect.x + rect.width < 0

    private fun calculateStep(speedPerMillis: Float): Float {
        return speedPerMillis * getSpeedMultiplier() * deltaTimeInMillis
    }

    private fun getSpeedMultiplier(): Float {
        val multipliers = config.speedMultiplier.multipliers
        if (multipliers.size != speedMultiplierIndex + 1) {
            if (multipliers[speedMultiplierIndex].timeFromStartInMillis < (timeInMillis - startTimeInMillis)) {
                speedMultiplierIndex += 1
            }
        }

        return multipliers[speedMultiplierIndex].multiplier
    }

}
