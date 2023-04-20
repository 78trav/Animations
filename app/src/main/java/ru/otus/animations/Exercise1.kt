package ru.otus.animations

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import kotlin.math.min
import kotlin.math.roundToInt

private const val EXERCISE_ONE_DURATION = 2000
private const val DISTANCE_BETWEEN_CIRCLES = 15
private const val APPROXIMATION_FACTOR = .2f

class Circle(var cx: Int, var cy: Int, var radius: Int, val paint: Paint) {

    fun draw(canvas: Canvas) {
        canvas.drawCircle(cx.toFloat(), cy.toFloat(), radius.toFloat(), paint)
    }
}

class Exercise1 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var duration = EXERCISE_ONE_DURATION
    var repeatCount = 1

    private var isAnimated = false

    private fun setupInitialValues(baseX: Int, baseY: Int) {
        val r = (min(baseX, baseY) * (1 - APPROXIMATION_FACTOR)).toInt()
        var d = baseX - r - ((r / 50 * DISTANCE_BETWEEN_CIRCLES) shr 1)

        c1.cx = baseX + d
        c1.cy = baseY
        c1.radius = r

        c2.cx = baseX * 3 - d
        c2.cy = baseY
        c2.radius = r
    }


    val c1: Circle = Circle(
        0,
        0,
        0,
        Paint().apply {
            color = Color.CYAN
            style = Paint.Style.FILL
        }
    )

    val c2: Circle = Circle(
        0,
        0,
        0,
        Paint().apply {
            color = Color.MAGENTA
            style = Paint.Style.FILL
        }
    )

    init {
        context.withStyledAttributes(attrs, R.styleable.ExerciseOneViewParams) {
            c1.paint.color = this.getColor(R.styleable.ExerciseOneViewParams_color1, Color.CYAN)
            c2.paint.color = this.getColor(R.styleable.ExerciseOneViewParams_color2, Color.MAGENTA)
            duration = this.getInt(R.styleable.ExerciseOneViewParams_duration, EXERCISE_ONE_DURATION)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        c1.draw(canvas)
        c2.draw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = 100 + DISTANCE_BETWEEN_CIRCLES + 100 + paddingLeft + paddingRight
        val h = 100 + (100 * APPROXIMATION_FACTOR).roundToInt() + paddingTop + paddingBottom

        val rw = resolveSize(w, widthMeasureSpec)
        val rh = resolveSize(h, heightMeasureSpec)

        setMeasuredDimension(rw, rh)

        setupInitialValues((rw - paddingLeft - paddingRight) shr 2, (rh - paddingTop - paddingBottom) shr 1)

        /*
        c1.cx = (rw - paddingLeft - paddingRight) shr 2
        c1.cy = (rh - paddingTop - paddingBottom) shr 1
        c1.radius = ((min(c1.cx, c1.cy) - DISTANCE_BETWEEN_CIRCLES) * .8).toInt()

        c2.cx = c1.cx * 3
        c2.cy = c1.cy
        c2.radius = c1.radius

         */

    }

    private fun getChanges(baseValue: Int, values: IntArray): IntArray {
        var i = -1
        return IntArray(values.size) {
            i++
            baseValue + values[i]
        }
    }

    fun startAnimation() {

        if (isAnimated) return

        isAnimated = true


        val w = (width - paddingLeft - paddingRight) shr 1
        val h = (height - paddingTop - paddingBottom) shr 1

        setupInitialValues(w shr 1, h)

        //val x = c1.cx

        /*

        ValueAnimator.ofPropertyValuesHolder(
            *arrayOf(
                PropertyValuesHolder.ofInt("c1x", *intArrayOf(c1.cx, w, c2.cx, c2.cx * 95 / 100, w, c1.cx)),
                PropertyValuesHolder.ofInt("c1radius", *getChanges(c1.radius, intArrayOf(0, (c1.radius * APPROXIMATION_FACTOR).toInt(), 0, 0, 0, 0))),
                PropertyValuesHolder.ofInt("c2x", *intArrayOf(c2.cx, w, c1.cx, c1.cx, w, c2.cx)),
                PropertyValuesHolder.ofInt("c2y", *getChanges(c2.cy, intArrayOf(0, -(c2.cy * APPROXIMATION_FACTOR).toInt(), 0, 0, 0, 0))),
                PropertyValuesHolder.ofInt("c2radius", *getChanges(c2.radius, intArrayOf(0, -(c2.radius * APPROXIMATION_FACTOR).toInt(), 0, 0, (c2.radius * APPROXIMATION_FACTOR).toInt(), 0))),
                PropertyValuesHolder.ofObject("c2color", ArgbEvaluator(), c2.paint.color, c2.paint.color and 0x00FFFFFF, c2.paint.color, c2.paint.color, c2.paint.color, c2.paint.color),
                PropertyValuesHolder.ofInt("c1onTop", 1, 1, 0, 0, 0)

                /*
                PropertyValuesHolder.ofInt("c1x", *getChanges(c1.cx, intArrayOf(0, x, x + x, x + x * 9 / 10, x, 0))),
                PropertyValuesHolder.ofInt("c1radius", *getChanges(c1.radius, intArrayOf(0, (c1.radius * APPROXIMATION_FACTOR).toInt(), 0, 0, 0, 0))),
                PropertyValuesHolder.ofInt("c2x", *getChanges(c2.cx, intArrayOf(0, -x, -x - x, -x - x, -x, 0))),
                PropertyValuesHolder.ofInt("c2y", *getChanges(c2.cy, intArrayOf(0, -(c2.cy * APPROXIMATION_FACTOR).toInt(), 0, 0, 0, 0))),
                PropertyValuesHolder.ofInt("c2radius", *getChanges(c2.radius, intArrayOf(0, -(c2.radius * APPROXIMATION_FACTOR).toInt(), 0, 0, (c2.radius * APPROXIMATION_FACTOR).toInt(), 0))),
                PropertyValuesHolder.ofObject("c2color", ArgbEvaluator(), c2.paint.color, c2.paint.color and 0x00FFFFFF, c2.paint.color, c2.paint.color, c2.paint.color, c2.paint.color),
                PropertyValuesHolder.ofInt("c1onTop", 1, 1, 0, 0, 0)

                 */
            )
        )
        .apply {
            this.duration = this@Exercise1.duration.toLong()
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = 2
            addUpdateListener {
                c1.cx = it.getAnimatedValue("c1x") as Int
                c1.radius = it.getAnimatedValue("c1radius") as Int

                c2.cx = it.getAnimatedValue("c2x") as Int
                c2.cy = it.getAnimatedValue("c2y") as Int
                c2.radius = it.getAnimatedValue("c2radius") as Int
                c2.paint.color = it.getAnimatedValue("c2color") as Int

                c1onTop = (it.getAnimatedValue("c1onTop") as Int == 1)
                invalidate()
            }

            addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
                    isAnimated = true
                }
                override fun onAnimationRepeat(p0: Animator) {
                }
                override fun onAnimationCancel(p0: Animator) {
                }
                override fun onAnimationEnd(p0: Animator) {
                    isAnimated = false
                }
            })
            start()
        }

        */

        val f = 0xA0000000
        var clr = c2.paint.color and 0x00FFFFFF or f.toInt()

        val c2anim = listOf<Animator>(
            ValueAnimator.ofPropertyValuesHolder(
                *arrayOf(
                    PropertyValuesHolder.ofInt("c2x", *intArrayOf(c2.cx, w + (w shr 2))),
                    PropertyValuesHolder.ofInt("c2y", *getChanges(c2.cy, intArrayOf(0, -(c2.cy * APPROXIMATION_FACTOR * 2).toInt()))),
                    PropertyValuesHolder.ofInt("c2radius", *getChanges(c2.radius, intArrayOf(0, -(c2.radius * APPROXIMATION_FACTOR * 2).toInt()))),
                    PropertyValuesHolder.ofObject("c2color", ArgbEvaluator(), c2.paint.color, clr)
                )
            ).apply {
                duration = this@Exercise1.duration.toLong() shr 3
                interpolator = LinearInterpolator()
                addUpdateListener {
                    c2.cx = it.getAnimatedValue("c2x") as Int
                    c2.cy = it.getAnimatedValue("c2y") as Int
                    c2.radius = it.getAnimatedValue("c2radius") as Int
                    c2.paint.color = it.getAnimatedValue("c2color") as Int

                    invalidate()
                }
            },
            ValueAnimator.ofPropertyValuesHolder(
                *arrayOf(
                    PropertyValuesHolder.ofInt("c2x", *intArrayOf(w + (w shr 2), w)),
                    PropertyValuesHolder.ofObject("c2color", ArgbEvaluator(), clr, c2.paint.color and 0x00FFFFFF)
                )
            ).apply {
                duration = this@Exercise1.duration.toLong() shr 3
                interpolator = LinearInterpolator()
                addUpdateListener {
                    c2.cx = it.getAnimatedValue("c2x") as Int
                    c2.paint.color = it.getAnimatedValue("c2color") as Int

                    invalidate()
                }
            },
            ValueAnimator.ofPropertyValuesHolder(
                *arrayOf(
                    PropertyValuesHolder.ofInt("c2x", *intArrayOf(w, w - (w shr 2))),
                )
            ).apply {
                duration = this@Exercise1.duration.toLong() shr 3
                interpolator = LinearInterpolator()
                addUpdateListener {
                    c2.cx = it.getAnimatedValue("c2x") as Int

                    invalidate()
                }
            },
            ValueAnimator.ofPropertyValuesHolder(
                *arrayOf(
                    PropertyValuesHolder.ofInt("c2x", *intArrayOf(w - (w shr 2), c1.cx)),
                    PropertyValuesHolder.ofInt("c2y", *getChanges(c2.cy, intArrayOf(-(c2.cy * APPROXIMATION_FACTOR * 2).toInt(), 0))),
                    PropertyValuesHolder.ofInt("c2radius", *getChanges(c2.radius, intArrayOf(-(c2.radius * APPROXIMATION_FACTOR * 2).toInt(), 0))),
                    PropertyValuesHolder.ofObject("c2color", ArgbEvaluator(), c2.paint.color and 0x00FFFFFF, c2.paint.color)
                )
            ).apply {
                duration = this@Exercise1.duration.toLong() shr 3
                interpolator = AccelerateInterpolator()
                addUpdateListener {
                    c2.cx = it.getAnimatedValue("c2x") as Int
                    c2.cy = it.getAnimatedValue("c2y") as Int
                    c2.radius = it.getAnimatedValue("c2radius") as Int
                    c2.paint.color = it.getAnimatedValue("c2color") as Int

                    invalidate()
                }
            },

            ValueAnimator.ofPropertyValuesHolder(
                *arrayOf(
                    PropertyValuesHolder.ofInt("c2x", *intArrayOf(c1.cx, w, c2.cx)),
                    PropertyValuesHolder.ofInt("c2radius", *getChanges(c2.radius, intArrayOf(0, (c2.radius * APPROXIMATION_FACTOR / 2).toInt(), 0)))
                )
            ).apply {
                duration = this@Exercise1.duration.toLong() shr 1
                interpolator = AccelerateDecelerateInterpolator()
                addUpdateListener {
                    c2.cx = it.getAnimatedValue("c2x") as Int
                    c2.radius = it.getAnimatedValue("c2radius") as Int

                    invalidate()
                }
            }
        )

        val c1anim = listOf<Animator>(
            ValueAnimator.ofPropertyValuesHolder(
                *arrayOf(
                    PropertyValuesHolder.ofInt("c1x", *intArrayOf(c1.cx, w, c2.cx)),
                    PropertyValuesHolder.ofInt("c1radius", *getChanges(c1.radius, intArrayOf(0, (c1.radius * APPROXIMATION_FACTOR / 2).toInt(), 0)))
                )
            ).apply {
                duration = this@Exercise1.duration.toLong() shr 1
                interpolator = AccelerateInterpolator()
                addUpdateListener {
                    c1.cx = it.getAnimatedValue("c1x") as Int
                    c1.radius = it.getAnimatedValue("c1radius") as Int

                    invalidate()
                }
            },
            ValueAnimator.ofPropertyValuesHolder(
                *arrayOf(
                    PropertyValuesHolder.ofInt("c1x", *intArrayOf(c2.cx, w, c1.cx))
                )
            ).apply {
                duration = this@Exercise1.duration.toLong() shr 1
                interpolator = AccelerateInterpolator(1.5f)
                addUpdateListener {
                    c1.cx = it.getAnimatedValue("c1x") as Int

                    invalidate()
                }
            }
        )

        AnimatorSet().apply {
            playSequentially(c2anim)
            playSequentially(c1anim)
            //playTogether(animationC11)
            addListener(object: Animator.AnimatorListener {
                var count = repeatCount
                override fun onAnimationStart(p0: Animator) {
                }
                override fun onAnimationRepeat(p0: Animator) {
                }
                override fun onAnimationCancel(p0: Animator) {
                }
                override fun onAnimationEnd(p0: Animator) {
                    if (count < 2) isAnimated = false
                        else {
                            count--
                            start()
                        }
                }
            })
            start()
        }

    }

}
