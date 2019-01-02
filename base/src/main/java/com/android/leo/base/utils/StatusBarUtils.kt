package com.android.leo.base.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import com.android.leo.base.R

/**
 * Edit by wentao 2017/12/28
 */

class StatusBarUtils(private val mActivity: Activity) {
    //状态栏颜色
    private var mColor = -1
    //状态栏drawble
    private var mDrawable: Drawable? = null
    //是否是最外层布局是 DrawerLayout 的侧滑菜单
    var isDrawerLayout: Boolean = false
        private set
    //是否包含 ActionBar
    var isActionBar: Boolean = false
        private set
    //侧滑菜单页面的内容视图
    private var mContentResourseIdInDrawer: Int = 0

    fun getColor(): Int {
        return mColor
    }

    fun setColor(color: Int): StatusBarUtils {
        mColor = color
        return this
    }

    fun getDrawable(): Drawable? {
        return mDrawable
    }

    fun setDrawable(drawable: Drawable): StatusBarUtils {
        mDrawable = drawable
        return this
    }

    fun setIsActionBar(actionBar: Boolean): StatusBarUtils {
        isActionBar = actionBar
        return this
    }

    /**
     * 是否是最外层布局为 DrawerLayout 的侧滑菜单
     *
     * @param drawerLayout 是否最外层布局为 DrawerLayout
     * @param contentId    内容视图的 id
     * @return
     */
    fun setDrawerLayoutContentId(drawerLayout: Boolean, contentId: Int): StatusBarUtils {
        isDrawerLayout = drawerLayout
        mContentResourseIdInDrawer = contentId
        return this
    }

    fun init() {
        translucentActivity(mActivity)
        if (mColor != -1) {
            //设置了状态栏颜色
            addStatusViewWithColor(mActivity, mColor)
        }
        if (mDrawable != null) {
            //设置了状态栏 drawble，例如渐变色
            addStatusViewWithDrawble(mActivity, mDrawable!!)
        }
        if (isDrawerLayout) {
            //未设置 fitsSystemWindows 且是侧滑菜单，需要设置 fitsSystemWindows 以解决 4.4 上侧滑菜单上方白条问题
            fitsSystemWindows(mActivity)
        }
        if (isActionBar) {
            //要增加内容视图的 paddingTop,否则内容被 ActionBar 遮盖
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val rootView = mActivity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
                rootView.setPadding(0, getStatusBarHeight(mActivity) + getActionBarHeight(mActivity), 0, 0)
            }
        }
    }

    /**
     * 去除 ActionBar 阴影
     */
    fun clearActionBarShadow(): StatusBarUtils {
        if (Build.VERSION.SDK_INT >= 21) {
            val supportActionBar = (mActivity as AppCompatActivity).supportActionBar
            if (supportActionBar != null) {
                supportActionBar.elevation = 0f
            }
        }
        return this
    }

    /**
     * 设置页面最外层布局 FitsSystemWindows 属性
     *
     * @param activity
     */
    private fun fitsSystemWindows(activity: Activity) {
        val contentFrameLayout = activity.findViewById<View>(android.R.id.content) as ViewGroup
        val parentView = contentFrameLayout.getChildAt(0)
        if (parentView != null) {
            parentView.fitsSystemWindows = true
            //布局预留状态栏高度的 padding
            if (parentView is DrawerLayout) {
//将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
                parentView.clipToPadding = false
            }
        }
    }

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private fun addStatusViewWithColor(activity: Activity, color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (isDrawerLayout) {
                //要在内容布局增加状态栏，否则会盖在侧滑菜单上
                val rootView = activity.findViewById<View>(android.R.id.content) as ViewGroup
                //DrawerLayout 则需要在第一个子视图即内容试图中添加padding
                val parentView = rootView.getChildAt(0)
                val linearLayout = LinearLayout(activity)
                linearLayout.orientation = LinearLayout.VERTICAL
                val statusBarView = View(activity)
                val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        getStatusBarHeight(activity))
                statusBarView.setBackgroundColor(color)
                //添加占位状态栏到线性布局中
                linearLayout.addView(statusBarView, lp)
                //侧滑菜单
                val drawer = parentView as DrawerLayout
                //内容视图
                val content = activity.findViewById<View>(mContentResourseIdInDrawer)
                //将内容视图从 DrawerLayout 中移除
                drawer.removeView(content)
                //添加内容视图
                linearLayout.addView(content, content.layoutParams)
                //将带有占位状态栏的新的内容视图设置给 DrawerLayout
                drawer.addView(linearLayout, 0)
            } else {
                //设置 paddingTop
                val rootView = mActivity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
                rootView.setPadding(0, getStatusBarHeight(mActivity), 0, 0)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //直接设置状态栏颜色
                    activity.window.statusBarColor = color
                } else {
                    //增加占位状态栏
                    val decorView = mActivity.window.decorView as ViewGroup
                    val statusBarView = View(activity)
                    val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            getStatusBarHeight(activity))
                    statusBarView.setBackgroundColor(color)
                    decorView.addView(statusBarView, lp)
                }
            }
        }
    }

    /**
     * 添加状态栏占位视图
     *
     * @param activity
     */
    private fun addStatusViewWithDrawble(activity: Activity, drawable: Drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //占位状态栏
            val statusBarView = View(activity)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                statusBarView.background = drawable
            } else {
                statusBarView.background = drawable
            }
            if (isDrawerLayout) {
                //要在内容布局增加状态栏，否则会盖在侧滑菜单上
                val rootView = activity.findViewById<View>(android.R.id.content) as ViewGroup
                //DrawerLayout 则需要在第一个子视图即内容试图中添加padding
                val parentView = rootView.getChildAt(0)
                val linearLayout = LinearLayout(activity)
                linearLayout.orientation = LinearLayout.VERTICAL
                //添加占位状态栏到线性布局中
                linearLayout.addView(statusBarView, lp)
                //侧滑菜单
                val drawer = parentView as DrawerLayout
                //内容视图
                val content = activity.findViewById<View>(mContentResourseIdInDrawer)
                //将内容视图从 DrawerLayout 中移除
                drawer.removeView(content)
                //添加内容视图
                linearLayout.addView(content, content.layoutParams)
                //将带有占位状态栏的新的内容视图设置给 DrawerLayout
                drawer.addView(linearLayout, 0)
            } else {
                //增加占位状态栏，并增加状态栏高度的 paddingTop
                val decorView = mActivity.window.decorView as ViewGroup
                decorView.addView(statusBarView, lp)
                //设置 paddingTop
                val rootView = mActivity.window.decorView.findViewById<View>(android.R.id.content) as ViewGroup
                rootView.setPadding(0, getStatusBarHeight(mActivity), 0, 0)
            }
        }
    }

    /**
     * 使用状态栏空间
     * @param activity
     */
    fun translucentActivity(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                val window = activity.window
                val decorView = window.decorView
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                decorView.systemUiVisibility = option
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = Color.TRANSPARENT
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                val window = activity.window
                val attributes = window.attributes
                val flagTranslucentStatus = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                } else {
                    0
                }
