package com.mshy.VInterestSpeed.common.ext

import android.content.Context
import android.os.Build
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.live.vquonline.base.BaseApplication
import com.live.vquonline.base.ktx.clickDelay
import com.live.vquonline.base.ktx.visible
import com.live.vquonline.base.utils.BarUtils
import com.mshy.VInterestSpeed.common.R
import com.mshy.VInterestSpeed.common.utils.ResUtils

/**
 * 描述　:项目中自定义类的拓展函数
 */


//绑定普通的Recyclerview
fun RecyclerView.init(
    layoutManger: RecyclerView.LayoutManager,
    bindAdapter: RecyclerView.Adapter<*>,
    isScroll: Boolean = true
): RecyclerView {
    layoutManager = layoutManger
    setHasFixedSize(true)
    adapter = bindAdapter
    isNestedScrollingEnabled = isScroll
    return this
}


/**
 * 初始化普通的toolbar 只设置标题
 */
fun Toolbar.init(titleStr: String = ""): Toolbar {
    setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    this.findViewById<TextView>(R.id.tv_title).text = titleStr
    return this
}

typealias BackFun = (toolbar: Toolbar) -> Unit
typealias SearchFun = (keyword: String) -> Unit
typealias ClearFun = () -> Boolean

/**
 * 初始化有返回键的toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    backImg: Int = R.mipmap.ic_back,
    onBack: BackFun
): Toolbar {
    setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    this.findViewById<TextView>(R.id.tv_title).text = titleStr
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    return this
}
fun Toolbar.initClose(
    titleStr: String = ""
): Toolbar {
    navigationIcon = null
    setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    this.findViewById<TextView>(R.id.tv_title).text = titleStr
    return this
}

/**
 * 初始化有返回键有右边文字的toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    rightStr: String = "",
    rightColor: Int = R.color.white,
    backImg: Int = R.mipmap.ic_back,
    onBack: BackFun,
    onClickRight: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    this.findViewById<TextView>(R.id.tv_title).text = titleStr
    this.findViewById<TextView>(R.id.tv_right).text = rightStr
    if (!rightStr.isNullOrEmpty()) {
        this.findViewById<TextView>(R.id.tv_right).visible()
    }
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    this.findViewById<TextView>(R.id.tv_right).setViewClickListener() { onClickRight.invoke(this) }
    return this
}

/**
 * 初始化有返回键有右边文字的toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    rightStr: String = "",
    rightColor: Int = R.color.color_A3AABE,
    backImg: Int = R.mipmap.ic_back,
    backgroundColor:Int = R.color.white,
    onBack: BackFun,
    onClickRight: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(ContextCompat.getColor(context, backgroundColor))
    this.findViewById<TextView>(R.id.tv_title).text = titleStr
    this.findViewById<TextView>(R.id.tv_right).text = rightStr
    if (!rightStr.isNullOrEmpty()) {
        this.findViewById<TextView>(R.id.tv_right).visible()
    }
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    this.findViewById<TextView>(R.id.tv_right).setViewClickListener() { onClickRight.invoke(this) }
    return this
}

/**
 * 初始化有返回键有图片的toolbar
 */
fun Toolbar.initClose(
    titleStr: String = "",
    rightImage: Int = R.mipmap.ic_more_black,
    backImg: Int = R.mipmap.ic_back,
    onBack: BackFun,
    onClickRight: (toolbar: Toolbar) -> Unit
): Toolbar {
    setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    this.findViewById<TextView>(R.id.tv_title).text = titleStr
    this.findViewById<ImageView>(R.id.iv_right).setImageResource(rightImage)
    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
    this.findViewById<ImageView>(R.id.iv_right).setViewClickListener() { onClickRight.invoke(this) }
    return this
}

fun RecyclerView.addOnPreloadListener(preloadCount: Int, onPreload: () -> Unit) {
    // 监听 RecyclerView 滚动状态
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            // 获取 LayoutManger
            val layoutManager = recyclerView.layoutManager
            // 如果 LayoutManager 是 LinearLayoutManager
            if (layoutManager is LinearLayoutManager) {
                // 如果列表正在往上滚动，并且表项最后可见表项索引值 等于 预加载阈值
                if (dy > 0 && layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1 - preloadCount) {
                    onPreload()
                }
            }
        }
    })
}

