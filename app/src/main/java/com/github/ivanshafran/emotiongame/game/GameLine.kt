package com.github.ivanshafran.emotiongame.game

fun getYPositionForFirstLine(config: GameConfig, objectHeight: Int): Float {
    return getFirstLineCenterY(config) - objectHeight / 2
}

fun getYPositionForSecondLine(config: GameConfig, objectHeight: Int): Float {
    return getSecondLineCenterY(config) - objectHeight / 2
}


private fun getFirstLineCenterY(config: GameConfig): Float {
    val roadConfig = config.roadConfig
    val canvasHeight = config.canvasConfig.height
    val roadPadding = canvasHeight *
            (roadConfig.doubleBorderHeightFraction + roadConfig.doubleGrassHeightFraction) / 2

    val roadTop = config.canvasConfig.height * config.skyConfig.heightFraction + roadPadding
    val roadBottom = config.canvasConfig.height - roadPadding
    return roadTop + (roadBottom - roadTop) / 4
}

private fun getSecondLineCenterY(config: GameConfig): Float {
    val roadHeight = config.canvasConfig.height * config.roadConfig.heightFraction
    return getFirstLineCenterY(config) + roadHeight / 2
}
