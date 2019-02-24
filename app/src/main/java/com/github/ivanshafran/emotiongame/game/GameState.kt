package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.game.game_object.GameObject

data class Score(
    var value: Int,
    var gameObject: GameObject
)

data class Life(
    var value: Int,
    var gameObject: GameObject
)

data class Player(
    var gameObject: GameObject
)

data class Enemy(
    var gameObject: GameObject
)

data class Bonus(
    var gameObject: GameObject
)

data class Sky(
    var background: GameObject,
    var clouds: List<GameObject>,
    var stars: List<GameObject>
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
    var score: Score,
    var life: Life,
    var player: Player,
    var enemy: Enemy,
    var bonus: Bonus,
    var sky: Sky,
    var road: Road
)
