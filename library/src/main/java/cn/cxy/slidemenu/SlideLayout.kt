package cn.cxy.slidemenu

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.HorizontalScrollView
import android.widget.LinearLayout

/**
 * 侧滑菜单布局
 */
class SlideLayout(context: Context?, attrs: AttributeSet? = null) :
    HorizontalScrollView(context, attrs) {
    private lateinit var mLeftMenu: ViewGroup
    private lateinit var mRightMenu: ViewGroup
    private var mOnMenuSelected: OnMenuSelect? = null

    /**
     * 左侧菜单宽度
     */
    private var mLeftMenuWidth = 0

    /**
     * 右侧菜单宽度
     */
    private var mRightMenuWidth = 0

    /**
     * 滑动超过如下比例，就自动完整显示菜单。
     */
    private var mShowMenuRatio = 0.5
    private var hasMeasured = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        /**
         * 显式设置中间内容区域宽度
         */
        if (!hasMeasured) {
            val wrapper = getChildAt(0) as LinearLayout
            val content = wrapper.getChildAt(1) as ViewGroup
            content.layoutParams.width = getScreenWidth()
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (changed) {
            val wrapper = getChildAt(0) as LinearLayout
            mLeftMenu = wrapper.getChildAt(0) as ViewGroup
            mRightMenu = wrapper.getChildAt(2) as ViewGroup
            mLeftMenuWidth = mLeftMenu.width
            mRightMenuWidth = mRightMenu.width
            hasMeasured = true
            //隐藏菜单，显示主体内容
            scrollTo(mLeftMenu.width, 0)
        }
    }

    /**
     * 左右滑动，超过菜单本身宽度一定比例，就完整显示菜单。
     */
    override fun onTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        when (action) {
            MotionEvent.ACTION_UP -> {
                val scrollX = scrollX
                if (scrollX > mLeftMenuWidth + mRightMenuWidth * mShowMenuRatio) {
                    smoothScrollTo(mLeftMenuWidth + mRightMenuWidth, 0)
                    mOnMenuSelected?.onSelected(mRightMenu)
                } else if (scrollX > mLeftMenuWidth * mShowMenuRatio) {
                    smoothScrollTo(mLeftMenuWidth, 0)
                } else {
                    mOnMenuSelected?.onSelected(mLeftMenu)
                    smoothScrollTo(0, 0)
                }
                return true
            }
        }
        return super.onTouchEvent(ev)
    }

    private fun getScreenWidth(): Int {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        return point.x
    }

    fun setCallback(onMenuSelect: OnMenuSelect) {
        mOnMenuSelected = onMenuSelect
    }
}

interface OnMenuSelect {
    fun onSelected(view: ViewGroup)
}