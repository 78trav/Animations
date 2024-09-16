package ru.otus.animations

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import java.lang.Integer.max
import kotlin.math.min

private const val EXERCISE_2_DURATION = 2000
private const val DEFAULT_CIRCLES_COUNT = 4

class Exercise2  @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var circlesCount = DEFAULT_CIRCLES_COUNT
    private var isAnimated = false
    private val circles = ArrayList<Circle>(0)
    private var mainColor: Int = Color.CYAN

    var duration = EXERCISE_2_DURATION
    var repeatCount = 1

    init {
        context.withStyledAttributes(attrs, R.styleable.CommonViewParams) {
            duration = this.getInt(R.styleable.CommonViewParams_duration, EXERCISE_2_DURATION)
        }
        context.withStyledAttributes(attrs, R.styleable.Exercise2ViewParams) {
            mainColor = this.getColor(R.styleable.Exercise2ViewParams_main_color, Color.CYAN)
            circlesCount = this.getColor(R.styleable.Exercise2ViewParams_circles, DEFAULT_CIRCLES_COUNT)
            if (circlesCount !in 3..10) circlesCount = DEFAULT_CIRCLES_COUNT
        }
    }

    private fun setupInitialValues(w: Int, h: Int) {

        if (circlesCount !in 3..10) circlesCount = DEFAULT_CIRCLES_COUNT

        val c = min(w, h) shr 1
        var r = c
        val d = r / circlesCount

        circles.clear()

        (1..circlesCount).forEach() { _ ->
            circles.add(
                Circle(
                    w shr 1,
                    h shr 1,
                    r,
                    Paint().apply {
                        style = Paint.Style.FILL
                        color = mainColor and 0x00FFFFFF or ((255 - 255 * r / c) shl 24)
                    }
                )
            )
            r -= d
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        circles
            .filter { it.radius > 0 }
            .forEach { it.draw(canvas)}
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val w = 100 + paddingLeft + paddingRight
        val h = 100 + paddingTop + paddingBottom

        val rw = resolveSize(w, widthMeasureSpec)
        val rh = resolveSize(h, heightMeasureSpec)

        setMeasuredDimension(rw, rh)

        setupInitialValues(rw - paddingLeft - paddingRight, rh - paddingTop - paddingBottom)
    }

    fun startAnimation() {

        if (isAnimated) return

        isAnimated = true

        setupInitialValues(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom)

        val r = min(width - paddingLeft - paddingRight, height - paddingTop - paddingBottom) shr 1

        ValueAnimator.ofInt(0, r).apply {
            duration = this@Exercise2.duration.toLong()
            interpolator = LinearInterpolator()
            repeatCount = max(this@Exercise2.repeatCount - 1, 0)
            addUpdateListener {
                circles.forEach { c ->
                    c.radius = if (c.radius + 1 > r) 0 else c.radius + 1
                    c.paint.color = c.paint.color and 0x00FFFFFF or ((255 - 255 * c.radius / r) shl 24)
                }
                invalidate()
            }
            addListener(object: Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator) {
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
    }


}
