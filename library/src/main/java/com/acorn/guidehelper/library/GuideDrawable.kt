package com.acorn.guidehelper.library

import android.graphics.*
import android.graphics.drawable.Drawable

/**
 * Created by acorn on 2019-07-31.
 */
class GuideDrawable(
    var backLayerColor: Int = Color.argb(120, 0, 0, 0),
    private val drawHighLightCallback: ((Canvas,Paint) -> Unit)? = null
) :
    Drawable() {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = backLayerColor
    }
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)


    override fun draw(canvas: Canvas) {
        with(paint) {
            color = backLayerColor
            xfermode = null
            canvas.drawPaint(this)
            reset()
            color = 0x00ffffff
            xfermode = this@GuideDrawable.xfermode
            drawHighLightCallback?.invoke(canvas,this)
        }
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}