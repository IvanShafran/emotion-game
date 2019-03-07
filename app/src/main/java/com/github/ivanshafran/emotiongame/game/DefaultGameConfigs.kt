package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.R

private const val SKY_FRACTION = 1f / 3f
private const val FULL_ROAD_FRACTION = 1f - SKY_FRACTION

val defaultSkyConfig = SkyConfig(
    heightFraction = SKY_FRACTION,
    dayColor = R.color.game_sky_day,
    nightColor = R.color.game_sky_night,
    cloudConfigs = listOf(
        CloudConfig(
            drawableRes = R.drawable.cloud_1,
            heightFraction = SKY_FRACTION * 0.2f,
            widthToHeightAspectRatio = 2f,
            speedDimenRes = R.dimen.cloud_1_speed,
            xWidthFraction = 0.2f,
            yHeightFraction = SKY_FRACTION * 0.5f
        ),
        CloudConfig(
            drawableRes = R.drawable.cloud_2,
            heightFraction = SKY_FRACTION * 0.15f,
            widthToHeightAspectRatio = 2f,
            speedDimenRes = R.dimen.cloud_2_speed,
            xWidthFraction = 0.4f,
            yHeightFraction = SKY_FRACTION * 0.1f
        ),
        CloudConfig(
            drawableRes = R.drawable.cloud_3,
            heightFraction = SKY_FRACTION * 0.3f,
            widthToHeightAspectRatio = 2f,
            speedDimenRes = R.dimen.cloud_3_speed,
            xWidthFraction = 0.7f,
            yHeightFraction = SKY_FRACTION * 0.3f
        )
    )
)

val defaultSunConfig = SunConfig(
    smileNotBlinkRes = R.drawable.sun_smile_not_blink,
    smileBlinkRes = R.drawable.sun_smile_blink,
    notSmileNotBlinkRes = R.drawable.sun_not_smile_not_blink,
    notSmileBlinkRes = R.drawable.sun_not_smile_blink,
    heightFraction = SKY_FRACTION * 0.8f,
    widthToHeightAspectRatio = 1f
)

val defaultRoadConfig = RoadConfig(
    doubleGrassHeightFraction = FULL_ROAD_FRACTION * 0.25f,
    grassColor = R.color.game_road_grass,
    doubleBorderHeightFraction = FULL_ROAD_FRACTION * 0.05f,
    borderColor = R.color.game_road_border,
    heightFraction = FULL_ROAD_FRACTION * 0.7f,
    color = R.color.game_road,
    lineHeightFraction = FULL_ROAD_FRACTION * 0.05f,
    lineColor = R.color.game_road_line,
    lineWidthToHeightAspectRatio = 5f,
    lineSkipFraction = 0.1f
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
    animationFrameSkipCount = 20,
    widthFractionStartOffset = 2f,
    enemyWidthIntersectOffsetFraction = 0.2f
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
        SpeedMultiplier(1.0f, 10000L),
        SpeedMultiplier(1.5f, 30000L),
        SpeedMultiplier(2.0f, 45000L),
        SpeedMultiplier(2.5f, 60000L),
        SpeedMultiplier(3.0f, 90000L),
        SpeedMultiplier(4.0f, 120000L),
        SpeedMultiplier(5.0f, 180000L)
    )
)
