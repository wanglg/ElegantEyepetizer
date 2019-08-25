package com.android.leo.mine

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.Fragment
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.GlobalConstant
import com.android.leo.base.ui.fragments.BaseFragment
import com.lasingwu.baselibrary.ImageLoader
import com.lasingwu.baselibrary.ImageLoaderOptions
import com.sankuai.waimai.router.annotation.RouterService
import kotlinx.android.synthetic.main.fragment_mine.*

@RouterService(interfaces = arrayOf(Fragment::class), key = arrayOf(GlobalConstant.Fragment.MINE))
class MineFragment : BaseFragment<IPresenter>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initPresenter() {
    }

    override fun configViews() {
        val imageLoaderOptions = ImageLoaderOptions.Builder(mine_head, R.mipmap.icon_mine_head).isCircle().isCrossFade(true).build()
        ImageLoader.showImage(imageLoaderOptions)
        github_jump.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://github.com/wanglg")
            startActivity(intent)
        }
//        val imagebgOptions = ImageLoaderOptions.Builder(iv_bg, R.mipmap.icon_mine_head).blurImage(true).blurValue(20).build()
//        ImageLoader.showImage(imagebgOptions)

    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
    }
}