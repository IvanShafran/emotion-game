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
            filepath = "cloud_1.png",
            heightFraction = SKY_FRACTION * 0.2f,
            widthToHeightAspectRatio = 2f,
            speedDimenRes = R.dimen.cloud_1_speed,
            xWidthFraction = 0.2f,
            yHeightFraction = SKY_FRACTION * 0.5f
        ),
        CloudConfig(
            filepath = "cloud_2.png",
            heightFraction = SKY_FRACTION * 0.15f,
            widthToHeightAspectRatio = 2f,
            speedDimenRes = R.dimen.cloud_2_speed,
            xWidthFraction = 0.4f,
            yHeightFraction = SKY_FRACTION * 0.1f
        ),
        CloudConfig(
            filepath = "cloud_3.png",
            heightFraction = SKY_FRACTION * 0.3f,
            widthToHeightAspectRatio = 2f,
            speedDimenRes = R.dimen.cloud_3_speed,
            xWidthFraction = 0.7f,
            yHeightFraction = SKY_FRACTION * 0.3f
        )
    )
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
    animationFilepathList = listOf(
        "cat/cat_run_0.png",
        "cat/cat_run_1.png",
        "cat/cat_run_2.png",
        "cat/cat_run_3.png",
        "cat/cat_run_4.png",
        "cat/cat_run_5.png",
        "cat/cat_run_6.png",
        "cat/cat_run_7.png"
    ),
    animationFrameSkipCount = 5,
    startMarginWidthFraction = 0.05f
)

val defaultEnemyConfig = EnemyConfig(
    speedDimenRes = R.dimen.enemy_speed,
    heightFraction = FULL_ROAD_FRACTION * 0.3f,
    widthToHeightAspectRatio = 1f,
    animationFilepathLists = listOf(
        listOf(
            "zombie_girl/zombie_girl_0.png",
            "zombie_girl/zombie_girl_1.png",
            "zombie_girl/zombie_girl_2.png",
            "zombie_girl/zombie_girl_3.png",
            "zombie_girl/zombie_girl_4.png",
            "zombie_girl/zombie_girl_5.png",
            "zombie_girl/zombie_girl_6.png",
            "zombie_girl/zombie_girl_7.png",
            "zombie_girl/zombie_girl_8.png",
            "zombie_girl/zombie_girl_9.png"
        ),
        listOf(
            "zombie_boy/zombie_boy_0.png",
            "zombie_boy/zombie_boy_1.png",
            "zombie_boy/zombie_boy_2.png",
            "zombie_boy/zombie_boy_3.png",
            "zombie_boy/zombie_boy_4.png",
            "zombie_boy/zombie_boy_5.png",
            "zombie_boy/zombie_boy_6.png",
            "zombie_boy/zombie_boy_7.png",
            "zombie_boy/zombie_boy_8.png",
            "zombie_boy/zombie_boy_9.png"
        )
    ),
    animationFrameSkipCount = 7,
    widthFractionStartOffset = 2f,
    widthIntersectStartOffsetFraction = 0.45f,
    widthIntersectEndOffsetFraction = 0.45f
)

val defaultScoreConfig = ScoreConfig(
    textSize = R.dimen.score_text_size,
    textColor = R.color.button_and_text_colors,
    stringRes = R.string.score,
    heightFraction = SKY_FRACTION * 0.3f,
    widthToHeightAspectRatio = 10f,
    dayBonusScore = 1,
    nightBonusScore = 2,
    marginStartRes = R.dimen.score_margin_start,
    marginTopRes = R.dimen.score_margin_top
)

val defaultLifeConfig = LifeConfig(
    fullHeartPath = "full_heart.png",
    emptyHeartPath = "empty_heart.png",
    initLifeCount = 3,
    heightFraction = SKY_FRACTION * 0.2f,
    widthToHeightAspectRatio = 1f,
    marginBottomRes = R.dimen.life_margin_bottom,
    marginStartRes = R.dimen.life_margin_start
)

val defaultBonusConfig = BonusConfig(
    speedDimenRes = R.dimen.bonus_speed,
    heightFraction = FULL_ROAD_FRACTION * 0.15f,
    widthToHeightAspectRatio = 1f,
    cakeEmojis = listOf(
        "\uD83C\uDF66",
        "\uD83C\uDF67",
        "\uD83C\uDF68",
        "\uD83C\uDF69",
        "\uD83C\uDF70",
        "\uD83C\uDF6D",
        "\uD83C\uDF82",
        "\uD83E\uDDC1"
    ),
    textColor = R.color.cake_color,
    textSize = R.dimen.cake_text_size,
    widthFractionStartOffset = 1f,
    widthIntersectOffsetFraction = 0.5f
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
