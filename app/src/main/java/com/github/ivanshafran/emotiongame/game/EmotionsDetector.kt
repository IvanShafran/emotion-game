package com.github.ivanshafran.emotiongame.game

import com.github.ivanshafran.emotiongame.camera.facedetection.FaceFeatures

class EmotionsDetector {

    companion object {
        private const val SMILE_THRESHOLD = 0.7f
        private const val EYE_BLINK_THRESHOLD = 0.1f
        private const val EYE_BLINK_TIME_THRESHOLD = 150 // ms
    }

    private var isSmile = false
    private var firstBlinkDetectedTimeInMillis: Long? = null

    fun detect(features: FaceFeatures?): Emotions {
        val isBlink = isBlink(features)
        val isSmile = isSmile(features)
        return Emotions(isSmile = isSmile, isBlink = isBlink)
    }

    private fun isBlink(features: FaceFeatures?): Boolean {
        if (!isBlinkDetected(features)) {
            firstBlinkDetectedTimeInMillis = null
            return false
        } else {
            val firstTime = firstBlinkDetectedTimeInMillis
            val nowTime = System.currentTimeMillis()
            return if (firstTime == null) {
                firstBlinkDetectedTimeInMillis = nowTime
                false
            } else {
                (nowTime - firstTime) > EYE_BLINK_TIME_THRESHOLD
            }
        }
    }

    private fun isBlinkDetected(features: FaceFeatures?): Boolean {
        return features != null && features.leftEyeOpenProbability < EYE_BLINK_THRESHOLD &&
                features.rightEyeOpenProbability < EYE_BLINK_THRESHOLD
    }

    private fun isSmile(features: FaceFeatures?): Boolean {
        if (features != null) {
            isSmile = features.smileProbability > SMILE_THRESHOLD
        }
        return isSmile
    }

}
