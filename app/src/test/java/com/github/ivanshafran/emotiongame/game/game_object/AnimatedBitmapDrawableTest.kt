package com.github.ivanshafran.emotiongame.game.game_object

import org.hamcrest.CoreMatchers.equalTo
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.Assert.assertThat

class AnimatedBitmapDrawableTest : Spek({
    describe("drawable object") {
        on("first call") {
            val drawableObject = AnimatedBitmapDrawable(listOf(0, 1), 1)
            it("should return first drawable res") {
                assertThat(drawableObject.getNextDrawableRes(), equalTo(0))
            }
        }

        on("second call") {
            val drawableObject = AnimatedBitmapDrawable(listOf(0, 1), 1)
            drawableObject.getNextDrawableRes()

            it("should return second drawable res") {
                assertThat(drawableObject.getNextDrawableRes(), equalTo(1))
            }
        }

        on("call after last drawable res") {
            val drawableObject = AnimatedBitmapDrawable(listOf(0, 1), 1)
            drawableObject.getNextDrawableRes()
            drawableObject.getNextDrawableRes()

            it("should return first drawable res by loop rule") {
                assertThat(drawableObject.getNextDrawableRes(), equalTo(0))
            }
        }
    }
})
