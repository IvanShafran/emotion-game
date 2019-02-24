package com.github.ivanshafran.emotiongame.camera

data class FaceFeatures(
    val smileProbability: Float,
    val leftEyeBlinkProbability: Float,
    val rightEyeBlinkProbability: Float
)
