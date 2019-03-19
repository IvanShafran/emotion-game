package com.github.ivanshafran.emotiongame.game.drawer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.support.annotation.ColorRes
import android.support.annotation.DimenRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import com.github.ivanshafran.emotiongame.game.GameState
import com.github.ivanshafran.emotiongame.game.game_object.*

class Drawer(private val context: Context) {

    private val bitmapCache: MutableMap<Int, Bitmap> = mutableMapOf()
    private val colorPaintCache: MutableMap<Int, Paint> = mutableMapOf()

    private data class TextPaintKey(val color: Int, val size: Int)

    private val textPaintCache: MutableMap<TextPaintKey, Paint> = mutableMapOf()

    fun draw(canvas: Canvas, gameState: GameState) {
        drawGameObject(canvas, gameState.sky.background)
        if (gameState.sky.isDay) {
            gameState.sky.clouds.forEach { drawGameObject(canvas, it.gameObject) }
        } else {
            gameState.sky.stars.forEach { drawGameObject(canvas, it) }
        }

        drawGameObject(canvas, gameState.road.road)
        drawGameObject(canvas, gameState.road.upBorder)
        drawGameObject(canvas, gameState.road.downBorder)
        drawGameObject(canvas, gameState.road.upGrass)
        drawGameObject(canvas, gameState.road.downGrass)
        gameState.road.lineDividers.forEach { drawGameObject(canvas, it) }

        drawGameObject(canvas, gameState.player.gameObject)

        drawGameObject(canvas, gameState.enemy.gameObject)

        drawGameObject(canvas, gameState.bonus.gameObject)

        drawGameObject(canvas, gameState.life.gameObject)

        drawGameObject(canvas, gameState.score.gameObject)
    }

    private fun drawGameObject(canvas: Canvas, gameObject: GameObject) {
        val drawable = gameObject.drawable
        when (drawable) {
            is ColorDrawable -> drawColorDrawable(canvas, drawable, gameObject)
            is BitmapDrawable -> drawBitmapDrawable(canvas, drawable, gameObject)
            is AnimatedBitmapDrawable -> drawAnimatedBitmapDrawable(canvas, drawable, gameObject)
            is TextDrawable -> drawTextDrawable(canvas, drawable, gameObject)
        }
    }

    private fun drawTextDrawable(canvas: Canvas, drawable: TextDrawable, gameObject: GameObject) {
        val paint = getTextPaint(drawable.textColor, drawable.textSize)

        val centerX = gameObject.rect.x
        val centerY = gameObject.rect.y + gameObject.rect.height

        canvas.drawText(drawable.text, centerX, centerY, paint)
    }

    private fun getTextPaint(@ColorRes textColor: Int, @DimenRes textSize: Int): Paint {
        val size = context.resources.getDimensionPixelSize(textSize)
        val color = ContextCompat.getColor(context, textColor)
        return textPaintCache.getOrPut(TextPaintKey(color, size)) {
            Paint()
                .apply { isAntiAlias = true }
                .apply { setColor(color) }
                .apply { setTextSize(size.toFloat()) }
                .apply { textAlign = Paint.Align.LEFT }
        }
    }

    private fun drawAnimatedBitmapDrawable(canvas: Canvas, drawable: AnimatedBitmapDrawable, gameObject: GameObject) {
        canvas.drawBitmap(
            getBitmap(drawable.getNextDrawableRes(), gameObject.rect.width, gameObject.rect.height),
            gameObject.rect.x,
            gameObject.rect.y,
            null
        )
    }

    private fun drawBitmapDrawable(canvas: Canvas, drawable: BitmapDrawable, gameObject: GameObject) {
        canvas.drawBitmap(
            getBitmap(drawable.bitmapDrawableRes, gameObject.rect.width, gameObject.rect.height),
            gameObject.rect.x,
            gameObject.rect.y,
            null
        )
    }

    private fun getBitmap(@DrawableRes res: Int, width: Int, height: Int): Bitmap {
        return bitmapCache.getOrPut(res) {
            BitmapLoader.load(
                context,
                res,
                width,
                height
            )
        }
    }

    private fun drawColorDrawable(canvas: Canvas, drawable: ColorDrawable, gameObject: GameObject) {
        canvas.drawRect(gameObject.rect.toAndroidRect(), getColorPaint(drawable.colorRes))
    }

    private fun getColorPaint(@ColorRes color: Int): Paint {
        return colorPaintCache.getOrPut(color) {
            Paint()
                .apply { style = Paint.Style.FILL_AND_STROKE }
                .apply { setColor(ContextCompat.getColor(context, color)) }
        }
    }

    private fun Rect.toAndroidRect(): android.graphics.Rect {
        return android.graphics.Rect(
            x.toInt(),
            y.toInt(),
            x.toInt() + width,
            y.toInt() + height
        )
    }
}
