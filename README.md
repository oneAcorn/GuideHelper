# GuideHelper
用户引导工具,用于高亮某部分以指示用户某些功能,采用链式调用.
### 效果展示
![github](https://github.com/oneAcorn/GuideHelper/blob/master/20190829_112046.gif)
### 依赖方法
1.在root build.gradle中加入

```gradle
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

2.在项目的 build.gradle中加入

```gradle
dependencies {
	        implementation 'com.github.oneAcorn:GuideHelper:v1.0'
	}
```
### 使用方法
#### 1 根据实际需求完成引导蒙层的xml
注意fakeView的使用
如下图中红色区域
![github](https://github.com/oneAcorn/GuideHelper/blob/master/demo2.png)
这个引导蒙层中的红色区域代表被引导的view的所在位置,当调用引导时传入此红色区域view的ID,GuideHelper会自动将此View移动到和被引导View相同的位置,改变为相同的大小
上述图片的详细xml
``` xml
<?xml version="1.0" encoding="utf-8"?>
<android.support.constraint.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
                                             xmlns:app="http://schemas.android.com/apk/res-auto"
                                             android:background="#A6AA9B"
                                             android:layout_width="match_parent"
                                             android:layout_height="match_parent">

    <View
            android:id="@+id/fakeView"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            android:layout_width="10dp"
            android:layout_height="10dp"
            />


    <ImageView
            android:id="@+id/guideIv"
            android:layout_width="wrap_content"
               android:layout_height="wrap_content"
               android:src="@drawable/guide"
               app:layout_constraintStart_toEndOf="@id/fakeView"
               app:layout_constraintTop_toBottomOf="@id/fakeView"
    />
   <Button android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           app:layout_constraintTop_toBottomOf="@id/guideIv"
           android:layout_marginTop="20dp"
           app:layout_constraintStart_toStartOf="@id/guideIv"
           app:layout_constraintEnd_toEndOf="@id/guideIv"
           android:text="确定"
   />
</android.support.constraint.ConstraintLayout>
```
#### 2 调用引导功能
完成蒙层的xml后就可以引导用户啦,使用下面的链式调用方法设置各种参数后.show()即可
``` kotlin
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
```
注意在fakeBeGuideViewResId()方法中传递的R.id.fakeView就是在R.layout.view_guide1你的蒙层布局中代表被引导View的那个View的ID
