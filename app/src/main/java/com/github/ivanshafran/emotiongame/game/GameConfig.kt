package com.github.ivanshafran.emotiongame.game

import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.StringRes

data class CanvasConfig(
    val width: Int, // pixels
    val height: Int // pixels
)

data class SkyConfig(
    val heightFraction: Float,
    @ColorRes val dayColor: Int,
    @ColorRes val nightColor: Int,
    val cloudConfigs: List<CloudConfig>
)

data class CloudConfig(
    val filepath: String,
    val heightFraction: Float,
    val widthToHeightAspectRatio: Float,
    val speedDimenRes: Int,
    val xWidthFraction: Float,
    val yHeightFraction: Float
)

data class RoadConfig(
    val doubleGrassHeightFraction: Float,
    @ColorRes val grassColor: Int,
    val doubleBorderHeightFraction: Float,
    @ColorRes val borderColor: Int,
    val heightFraction: Float,
    @ColorRes val color: Int,
    val lineHeightFraction: Float,
    @ColorRes val lineColor: Int,
    val lineWidthToHeightAspectRatio: Float,
    val lineSkipFraction: Float
)

data class PlayerConfig(
    @DimenRes val speedDimenRes: Int, // per second
    val heightFraction: Float,
    val widthToHeightAspectRatio: Float,
    val animationFilepathList: List<String>,
    val animationFrameSkipCount: Int,
    val startMarginWidthFraction: Float
)

data class EnemyConfig(
    @DimenRes val speedDimenRes: Int,
    val heightFraction: Float,
    val widthToHeightAspectRatio: Float,
    val animationFilepathLists: List<List<String>>,
    val animationFrameSkipCount: Int,
    val widthFractionStartOffset: Float,
    val widthIntersectStartOffsetFraction: Float,
    val widthIntersectEndOffsetFraction: Float
)

data class ScoreConfig(
    @DimenRes val textSize: Int,
    @ColorRes val textColor: Int,
    @StringRes val stringRes: Int,
    val heightFraction: Float,
    val widthToHeightAspectRatio: Float,
    val dayBonusScore: Int,
    val nightBonusScore: Int,
    @DimenRes val marginTopRes: Int,
    @DimenRes val marginStartRes: Int
)

data class LifeConfig(
    val fullHeartPath: String,
    val emptyHeartPath: String,
    val heightFraction: Float,
    val widthToHeightAspectRatio: Float,
    val initLifeCount: Int,
    @DimenRes val marginBottomRes: Int,
    @DimenRes val marginStartRes: Int
)

data class BonusConfig(
    @DimenRes val speedDimenRes: Int,
    val heightFraction: Float,
    val widthToHeightAspectRatio: Float,
    val widthFractionStartOffset: Float,
    val widthIntersectOffsetFraction: Float,
    val cakeEmojis: List<String>,
    @DimenRes val textSize: Int,
    @ColorRes val textColor: Int
)

data class SpeedMultiplier(
    val multiplier: Float,
    val timeFromStartInMillis: Long
)

data class SpeedMultiplierConfig(
    val multipliers: List<SpeedMultiplier>
)

data class GameConfig(
    val canvasConfig: CanvasConfig,
    val skyConfig: SkyConfig,
    val roadConfig: RoadConfig,
    val playerConfig: PlayerConfig,
    val enemyConfig: EnemyConfig,
    val scoreConfig: ScoreConfig,
    val lifeConfig: LifeConfig,
    val bonusConfig: BonusConfig,
    val speedMultiplier: SpeedMultiplierConfig
) {

    init {
        val backgroundHeightFraction = skyConfig.heightFraction +
                roadConfig.doubleBorderHeightFraction +
                roadConfig.doubleGrassHeightFraction +
                roadConfig.heightFraction

        if (1f.compareTo(backgroundHeightFraction) == 0) {
            throw IllegalStateException("Background fractions sum should be equal to 1")
        }
    }

}
