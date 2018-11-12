# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#############################################
#
# 对于一些基本指令的添加
#
#############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5
# 混合时不使用大小写混合，混合后的类名为小写
-dontusemixedcaseclassnames
# 指定不去忽略非公共库的类
-dontskipnonpubliclibraryclasses
# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose
# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers
# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify
# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses
# 避免混淆泛型
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

# 保留support下的所有类及其内部类
-keep class android.support.** {*;}

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keep class * extends com.playablestudio.caowt.base.BaseBeanModel{
    *** *;
    void set*(***);
    *** get*();
}
# webView处理，项目中没有使用到webView忽略即可
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
    public *;
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.webView, jav.lang.String);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * implements java.io.Serializable {
     static final long serialVersionUID;
     private static final java.io.ObjectStreamField[] serialPersistentFields;
     private void writeObject(java.io.ObjectOutputStream);
     private void readObject(java.io.ObjectInputStream);
     java.lang.Object writeReplace();
     java.lang.Object readResolve();
 }

-keep class com.umeng.commonsdk.** {*;}
-keep class * extends com.playablestudio.caowt.module.base.BaseModel {
    *** *;
}

-dontwarn io.reactivex.**
-dontwarn org.reactivestreams.**
-dontwarn com.alivc.player.**
-dontwarn com.aliyun.**
-dontwarn com.aliyun.vodplayer.**
-dontwarn android.arch.**
-dontwarn android.support.**
-dontwarn com.dhh.websocket.**
-dontwarn com.aspsine.swipetoloadlayout.**
-dontwarn com.bumptech.**
-dontwarn master.flame.danmaku.**
-dontwarn com.google.**
-dontwarn com.squareup.**
-dontwarn io.reactivex.**
-dontwarn jp.wasabeef.glide.transformations.**
-dontwarn kotlin.**
-dontwarn org.reactivestreams.**
-dontwarn com.umeng.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**
-dontwarn com.yanzhenjie.loading.**
-dontwarn com.yanzhenjie.recyclerview.swipe.**
-dontwarn org.eclipse.paho.android.service.**
-dontwarn org.eclipse.paho.client.mqttv3.**
-dontwarn greendao.**
-dontwarn com.tencent.**
-dontwarn com.tencent.StubShell.TxMeShell.**
-dontwarn com.playablestudio.caowt.PlayableApplication.**
-dontwarn android.support.v4.content.FileProvider.**
-dontwarn com.surevideo.core.**



#-dontwarn com.yixia.**
#-dontwarn com.yixia.camera.demo.**
#-dontwarn com.yixia.videoeditor.**
#-dontwarn com.yixia.weibo.sdk.**


-keep class io.reactivex.** { *;}
-keep class org.reactivestreams.** { *;}
-keep class com.alivc.player.** { *;}
-keep class com.aliyun.** { *;}
-keep class android.arch.** { *;}
-keep class android.support.** { *;}
-keep class com.dhh.websocket.** { *;}
-keep class com.aspsine.swipetoloadlayout.** { *;}
-keep class com.bumptech.** { *;}
-keep class master.flame.danmaku.** { *;}
-keep class com.google.** { *;}
-keep class com.squareup.** { *;}
-keep class io.reactivex.** { *;}
-keep class jp.wasabeef.glide.transformations.** { *;}
-keep class kotlin.** { *;}
-keep class org.reactivestreams.** { *;}
-keep class com.umeng.** { *;}
-keep class com.okhttp3.** { *;}
-keep class okio.** { *;}
-keep class retrofit2.** { *;}
-keep class com.yanzhenjie.loading.** { *;}
-keep class com.yanzhenjie.recyclerview.swipe.** { *;}
-keep class org.eclipse.paho.android.service.** { *;}
-keep class org.eclipse.paho.client.mqttv3.** { *;}
-keep class greendao.** { *;}
-keep class com.tencent.** { *;}
-keep class com.tencent.StubShell.TxMeShell.** { *;}
-keep class com.playablestudio.caowt.PlayableApplication.** { *;}
-keep class android.support.v4.content.FileProvider.** { *;}
-keep class com.surevideo.core.** { *;}
#-keep class com.yixia.** { *;}
#-keep class com.yixia.camera.demo.** { *;}
#-keep class com.yixia.videoeditor.** { *;}
#-keep class com.yixia.weibo.sdk.** { *;}
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

-keep class com.inno.innosdk.pb.** { *; }
-keep class com.inno.innosdk.bean.DeviceInfo { *; }
-keep class com.inno.innocommon.pb.** { *; }
-keep class com.inno.innocommon.bean.DeviceInfo { *; }

-keep class tv.danmaku.ijk.media.player.** {*;}
-keep class tv.danmaku.ijk.media.player.IjkMediaPlayer{*;}
-keep class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{*;}

-ignorewarnings
-dontwarn com.google.common.cache.**
-dontwarn java.nio.file.**
-dontwarn sun.misc.**
-keep class com.bumptech.glide.integration.okhttp3.** { *; }
-keep class com.liulishuo.filedownloader.** { *; }
-keep class java.nio.file.** { *; }
-keep class sun.misc.** { *; }


-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.playablestudio.caowt.R$*{
public static final int *;
}

-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**

##-------Glide----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule

##---------------Begin: proguard configuration for Gson  ----------
 # Gson specific classes
 -keep class sun.misc.Unsafe { *; }
 #-keep class com.google.gson.stream.** { *; }
 # Application classes that will be serialized/deserialized over Gson
 -keep class com.google.gson.examples.android.model.** { *; }
 ##---------------End: proguard configuration for Gson  ----------

 # 使用了RouterService注解的实现类，需要避免Proguard把构造方法、方法等成员移除(shrink)或混淆(obfuscate)，导致无法反射调用。实现类的类名可以混淆。
 -keepclassmembers @com.sankuai.waimai.router.annotation.RouterService class * { *; }

