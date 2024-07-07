# About this Project

An android skin change framework

# Core Ability

Load custom ui style from skin package

# Steps for Integration

#### 1. Dependency

```kotlin
api("io.github.hellogoogle2000:android-skinner:1.0.0")
```

#### 2. Init

suggest calling after `Application#onCreate`

```kotlin
SkinnerKit.init(application)
```

#### 3. Install Skin Package

call before apply this skin

```kotlin
SkinnerKit.installSkin(assets.open("skin.apk"), "skinner")
```

#### 4. Install SkinnerInflaterFactory

suggest calling before `Activity#onCreate`

```kotlin
SkinnerKit.installSkinnerFactory(activity)
```

#### 5. Load Skin

recall `Activity#setContentView` to take effect

```kotlin
SkinnerKit.loadSkin("skinner")
```

#### 5. Switch Skin Mode

recall `Activity#setContentView` to take effect

```kotlin
SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_DARK)
```

#### 6. Restore to Default

recall `Activity#setContentView` to take effect

```kotlin
SkinnerKit.loadSkin(SkinnerValues.SKIN_NAME_DEFAULT)
SkinnerKit.setSkinMode(SkinnerValues.SKIN_MODE_DEFAULT)
```

#### 7. Apply to LayoutXml

- add skin namespace
- specify provider
- dynamic resource should named end with `_skinnable`

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
      xmlns:app="http://schemas.android.com/apk/res-auto"
      xmlns:skin="http://schemas.android.com/skin"
      android:layout_width="match_parent"
      android:layout_height="match_parent"
      android:background="@color/background_color_01_skinnable"
      android:gravity="center"
      android:orientation="vertical"
      android:padding="30dp">

    <ImageView
        android:id="@+id/image"
        android:layout_width="100dp"
        android:layout_height="100dp"
        android:scaleType="fitXY"
        android:src="@drawable/icon_app_skinnable"
        skin:provider="BasicAttributeSkinner" />

    <TextView
        android:id="@+id/text"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:gravity="center"
        android:text="Hello"
        android:textColor="@color/text_color_01_skinnable"
        android:textSize="16dp"
        skin:provider="BasicAttributeSkinner" />
</LinearLayout>
```

#### 8. Register Custom View Provider

due to android technical limit<br>
skinner only support three skinnable attribute by default<br>
background, src, and textColor

you can create your own provider to introduce more customization<br>
take a reference to `BasicAttributeSkinner`, that work is just simple

```kotlin
object CustomAttributeSkinner : BaseSkinnerProvider()
SkinnerProvidersFactory.registerViewProvider(CustomAttributeSkinner)
```

# Make Skin Package

#### 1. Create Skin Package Project

create an empty android application project<br>
keep application id same with the origin one<br>
if your skin have more than one mode, create a mode folder additionally<br>
mode folder named as `res-mode`, corresponding resource named as `xxx_skinnable_mode`

<img src="/Users/easing/Cloud/Nutstore/Markdown/Blog/01.png" height="250"><br>
<img src="/Users/easing/Cloud/Nutstore/Markdown/Blog/02.png" height="400" width="800">

```groovy
plugins {
    id("com.android.application")
}

android {
    compileSdk = 34
    defaultConfig {
        namespace = "com.android.app"
        applicationId = "com.android.app"
        minSdk = 30
    }

    sourceSets {
        getByName("main").res.srcDirs("src/main/res-dark")
    }
}
```

#### 2. Build Skin Package

just like build an android apk<br>
run `Build - Build APK`, then you will get a skin apk in `build/output` directory<br>
install it to your application spaces as mentioned above

# End

for more detailed confusion, take a look at sample app<br>
this is actually a simple and concise library, I believe you can do it<br>

*Good Job, Baby !*