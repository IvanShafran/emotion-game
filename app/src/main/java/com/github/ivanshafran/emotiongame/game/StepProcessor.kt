package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.camera.FaceFeatures
import com.github.ivanshafran.emotiongame.game.game_object.*
import com.github.ivanshafran.emotiongame.resource.ResourceProvider


class StepProcessor(
    private val config: GameConfig,
    private val resourceProvider: ResourceProvider
) {

    companion object {
        private const val NO_TIME = -1L
    }

    private var timeInMillis: Long = NO_TIME
    private val gameState: GameState
    private lateinit var faceFeatures: FaceFeatures

    private val firstLineCenterY: Float by lazy {
        val roadConfig = config.roadConfig
        val canvasHeight = config.canvasConfig.height
        val roadPadding = canvasHeight *
                (roadConfig.doubleBorderHeightFraction + roadConfig.doubleGrassHeightFraction) / 2

        val roadTop = config.canvasConfig.height * config.skyConfig.heightFraction + roadPadding
        val roadBottom = config.canvasConfig.height - roadPadding
        return@lazy roadTop + (roadBottom - roadTop) / 4
    }

    private val secondLineCenterY: Float by lazy {
        val roadHeight = config.canvasConfig.height * config.roadConfig.heightFraction
        return@lazy firstLineCenterY + roadHeight / 2
    }

    init {
        gameState = getInitializedGameState()
    }

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

    private fun getYPositionForFirstLine(objectHeight: Int): Float {
        return firstLineCenterY - objectHeight / 2
    }

    private fun getYPositionForSecondLine(objectHeight: Int): Float {
        return secondLineCenterY - objectHeight / 2
    }

    private fun getInitializedGameState(): GameState {
        return GameState(
            score = getInitializedScore(),
            life = getInitializedLife(),
            bonus = getInitializedBonus(),
            enemy = getInitializedEnemy(),
            player = getInitializedPlayer(),
            road = getInitializedRoad(),
            sky = getInitializedSky()
        )
    }

    private fun getInitializedSky(): Sky {
        val height = config.canvasConfig.height * config.skyConfig.heightFraction
        return Sky(
            background = GameObject(
                ColorDrawable(colorRes = config.skyConfig.dayColor),
                rect = Rect(
                    x = 0f,
                    y = 0f,
                    width = config.canvasConfig.width,
                    height = height.toInt()
                )
            ),
            clouds = listOf(),
            stars = listOf()
        )
    }

    private fun getInitializedRoad(): Road {
        val grassHeight = (config.canvasConfig.height * config.roadConfig.doubleGrassHeightFraction / 2).toInt()
        val uppGrassTop = config.canvasConfig.height * config.skyConfig.heightFraction

        val upBorderTop = uppGrassTop + grassHeight
        val borderHeight = (config.canvasConfig.height * config.roadConfig.doubleBorderHeightFraction / 2).toInt()

        val roadTop = upBorderTop + borderHeight
        val roadHeight = (config.canvasConfig.height * config.roadConfig.heightFraction).toInt()

        val downBorderTop = roadTop + roadHeight

        val downGrassTop = downBorderTop + borderHeight

        val width = config.canvasConfig.width

        return Road(
            road = GameObject(
                drawable = ColorDrawable(colorRes = config.roadConfig.color),
                rect = Rect(
                    x = 0f,
                    y = roadTop,
                    width = width,
                    height = roadHeight
                )
            ),
            upGrass = GameObject(
                drawable = ColorDrawable(colorRes = config.roadConfig.grassColor),
                rect = Rect(
                    x = 0f,
                    y = uppGrassTop,
                    width = width,
                    height = grassHeight
                )
            ),
            downGrass = GameObject(
                drawable = ColorDrawable(colorRes = config.roadConfig.grassColor),
                rect = Rect(
                    x = 0f,
                    y = downGrassTop,
                    width = width,
                    height = grassHeight
                )
            ),
            upBorder = GameObject(
                drawable = ColorDrawable(colorRes = config.roadConfig.borderColor),
                rect = Rect(
                    x = 0f,
                    y = upBorderTop,
                    width = width,
                    height = borderHeight
                )
            ),
            downBorder = GameObject(
                drawable = ColorDrawable(colorRes = config.roadConfig.borderColor),
                rect = Rect(
                    x = 0f,
                    y = downBorderTop,
                    width = width,
                    height = borderHeight
                )
            ),
            lineDividers = listOf()
        )
    }

    private fun getInitializedPlayer(): Player {
        val height = (config.canvasConfig.height * config.playerConfig.heightFraction).toInt()
        val width = (height * config.playerConfig.widthToHeightAspectRatio).toInt()
        return Player(
            gameObject = GameObject(
                drawable = AnimatedBitmapDrawable(
                    drawableResList = config.playerConfig.animationDrawableResList,
                    frameSkipBeforeNewBitmap = config.playerConfig.animationFrameSkipCount
                ),
                rect = Rect(
                    x = config.canvasConfig.width * config.playerConfig.startMarginWidthFraction,
                    y = getYPositionForSecondLine(height),
                    height = height,
                    width = width
                )
            )
        )
    }

    private fun getInitializedEnemy(): Enemy {
        val height = (config.canvasConfig.height * config.enemyConfig.heightFraction).toInt()
        val width = (height * config.enemyConfig.widthToHeightAspectRatio).toInt()
        return Enemy(
            gameObject = GameObject(
                drawable = AnimatedBitmapDrawable(
                    drawableResList = config.enemyConfig.animationDrawableResList,
                    frameSkipBeforeNewBitmap = config.enemyConfig.animationFrameSkipCount
                ),
                rect = Rect(
                    x = config.canvasConfig.width.toFloat(),
                    y = getYPositionForSecondLine(height),
                    width = width,
                    height = height
                )
            )
        )
    }

    private fun getInitializedBonus(): Bonus {
        val height = (config.canvasConfig.height * config.bonusConfig.heightFraction).toInt()
        val width = (height * config.bonusConfig.widthToHeightAspectRatio).toInt()
        return Bonus(
            gameObject = GameObject(
                drawable = BitmapDrawable(
                    bitmapDrawableRes = config.bonusConfig.drawableResList.first()
                ),
                rect = Rect(
                    x = config.canvasConfig.width.toFloat(),
                    y = getYPositionForFirstLine(height),
                    height = height,
                    width = width
                )
            )
        )
    }

    private fun getInitializedLife(): Life {
        val lifeConfig = config.lifeConfig
        val scoreHeight = config.canvasConfig.height * config.scoreConfig.heightFraction
        val height = (config.canvasConfig.height * lifeConfig.heightFraction).toInt()
        val width = (height * lifeConfig.widthToHeightAspectRatio).toInt()
        return Life(
            value = lifeConfig.initLifeCount,
            gameObject = GameObject(
                drawable = TextDrawable(
                    textColor = lifeConfig.textColor,
                    textSize = lifeConfig.textSize,
                    text = resourceProvider.getString(lifeConfig.stringRes, lifeConfig.initLifeCount)
                ),
                rect = Rect(x = 0f, y = scoreHeight, width = width, height = height)
            )
        )
    }

    private fun getInitializedScore(): Score {
        val height = (config.canvasConfig.height * config.scoreConfig.heightFraction).toInt()
        val width = (height * config.scoreConfig.widthToHeightAspectRatio).toInt()
        return Score(
            value = 0,
            gameObject = GameObject(
                drawable = TextDrawable(
                    text = resourceProvider.getString(config.scoreConfig.stringRes, 0),
                    textSize = config.scoreConfig.textSize,
                    textColor = config.scoreConfig.textColor
                ),
                rect = Rect(x = 0f, y = 0f, width = width, height = height)
            )
        )
    }

}
