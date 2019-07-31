package com.acorn.guidehelper.library

import android.app.Activity
import android.content.Context
import android.graphics.RectF
import android.support.annotation.IdRes
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import java.lang.IllegalArgumentException
import kotlin.math.max

/**
 * Created by acorn on 2019-07-31.
 */
class GuideHelper(builder: Builder) {
    private var activity: Activity? = builder.activity
    private val decorView: FrameLayout? = builder.activity.window.decorView as? FrameLayout
    private var backLayerColor: Int = builder.backLayerColor
    private val shape = builder.shape
    private val layoutResId = builder.layoutResId
    private val fakeBeGuideViewResId = builder.fakeBeGuideViewResId
    private var paddingLeft = builder.paddingLeft
    private var paddingRight = builder.paddingRight
    private var paddingTop = builder.paddingTop
    private var paddingBottom = builder.paddingBottom
    private val beGuideView = builder.beGuideView
    private val rootView = LayoutInflater.from(activity).inflate(R.layout.view_root_guide, null) as FrameLayout
    private val backLayerIv: ImageView
    private val fakeBeGuideView: View?
    private val guideDrawable: GuideDrawable

    companion object {
        const val SHAPE_CIRCLE = 0
        const val SHAPE_RECT = 1
        const val SHAPE_OVAL = 2

        fun with(activity: Activity): Builder {
            return Builder(activity)
        }
    }

    init {
        with(rootView) {
            backLayerIv = findViewById(R.id.backLayerIv)
            if (layoutResId != null) {
                addView(
                    LayoutInflater.from(activity).inflate(layoutResId, null),
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
            fakeBeGuideView =
                if (layoutResId != null && fakeBeGuideViewResId != null) findViewById(fakeBeGuideViewResId) else null
            setOnClickListener {
                dismiss()
            }
        }

        val viewWidth = beGuideView?.width
        val viewHeight = beGuideView?.height

        if (null == beGuideView || beGuideView.visibility == View.GONE || viewWidth == 0 || viewHeight == 0) {
            guideDrawable = GuideDrawable(backLayerColor).apply {
                setBounds(0, getStatusHeight(activity!!), decorView?.width ?: 0, decorView?.height ?: 0)
            }
        } else {
            val location = IntArray(2)
            beGuideView.getLocationInWindow(location)
            val paddingLeftDp = dip2px(paddingLeft.toFloat())
            val paddingTopDp = dip2px(paddingTop.toFloat())
            val paddingRightDp = dip2px(paddingRight.toFloat())
            val paddingBottomDp = dip2px(paddingBottom.toFloat())

            if (null != fakeBeGuideView) {
                if (fakeBeGuideView.layoutParams !is ViewGroup.MarginLayoutParams) {
                    throw IllegalArgumentException("不支持MarginLayoutParams以外的LayoutParams")
                }

                with(fakeBeGuideView.layoutParams as ViewGroup.MarginLayoutParams) {
                    width = viewWidth!!
                    height = viewHeight!!
                    leftMargin = location[0]
                    topMargin = location[1]
                    fakeBeGuideView.setPadding(paddingLeftDp, paddingTopDp, paddingRightDp, paddingBottomDp)
                }
            }

            val rectF = RectF(
                (location[0] - paddingLeftDp).toFloat(),
                (location[1] - paddingTopDp).toFloat(),
                (location[0] + viewWidth!! + paddingRightDp).toFloat(),
                (location[1] + viewHeight!! + paddingBottomDp).toFloat()
            )

            guideDrawable = GuideDrawable(backLayerColor) { canvas, paint ->
                when (shape) {
                    SHAPE_CIRCLE -> canvas.drawCircle(
                        rectF.centerX(),
                        rectF.centerY(),
                        ((max(viewWidth, viewHeight) + paddingLeftDp) / 2).toFloat(),
                        paint
                    )
                    SHAPE_RECT -> canvas.drawRect(rectF, paint)
                    SHAPE_OVAL -> canvas.drawOval(rectF, paint)
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
        var layoutResId: Int? = null
        var fakeBeGuideViewResId: Int? = null

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

        fun layoutResId(@LayoutRes resId: Int): Builder {
            this.layoutResId = resId
            return this
        }

        fun fakeBeGuideViewResId(@IdRes resId: Int): Builder {
            this.fakeBeGuideViewResId = resId
            return this
        }

        fun build(): GuideHelper {
            return GuideHelper(this)
        }
    }
}