package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.R

private const val SKY_FRACTION = 1f / 3f
private const val FULL_ROAD_FRACTION = 1f - SKY_FRACTION

val defaultSkyConfig = SkyConfig(
    heightFraction = SKY_FRACTION,
    dayColor = R.color.game_sky_day,
    nightColor = R.color.game_sky_night,
    cloudDrawableResList = listOf(
        R.drawable.cloud_1,
        R.drawable.cloud_2,
        R.drawable.cloud_3
    ),
    cloudHeightFractions = listOf(
        SKY_FRACTION * 0.2f,
        SKY_FRACTION * 0.15f,
        SKY_FRACTION * 0.3f
    ),
    cloudWidthToHeightAspectRatios = listOf(
        2f,
        2f,
        2f
    )
)

val defaultSunConfig = SunConfig(
    smileNotBlinkRes = R.drawable.sun_smile_not_blink,
    smileBlinkRes = R.drawable.sun_smile_blink,
    notSmileNotBlinkRes = R.drawable.sun_not_smile_not_blink,
    notSmileBlinkRes = R.drawable.sun_not_smile_blink,
    heightFraction = SKY_FRACTION * 0.4f,
    widthToHeightAspectRatio = 1f
)

val defaultRoadConfig = RoadConfig(
    doubleGrassHeightFraction = FULL_ROAD_FRACTION * 0.25f,
    grassColor = R.color.game_road_grass,
    doubleBorderHeightFraction = FULL_ROAD_FRACTION * 0.05f,
    borderColor = R.color.game_road_border,
    heightFraction = FULL_ROAD_FRACTION * 0.7f,
    color = R.color.game_road,
    lineHeightFraction = FULL_ROAD_FRACTION * 0.15f,
    lineColor = R.color.game_road_line,
    lineWidthToHeightAspectRatio = 8f,
    lineSkipFraction = 0.5f
)

val defaultPlayerConfig = PlayerConfig(
    speedDimenRes = R.dimen.player_speed,
    heightFraction = FULL_ROAD_FRACTION * 0.3f,
    widthToHeightAspectRatio = 1f,
    animationDrawableResList = listOf(
        R.drawable.nyan_cat_1,
        R.drawable.nyan_cat_2
    ),
    animationFrameSkipCount = 20,
    startMarginWidthFraction = 0.05f
)

val defaultEnemyConfig = EnemyConfig(
    speedDimenRes = R.dimen.enemy_speed,
    heightFraction = FULL_ROAD_FRACTION * 0.3f,
    widthToHeightAspectRatio = 1f,
    animationDrawableResList = listOf(
        R.drawable.dog_1,
        R.drawable.dog_2

    ),
    animationFrameSkipCount = 20
)

val defaultScoreConfig = ScoreConfig(
    textSize = R.dimen.score_text_size,
    textColor = R.color.score_text_color,
    stringRes = R.string.score,
    heightFraction = SKY_FRACTION * 0.3f,
    widthToHeightAspectRatio = 10f,
    dayBonusScore = 1,
    nightBonusScore = 2
)

val defaultLifeConfig = LifeConfig(
    textSize = R.dimen.life_text_size,
    textColor = R.color.life_text_color,
    stringRes = R.string.life,
    initLifeCount = 3,
    heightFraction = SKY_FRACTION * 0.3f,
    widthToHeightAspectRatio = 10f
)

val defaultBonusConfig = BonusConfig(
    speedDimenRes = R.dimen.bonus_speed,
    heightFraction = FULL_ROAD_FRACTION * 0.15f,
    widthToHeightAspectRatio = 1f,
    drawableResList = listOf(
        R.drawable.cake,
        R.drawable.bottle
    )
)

val defaultSpeedMultiplierConfig = SpeedMultiplierConfig(
    multipliers = listOf(
        SpeedMultiplier(1f, 0L),
        SpeedMultiplier(1.5f, 10000L),
        SpeedMultiplier(2f, 20000L),
        SpeedMultiplier(2.5f, 30000L),
        SpeedMultiplier(3f, 40000L),
        SpeedMultiplier(5f, 50000L)
    )
)
