package com.acorn.guidehelper

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.acorn.guidehelper.library.GuideHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn1.setOnClickListener {
            GuideHelper.with(this)
                .backLayerColor(Color.argb(120, 144, 120, 60)) //蒙层颜色
                .beGuideView(btn1) //被引导的View
                .shape(GuideHelper.SHAPE_RECT) //高亮区域形状
                .paddingTop(5) //高亮区域的padding
                .paddingBottom(5)
                .paddingRight(10)
                .paddingLeft(10)
                .layoutResId(R.layout.view_guide1) //自己的引导布局
                .fakeBeGuideViewResId(R.id.fakeView) //自己的引导布局中的被引导的View的替代
                .build()
                .show()
        }

        btn2.setOnClickListener {
            GuideHelper.with(this)
                .backLayerColor(Color.argb(120, 0, 0, 0))
                .beGuideView(btn2)
                .shape(GuideHelper.SHAPE_CIRCLE)
                .layoutResId(R.layout.view_guide1)
                .fakeBeGuideViewResId(R.id.fakeView)
                .build()
                .show()
        }

        btn3.setOnClickListener {
            GuideHelper.with(this)
                .backLayerColor(Color.argb(180, 30, 220, 60))
                .beGuideView(btn3)
                .shape(GuideHelper.SHAPE_OVAL)
                .layoutResId(R.layout.view_guide1)
                .fakeBeGuideViewResId(R.id.fakeView)
                .build()
                .show()
        }

        btn4.setOnClickListener {
            GuideHelper.with(this)
                .backLayerColor(Color.argb(120, 0, 0, 0))
                .beGuideView(btn4)
                .shape(GuideHelper.SHAPE_CIRCLE)
                .padding(-30)
                .layoutResId(R.layout.view_guide1)
                .fakeBeGuideViewResId(R.id.fakeView)
                .build()
                .show()
        }

        btn5.setOnClickListener {
            GuideHelper.with(this)
                .backLayerColor(Color.argb(120, 0, 0, 0))
                .layoutResId(R.layout.view_guide1)
                .build()
                .show()
        }
    }
}
