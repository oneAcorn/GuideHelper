package com.acorn.guidehelper

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.acorn.guidehelper.library.GuideHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn1.setOnClickListener {
            GuideHelper.with(this)
                .backLayerColor(Color.argb(120, 144, 120, 60))
                .beGuideView(btn1)
                .shape(GuideHelper.SHAPE_RECT)
                .padding(10)
                .build()
                .show()
        }
    }
}
