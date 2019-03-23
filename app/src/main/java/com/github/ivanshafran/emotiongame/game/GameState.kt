package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.game.game_object.GameObject

data class Score(
    var value: Int,
    var gameObject: GameObject
)

data class Life(
    var value: Int,
    var firstHeart: GameObject,
    val secondHeart: GameObject,
    val thirdHeart: GameObject
)

data class Player(
    var gameObject: GameObject,
    val speedPerMillis: Float
)

data class Enemy(
    var gameObject: GameObject,
    val speedPerMillis: Float
)

data class Bonus(
    var gameObject: GameObject,
    val speedPerMillis: Float
)

data class Sky(
    var background: GameObject,
    var clouds: List<Cloud>,
    var stars: List<GameObject>,
    var isDay: Boolean = true
)

data class Cloud(
    val gameObject: GameObject,
    val speedPerMillis: Float
)

data class Road(
    var road: GameObject,
    var upGrass: GameObject,
    var downGrass: GameObject,
    var upBorder: GameObject,
    var downBorder: GameObject,
    var lineDividers: List<GameObject>
)

data class GameState(
    val score: Score,
    val life: Life,
    val player: Player,
    val enemy: Enemy,
    val bonus: Bonus,
    val sky: Sky,
    val road: Road
)