//                val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                attributes.flags = attributes.flags or flagTranslucentStatus
                //                attributes.flags |= flagTranslucentNavigation;
                window.attributes = attributes
            }
        }
    }

    companion object {

        fun with(activity: Activity): StatusBarUtils {
            return StatusBarUtils(activity)
        }

        /**
         * 给指定view上方包裹一层类似statusbar的view
         * 沉浸式开关开了才起作用
         *
         * @param view
         * @param statusBarColor
         */
        fun wrapStatusBarView(view: View?, statusBarColor: Int) {
            if (view == null) {
                return
            }
            val parent = view.parent as ViewGroup
            if (parent != null) {
                val linearLayout = LinearLayout(view.context)
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                val statusBarView = View(view.context)
                statusBarView.id = R.id.status_bar_view
                statusBarView.setBackgroundColor(statusBarColor)
                linearLayout.addView(statusBarView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(view.context)))
                parent.removeView(view)
                linearLayout.addView(view)
                parent.addView(linearLayout, -1, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
            }
        }

        fun wrapStatusBarView(activity: Activity, statusBarColor: Int) {
            val view = (activity.findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
            wrapStatusBarView(view, statusBarColor)
        }

        /**
         * 利用反射获取状态栏高度
         *
         * @return
         */
        fun getStatusBarHeight(activity: Context): Int {
            var result = 0
            //获取状态栏高度的资源id
            val resourceId = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = activity.resources.getDimensionPixelSize(resourceId)
            }
            Log.e("getStatusBarHeight", result.toString() + "")
            return result
        }

        /**
         * 获得 ActionBar 的高度
         *
         * @param context
         * @return
         */
        fun getActionBarHeight(context: Context): Int {
            var result = 0
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                val tv = TypedValue()
                context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)
                result = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
            }
            return result
        }

        /**
         * 获取全屏flag
         */
        fun getFullscreenUiFlags(): Int {
            var flags = View.SYSTEM_UI_FLAG_LOW_PROFILE or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                flags = flags or (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
            return flags
        }


        /**
         * 播放器全屏处理
         */
        fun setUiFlags(activity: Activity, fullscreen: Boolean) {
            val win = activity.getWindow()
            val winParams = win.getAttributes()
            if (fullscreen) {
                winParams.flags = winParams.flags or WindowManager.LayoutParams.FLAG_FULLSCREEN
            } else {
                winParams.flags = winParams.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN.inv()
            }
            win.setAttributes(winParams)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val decorView = activity.getWindow().getDecorView()
                if (decorView != null) {
                    val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    decorView.setSystemUiVisibility(if (fullscreen) getFullscreenUiFlags() else option)
                }
            }
        }

        /**
         * 通过设置全屏，设置状态栏透明 导航栏黑色
         *
         * @param activity
         */
        fun setStatusTransparent(activity: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val window = activity.window

                    val attributes = window.attributes
//                    val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    val flagTranslucentNavigation = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    } else {
                        0
                    }
                    //                attributes.flags |= flagTranslucentStatus;
                    attributes.flags = attributes.flags or flagTranslucentNavigation
                    window.attributes = attributes

                    window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = Color.TRANSPARENT
                    window.navigationBarColor = Color.TRANSPARENT
                } else {
                    val window = activity.window
                    val attributes = window.attributes
                    val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    val flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
                    attributes.flags = attributes.flags or flagTranslucentStatus
                    attributes.flags = attributes.flags or flagTranslucentNavigation
                    window.attributes = attributes
                }
            }
        }
    }
}
