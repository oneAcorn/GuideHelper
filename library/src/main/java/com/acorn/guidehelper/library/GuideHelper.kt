package com.acorn.guidehelper.library

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

/**
 * Created by acorn on 2019-07-31.
 */
class GuideHelper(builder: Builder) {
    private var activity: Activity? = builder.activity
    private val decorView: FrameLayout? = builder.activity.window.decorView as? FrameLayout
    private var backLayerColor: Int = builder.backLayerColor
    private val shape = builder.shape
    private var paddingLeft = builder.paddingLeft
    private var paddingRight = builder.paddingRight
    private var paddingTop = builder.paddingTop
    private var paddingBottom = builder.paddingBottom
    private val beGuideView = builder.beGuideView
    private var rootView = LayoutInflater.from(activity).inflate(R.layout.view_root_guide, null)
    private var backLayerIv: ImageView
    private var fakeBeGuideView: View
    private val guideDrawable: GuideDrawable

    companion object {
        const val SHAPE_CIRCLE = 0
        const val SHAPE_RECT = 1
        const val SHAPE_ROUND_RECT = 2
        const val SHAPE_OVAL = 3

        fun with(activity: Activity): Builder {
            return Builder(activity)
        }
    }

    init {
        with(rootView) {
            backLayerIv = findViewById(R.id.backLayerIv)
            fakeBeGuideView = findViewById(R.id.fakeBeGuideView)
            setOnClickListener {
                dismiss()
            }
        }

        val viewWidth = beGuideView?.width
        val viewHeight = beGuideView?.height
        guideDrawable =
            if (null == beGuideView || beGuideView.visibility == View.GONE || viewWidth == 0 || viewHeight == 0) {
                GuideDrawable(backLayerColor).apply {
                    setBounds(0, 0, decorView?.width ?: 0, decorView?.height ?: 0)
                }
            } else {
                val location = IntArray(2)
                beGuideView.getLocationInWindow(location)
                val rectF: RectF? = if (shape != SHAPE_CIRCLE) {
                    RectF(
                        (location[0] - dip2px(paddingLeft.toFloat())).toFloat(),
                        (location[1] - dip2px(paddingTop.toFloat())).toFloat(),
                        (location[0] + viewWidth!! + dip2px(paddingRight.toFloat())).toFloat(),
                        (location[1] + viewHeight!! + dip2px(paddingBottom.toFloat())).toFloat()
                    )
                } else null
                GuideDrawable(backLayerColor) { canvas, paint ->
                    if (shape == SHAPE_CIRCLE) {

                    } else if (shape == SHAPE_RECT) {
                        canvas.drawRect(rectF, paint)
                    } else if (shape == SHAPE_OVAL) {
                        canvas.drawOval(rectF, paint)
                    }
                }.apply {
                    setBounds(0, 0, decorView?.width ?: 0, decorView?.height ?: 0)
                }
            }
    }

    fun show() {
        with(backLayerIv) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null)
            setImageDrawable(guideDrawable)
        }
        try {
            decorView?.addView(
                rootView, FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams
                        .MATCH_PARENT
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun dismiss() {
        decorView?.removeView(rootView)
        this.activity = null
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    private fun getStatusHeight(context: Context): Int {

        var statusHeight = -1
        try {
            val clazz = Class.forName("com.android.internal.R\$dimen")
            val mObject = clazz.newInstance()
            val height = Integer.parseInt(
                clazz.getField("status_bar_height")
                    .get(mObject).toString()
            )
            statusHeight = context.resources.getDimensionPixelSize(height)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return statusHeight
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private fun dip2px(dpValue: Float): Int {
        val scale = activity!!.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    class Builder(val activity: Activity) {
        var backLayerColor: Int = -0x80000000
        var beGuideView: View? = null
        var paddingLeft: Int = 0
        var paddingRight: Int = 0
        var paddingTop: Int = 0
        var paddingBottom: Int = 0
        var shape: Int = 0

        fun backLayerColor(color: Int): Builder {
            this.backLayerColor = color
            return this
        }

        fun beGuideView(view: View?): Builder {
            this.beGuideView = view
            return this
        }

        fun paddingLeft(offset: Int): Builder {
            this.paddingLeft = offset
            return this
        }

        fun paddingRight(offset: Int): Builder {
            this.paddingRight = offset
            return this
        }

        fun paddingTop(offset: Int): Builder {
            this.paddingTop = offset
            return this
        }

        fun paddingBottom(offset: Int): Builder {
            this.paddingBottom = offset
            return this
        }

        fun padding(offset: Int): Builder {
            this.paddingLeft = offset
            this.paddingRight = offset
            this.paddingTop = offset
            this.paddingBottom = offset
            return this
        }

        fun shape(shape: Int): Builder {
            this.shape = shape
            return this
        }

        fun build(): GuideHelper {
            return GuideHelper(this)
        }
    }
}