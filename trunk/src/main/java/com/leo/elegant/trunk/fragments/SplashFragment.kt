package com.leo.elegant.trunk.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import com.agile.android.leo.mvp.IPresenter
import com.android.leo.base.ui.fragments.BaseFragment
import com.leo.elegant.trunk.R
import com.leo.elegant.trunk.activity.MainActivity
import com.trello.rxlifecycle2.android.FragmentEvent
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_splash.*
import java.util.concurrent.TimeUnit

class SplashFragment : BaseFragment<IPresenter>() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_splash
    }

    override fun initPresenter() {
    }

    override fun configViews() {
        val animator_x = ObjectAnimator.ofFloat(splash_im, "scaleX", 1.0f, 1.1f)
        val animator_y = ObjectAnimator.ofFloat(splash_im, "scaleY", 1.0f, 1.1f)
        val animorSet = AnimatorSet()
        animorSet.duration = 1500;
        animorSet.play(animator_x).with(animator_y);
        animorSet.start();
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun requestData() {
        addDispose(Observable.timer(1500, TimeUnit.MILLISECONDS).compose(bindUntilEvent(FragmentEvent.DESTROY))
                .subscribe({
                    startActivity(MainActivity::class.java)
                    activity?.finish()
                }))
    }
}