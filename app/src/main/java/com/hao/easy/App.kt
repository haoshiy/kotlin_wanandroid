package com.hao.easy

import android.os.StrictMode
import com.alibaba.android.arouter.launcher.ARouter
import com.hao.easy.base.BaseApplication
import com.hao.easy.base.Config
import com.hao.library.service.InitX5Service
import com.hao.library.utils.AppUtils
import com.hao.library.utils.CoroutineUtils
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta

/**
 * @author Yang Shihao
 */
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        if (AppUtils.isMainProcess(instance, android.os.Process.myPid())) {
            CoroutineUtils.io {
                Config.init()
                initARouter()
                // 此处很生猛
                InitX5Service.start(instance)
            }
            initBugly()
        }
//        openStrictMode()
    }

    private fun initARouter() {
        ARouter.openLog()
        ARouter.openDebug()
        ARouter.init(this)
    }

    private fun initBugly() {
        Beta.autoInit = false
        Bugly.init(this, "50bf0502bf", true)
    }

    private fun openStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .penaltyDeath()
                    .build()
            )
        }
    }
}