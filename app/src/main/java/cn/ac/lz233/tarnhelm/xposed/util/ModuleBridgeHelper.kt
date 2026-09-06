package cn.ac.lz233.tarnhelm.xposed.util

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Build
import android.os.IBinder
import cn.ac.lz233.tarnhelm.util.LogUtil
import cn.ac.lz233.tarnhelm.xposed.Config
import cn.ac.lz233.tarnhelm.xposed.ModuleDataBridge
import cn.ac.lz233.tarnhelm.xposed.module.Android

@SuppressLint("StaticFieldLeak")
object ModuleBridgeHelper {

    @Volatile private var bridge: ModuleDataBridge? = null
    @Volatile var isBridgeAvailable = false
    @Volatile private var isBound = false
    var mContext: Context? = null

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, binder: IBinder) {
            bridge = ModuleDataBridge.Stub.asInterface(binder)
            isBridgeAvailable = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            rebind()
        }

        override fun onBindingDied(name: ComponentName) {
            rebind()
        }

        override fun onNullBinding(name: ComponentName) {
            LogUtil.xpe("bridge service returned a null binder")
        }
    }

    private fun rebind() {
        bridge = null
        isBridgeAvailable = false
        unbindBridgeService(mContext)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.BAKLAVA) Android.startModuleAppProcess()
        bindBridgeService()
    }

    fun isBridgeActive(): Boolean {
        try {
            if (bridge == null) { return false }
            bridge!!.ping()
            return true
        } catch (thr: Throwable) {
            thr.printStackTrace()
            return false
        }
    }

    private fun bridgeIntent() = Intent().apply {
        component = ComponentName(Config.packageName, Config.bridgeServiceName)
        action = Config.bridgeAction
    }

    @SuppressLint("MissingPermission")
    fun bindBridgeService(context: Context? = mContext) {
        if (isBound) return
        LogUtil.xp("bind bridge service")
        runCatching {
            val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                context?.bindServiceAsUser(
                    bridgeIntent(),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE,
                    android.os.Process.myUserHandle()
                )
            } else {
                context?.bindService(
                    bridgeIntent(),
                    serviceConnection,
                    Context.BIND_AUTO_CREATE
                )
            }
            isBound = result != null
            if (result == false) unbindBridgeService(context)
        }.onFailure { LogUtil.xpe(it) }
    }

    fun unbindBridgeService(context: Context? = mContext) {
        if (!isBound) return
        LogUtil.xp("unbind bridge service")
        runCatching {
            context?.unbindService(serviceConnection)
        }
        isBound = false
    }

    fun doTarnhelms(string: String): String {
        if (!(isBridgeAvailable && isBridgeActive())) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.BAKLAVA) Android.startModuleAppProcess()
            bindBridgeService()
        }
        bridge?.let {
            return it.doTarnhelms(string)
        }
        LogUtil.xpe("Bridge is not available")
        return string
    }

}