fun Toolbar.initSearch(
    hint: String,
    onSearch: SearchFun,
    onClear: ClearFun? = null,
    onBack: BackFun
) {
    setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    val tvTitle = this.findViewById<EditText>(R.id.tv_title)
    tvTitle.hint = hint

    val ivVquClear = this.findViewById<ImageView>(R.id.iv_vqu_search_toolbar_clear)

    tvTitle.addTextChangedListener {
        ivVquClear.visibility = if ((it?.length ?: 0) > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    ivVquClear.clickDelay {
        if (onClear?.invoke() != true) {
            tvTitle.setText("")
            ivVquClear.visibility = View.GONE
        }
    }

    tvTitle.setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
            (event.keyCode == KeyEvent.KEYCODE_ENTER)
        ) {
            val imm: InputMethodManager =
                BaseApplication.application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(
                tvTitle.windowToken,
                0
            )

            onSearch.invoke(tvTitle.text.toString())
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }

    this.findViewById<com.mshy.VInterestSpeed.common.ui.view.ShapeTextView>(R.id.stv_vqu_search_bar_search).clickDelay {
        onSearch.invoke(tvTitle.text.toString())
    }

    this.findViewById<ImageView>(R.id.iv_vqu_search_toolbar_back).clickDelay {
        onBack.invoke(this)
    }

}

fun Toolbar.initSearch(
    @StringRes hint: Int,
    onSearch: SearchFun,
    onClear: ClearFun? = null,
    onBack: BackFun
) {
    initSearch(ResUtils.getString(hint), onSearch, onClear, onBack)
}

fun Toolbar.initSearch2(
    hint: String,
    backImg: Int = R.mipmap.ic_back,
    onSearch: SearchFun,
    onClear: ClearFun? = null,
    onBack: BackFun
) {
    setBackgroundColor(ContextCompat.getColor(context, R.color.white))
    val tvTitle = this.findViewById<EditText>(R.id.tv_title)
    tvTitle.hint = hint

    val ivVquClear = this.findViewById<ImageView>(R.id.iv_vqu_search_toolbar_clear)

    tvTitle.addTextChangedListener {
        ivVquClear.visibility = if ((it?.length ?: 0) > 0) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    ivVquClear.clickDelay {
        if (onClear?.invoke() != true) {
            tvTitle.setText("")
        }
    }

    tvTitle.setOnEditorActionListener { _, actionId, event ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH ||
            (event.keyCode == KeyEvent.KEYCODE_ENTER)
        ) {
            onSearch.invoke(tvTitle.text.toString())
            return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
    }

    setNavigationIcon(backImg)
    setNavigationOnClickListener { onBack.invoke(this) }
}


fun Toolbar.initSearch2(
    @StringRes hint: Int,
    backImg: Int = R.mipmap.ic_back,
    onSearch: SearchFun,
    onClear: ClearFun? = null,
    onBack: BackFun
) {
    initSearch2(ResUtils.getString(hint), backImg, onSearch, onClear, onBack)
}


//设置适配器的列表动画
fun BaseQuickAdapter<*, *>.setAdapterAnimation(mode: Int) {
    //等于0，关闭列表动画 否则开启
    if (mode == 0) {
        this.animationEnable = false
    } else {
        this.animationEnable = true
        this.setAnimationWithDefault(BaseQuickAdapter.AnimationType.values()[mode - 1])
    }
}


fun TextView.html(html: String) {
    val clickableHtml = com.mshy.VInterestSpeed.common.utils.TextUtils.getClickableHtml(html)
    this.movementMethod =
        LinkMovementMethod.getInstance()
    this.autoLinkMask = Linkify.WEB_URLS
    this.text = clickableHtml
}


fun View.addStatusBarHeight(isTranslucent: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || isTranslucent) {
        val layoutParams: ViewGroup.LayoutParams = this.layoutParams
        //            layoutParams.height = windowsPX(AppUtils.getApplication(), srcHeight) + gianStatusHeight(AppUtils.getApplication());
        layoutParams.height = layoutParams.height + BarUtils.getStatusBarHeight()
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
        this.layoutParams = layoutParams
        this.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0)
    }
}




