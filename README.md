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
1 根据实际需求完成引导蒙层的xml
注意:蒙层的
