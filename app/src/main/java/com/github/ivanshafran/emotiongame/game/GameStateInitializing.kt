package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.game.game_object.*
import com.github.ivanshafran.emotiongame.resource.ResourceProvider

private const val MILLIS_IN_SECOND = 1000f

fun getInitializedGameState(config: GameConfig, resourceProvider: ResourceProvider): GameState {
    return GameState(
        score = getInitializedScore(config, resourceProvider),
        life = getInitializedLife(config, resourceProvider),
        bonus = getInitializedBonus(config, resourceProvider),
        enemy = getInitializedEnemy(config, resourceProvider),
        player = getInitializedPlayer(config, resourceProvider),
        road = getInitializedRoad(config),
        sky = getInitializedSky(config, resourceProvider)
    )
}

private fun getInitializedSky(config: GameConfig, resourceProvider: ResourceProvider): Sky {
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
        clouds = getInitializedClouds(config, resourceProvider),
        stars = listOf()
    )
}

private fun getInitializedClouds(config: GameConfig, resourceProvider: ResourceProvider): List<Cloud> {
    return config.skyConfig.cloudConfigs.map { cloudConfig ->
        val height = (config.canvasConfig.height * cloudConfig.heightFraction).toInt()
        val width = (height * cloudConfig.widthToHeightAspectRatio).toInt()
        val x = config.canvasConfig.width * cloudConfig.xWidthFraction
        val y = config.canvasConfig.height * cloudConfig.yHeightFraction

        Cloud(
            gameObject = GameObject(
                drawable = BitmapDrawable(cloudConfig.drawableRes),
                rect = Rect(
                    x = x,
                    y = y,
                    width = width,
                    height = height
                )
            ),
            speedPerMillis = resourceProvider.getDimen(cloudConfig.speedDimenRes) / MILLIS_IN_SECOND
        )
    }
}

private fun getInitializedRoad(config: GameConfig): Road {
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
        lineDividers = getInitializedLineDividers(config)
    )
}

private fun getInitializedLineDividers(config: GameConfig): List<GameObject> {
    val lineDividers = mutableListOf<GameObject>()
    val lineHeight = (config.canvasConfig.height * config.roadConfig.lineHeightFraction).toInt()
    val lineWidth = (lineHeight * config.roadConfig.lineWidthToHeightAspectRatio).toInt()
    val lineSkip = (config.canvasConfig.width * config.roadConfig.lineSkipFraction)
    val drawable = ColorDrawable(colorRes = config.roadConfig.lineColor)
    val y = config.canvasConfig.height *
            (config.skyConfig.heightFraction + (1 - config.skyConfig.heightFraction) / 2) - lineHeight / 2

    lineDividers.add(
        GameObject(
            drawable = drawable,
            rect = Rect(x = 0f, y = y, width = lineWidth, height = lineHeight)
        )
    )

    while (lineDividers.last().rect.x < config.canvasConfig.width) {
        lineDividers.add(
            GameObject(
                drawable = drawable,
                rect = Rect(
                    x = lineDividers.last().rect.x + lineSkip + lineWidth,
                    y = y,
                    width = lineWidth,
                    height = lineHeight
                )
            )
        )
    }

    return lineDividers
}

private fun getInitializedPlayer(config: GameConfig, resourceProvider: ResourceProvider): Player {
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
                y = getYPositionForSecondLine(config, height),
                height = height,
                width = width
            )
        ),
        speedPerMillis = resourceProvider.getDimen(config.playerConfig.speedDimenRes) / MILLIS_IN_SECOND
    )
}

private fun getInitializedEnemy(config: GameConfig, resourceProvider: ResourceProvider): Enemy {
    val height = (config.canvasConfig.height * config.enemyConfig.heightFraction).toInt()
    val width = (height * config.enemyConfig.widthToHeightAspectRatio).toInt()
    return Enemy(
        gameObject = GameObject(
            drawable = AnimatedBitmapDrawable(
                drawableResList = config.enemyConfig.animationDrawableResList,
                frameSkipBeforeNewBitmap = config.enemyConfig.animationFrameSkipCount
            ),
            rect = Rect(
                x = config.canvasConfig.width * (1f + config.enemyConfig.widthFractionStartOffset),
                y = getYPositionForSecondLine(config, height),
                width = width,
                height = height
            )
        ),
        speedPerMillis = resourceProvider.getDimen(config.enemyConfig.speedDimenRes) / MILLIS_IN_SECOND
    )
}

private fun getInitializedBonus(config: GameConfig, resourceProvider: ResourceProvider): Bonus {
    val height = (config.canvasConfig.height * config.bonusConfig.heightFraction).toInt()
    val width = (height * config.bonusConfig.widthToHeightAspectRatio).toInt()
    return Bonus(
        gameObject = GameObject(
            drawable = BitmapDrawable(
                bitmapDrawableRes = config.bonusConfig.drawableResList.first()
            ),
            rect = Rect(
                x = config.canvasConfig.width.toFloat(),
                y = getYPositionForFirstLine(config, height),
                height = height,
                width = width
            )
        ),
        speedPerMillis = resourceProvider.getDimen(config.bonusConfig.speedDimenRes) / MILLIS_IN_SECOND
    )
}

private fun getInitializedLife(config: GameConfig, resourceProvider: ResourceProvider): Life {
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

private fun getInitializedScore(config: GameConfig, resourceProvider: ResourceProvider): Score {
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
