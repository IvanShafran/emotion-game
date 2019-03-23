package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.game.game_object.BitmapDrawable
import com.github.ivanshafran.emotiongame.game.game_object.ColorDrawable
import com.github.ivanshafran.emotiongame.game.game_object.Rect
import com.github.ivanshafran.emotiongame.game.game_object.TextDrawable
import com.github.ivanshafran.emotiongame.resource.ResourceProvider
import kotlin.random.Random

class StepProcessor(
    private val config: GameConfig,
    private val resourceProvider: ResourceProvider
) {

    companion object {
        private const val NO_TIME = -1L
    }

    private val random = Random

    private var startTimeInMillis: Long = NO_TIME
    private var timeInMillis: Long = NO_TIME
    private var deltaTimeInMillis: Long = NO_TIME

    private val gameState: GameState = getInitializedGameState(config, resourceProvider)
    private var emotions: Emotions = Emotions(isSmile = false, isBlink = false)

    private var speedMultiplierIndex = 0

    fun doNextStep(timeInMillis: Long, emotions: Emotions): GameState {
        this.emotions = emotions

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

    fun recalculateTimeAfterResume(timeInMillis: Long) {
        startTimeInMillis += timeInMillis - this.timeInMillis
        this.timeInMillis = timeInMillis
    }

    private fun doNextStep() {
        doRoadLinesStep()
        updateSky()
        updatePlayer()
        doEnemyStep()
        doBonusStep()
        updateScore()
        updateLife()
    }

    private fun doRoadLinesStep() {
        val playerStep = calculateStep(gameState.player.speedPerMillis)

        val lineDividers = gameState.road.lineDividers
        for (index in lineDividers.indices) {
            val rect = lineDividers[index].rect
            rect.x -= playerStep
            if (isRectToLeftOfScreen(rect)) {
                val lastIndex = if (index == 0) lineDividers.size - 1 else index - 1
                val lineSkip = config.canvasConfig.width * config.roadConfig.lineSkipFraction
                rect.x = lineDividers[lastIndex].rect.x + rect.width + lineSkip
            }
        }
    }

    private fun updateSky() {
        gameState.sky.isDay = !emotions.isBlink
        val color = if (gameState.sky.isDay) {
            config.skyConfig.dayColor
        } else {
            config.skyConfig.nightColor
        }
        gameState.sky.background.drawable = ColorDrawable(color)

        doSkyCloudsStep()
    }

    private fun doSkyCloudsStep() {
        for (cloud in gameState.sky.clouds) {
            val rect = cloud.gameObject.rect
            rect.x -= calculateStep(cloud.speedPerMillis)
            if (isRectToLeftOfScreen(rect)) {
                rect.x = config.canvasConfig.width.toFloat()
            }
        }
    }

    private fun updatePlayer() {
        val rect = gameState.player.gameObject.rect
        rect.y = if (emotions.isSmile) {
            getYPositionForFirstLine(config, rect.height)
        } else {
            getYPositionForSecondLine(config, rect.height)
        }
    }

    private fun doEnemyStep() {
        val step = calculateStep(gameState.enemy.speedPerMillis)

        val rect = gameState.enemy.gameObject.rect
        rect.x -= step

        var shouldRestart = isRectToLeftOfScreen(rect)
        val intersectOffset = gameState.enemy.gameObject.rect.width * config.enemyConfig.widthIntersectOffsetFraction
        val intersectRect = Rect(
            x = rect.x + intersectOffset,
            y = rect.y,
            width = rect.width,
            height = rect.height
        )

        if (isRectIntersects(intersectRect, gameState.player.gameObject.rect)) {
            gameState.life.value -= 1
            shouldRestart = true
        }

        if (shouldRestart) {
            rect.x = config.canvasConfig.width * (1f + config.enemyConfig.widthFractionStartOffset)

            if (random.nextBoolean()) {
                rect.y = getYPositionForFirstLine(config, rect.height)
            } else {
                rect.y = getYPositionForSecondLine(config, rect.height)
            }
        }
    }

    private fun doBonusStep() {
        val step = calculateStep(gameState.bonus.speedPerMillis)

        val rect = gameState.bonus.gameObject.rect
        rect.x -= step

        var shouldRestart = isRectToLeftOfScreen(rect)
        val intersectOffset = gameState.bonus.gameObject.rect.width * config.bonusConfig.widthIntersectOffsetFraction
        val intersectRect = Rect(
            x = rect.x + intersectOffset,
            y = rect.y,
            width = rect.width,
            height = rect.height
        )

        if (isRectIntersects(intersectRect, gameState.player.gameObject.rect)) {
            gameState.score.value += if (gameState.sky.isDay) {
                config.scoreConfig.dayBonusScore
            } else {
                config.scoreConfig.nightBonusScore
            }
            shouldRestart = true
        }

        if (shouldRestart) {
            rect.x = config.canvasConfig.width * (1f + config.bonusConfig.widthFractionStartOffset)

            if (random.nextBoolean()) {
                rect.y = getYPositionForFirstLine(config, rect.height)
            } else {
                rect.y = getYPositionForSecondLine(config, rect.height)
            }

            (gameState.bonus.gameObject.drawable as TextDrawable).text = config.bonusConfig.cakeEmojis.random()
        }
    }

    private fun updateLife() {
        val life = gameState.life
        val emptyHeartPath = config.lifeConfig.emptyHeartPath

        when (life.value) {
            2 -> {
                (life.thirdHeart.drawable as BitmapDrawable).filepath = emptyHeartPath
            }
            1 -> {
                (life.secondHeart.drawable as BitmapDrawable).filepath = emptyHeartPath
                (life.thirdHeart.drawable as BitmapDrawable).filepath = emptyHeartPath
            }
        }
    }

    private fun updateScore() {
        val score = gameState.score
        score.gameObject.drawable = TextDrawable(
            text = resourceProvider.getString(config.scoreConfig.stringRes, score.value),
            textColor = config.scoreConfig.textColor,
            textSize = config.scoreConfig.textSize
        )
    }

    private fun isRectIntersects(first: Rect, second: Rect): Boolean {
        return isPointInRect(first.x, first.y, second) ||
                isPointInRect(first.x, first.y + first.height, second) ||
                isPointInRect(first.x + first.width, first.y, second) ||
                isPointInRect(first.x + first.width, first.y + first.height, second)
    }

    private fun isPointInRect(x: Float, y: Float, rect: Rect): Boolean {
        return rect.x <= x && x < rect.x + rect.width && rect.y <= y && y < rect.y + rect.height
    }

    private fun isRectToLeftOfScreen(rect: Rect) = rect.x + rect.width < 0

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
