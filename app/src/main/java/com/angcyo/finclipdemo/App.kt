package com.angcyo.finclipdemo

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import com.finogeeks.lib.applet.client.FinAppClient
import com.finogeeks.lib.applet.client.FinAppClient.appletApiManager
import com.finogeeks.lib.applet.client.FinAppConfig
import com.finogeeks.lib.applet.client.FinAppConfig.UIConfig
import com.finogeeks.lib.applet.client.FinAppConfig.UIConfig.CapsuleConfig
import com.finogeeks.lib.applet.client.FinAppProcessClient
import com.finogeeks.lib.applet.client.FinAppProcessClient.callback
import com.finogeeks.lib.applet.client.FinStoreConfig
import com.finogeeks.lib.applet.interfaces.FinCallback
import com.finogeeks.lib.applet.interfaces.IApi
import com.finogeeks.lib.applet.sdk.api.IAppletApiManager


/**
 * @author <a href="mailto:angcyo@126.com">angcyo</a>
 * @since 2023/04/24
 */
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        if (FinAppClient.isFinAppProcess(this)) {
            // 小程序进程
            // 小程序进程中注册api的方法能获取到小程序所在activity对象，可以用做创建对话框的context参数）
            callback = object : FinAppProcessClient.Callback {

                override fun getRegisterExtensionApis(activity: Activity): List<IApi>? {
                    val apis = ArrayList<IApi>()
                    //apis.add(LoginApi(activity))
                    //apis.add(ProfileApi())
                    return apis
                }

                override fun getRegisterExtensionWebApis(activity: Activity): List<IApi>? {
                    return null
                }
            }
            return
        }

        val uiConfig = UIConfig()
        uiConfig.isHideNavigationBarCloseButton = true
        uiConfig.isHideBackHome = true
        uiConfig.isHideForwardMenu = true
        uiConfig.isHideFeedbackAndComplaints = true
        uiConfig.moreMenuStyle = UIConfig.MORE_MENU_NORMAL

        val capsuleConfig = CapsuleConfig()
        capsuleConfig.capsuleWidth = 86f
        capsuleConfig.capsuleHeight = 31f
        capsuleConfig.capsuleRightMargin = 15f
        capsuleConfig.capsuleCornerRadius = 15.5f
        capsuleConfig.capsuleBorderWidth = 0.5f
        capsuleConfig.capsuleBgLightColor = Color.BLACK
        capsuleConfig.capsuleBgDarkColor = Color.WHITE
        capsuleConfig.capsuleBorderLightColor = Color.parseColor("#88ffffff")
        capsuleConfig.capsuleBorderDarkColor = Color.parseColor("#a5a9b4")

        //capsuleConfig.moreLightImage = R.mipmap.more_light
        //capsuleConfig.moreDarkImage = R.mipmap.more_dark
        capsuleConfig.moreBtnWidth = 25f
        capsuleConfig.moreBtnLeftMargin = 11f

        //capsuleConfig.closeLightImage = R.mipmap.close_light
        //capsuleConfig.closeDarkImage = R.mipmap.close_dark
        capsuleConfig.closeBtnWidth = 25f
        capsuleConfig.closeBtnLeftMargin = 9f

        capsuleConfig.capsuleDividerLightColor = Color.parseColor("#88ffffff")
        capsuleConfig.capsuleDividerDarkColor = Color.parseColor("#a5a9b4")

        uiConfig.capsuleConfig = capsuleConfig


        // 服务器信息集合
        val storeConfigs: MutableList<FinStoreConfig> = ArrayList()

        // 服务器1的信息
        val storeConfig1 = FinStoreConfig(
            "SDK Key信息",  // SDK Key
            "SDK Secret信息",  // SDK Secret
            "服务器1的地址",  // 服务器地址
            "服务器1的数据上报服务器地址",  // 数据上报服务器地址
            "/api/v1/mop/",  // 服务器接口请求路由前缀
            "",
            "加密方式" // 加密方式，国密:SM，md5: MD5（推荐）
        )
        storeConfigs.add(storeConfig1)

        // 服务器2的信息
        val storeConfig2 = FinStoreConfig(
            "SDK Key信息",  // SDK Key
            "SDK Secret信息",  // SDK Secret
            "服务器2的地址",  // 服务器地址
            "服务器2的数据上报服务器地址",  // 数据上报服务器地址
            "/api/v1/mop/",  // 服务器接口请求路由前缀
            "",
            "加密方式" // 加密方式，国密:SM，md5: MD5（推荐）
        )
        storeConfigs.add(storeConfig2)

        val config = FinAppConfig.Builder()
            //.setFinStoreConfigs(storeConfigs) // 服务器信息集合
            .setSdkKey(BuildConfig.APP_KEY)
            .setSdkSecret(BuildConfig.APP_SECRET)
            .setApiUrl(BuildConfig.API_URL)
            .setApiPrefix(BuildConfig.API_PREFIX)
            .setDebugMode(BuildConfig.DEBUG)
            .setUiConfig(uiConfig)
            .setEncryptionType(FinAppConfig.ENCRYPTION_TYPE_SM)
            .build()


        // SDK初始化结果回调，用于接收SDK初始化状态
        val callback = object : FinCallback<Any?> {
            override fun onSuccess(result: Any?) {
                // SDK初始化成功
                Toast.makeText(this@App, "SDK初始化成功", Toast.LENGTH_SHORT).show()
                // 注册自定义小程序API
                //extensionApiManager.registerApi(CustomApi(this@App))
                // 注册自定义H5 API
                //extensionWebApiManager.registerApi(CustomH5Api(this@App))
                // 设置IAppletHandler实现类
                //appletHandler = AppletHandler(applicationContext)

                // 在主进程设置"小程序进程调用主进程"的处理方法
                // 开发者也可以选择在主进程其他合适的代码位置设置处理方法

                // 在主进程设置"小程序进程调用主进程"的处理方法
                // 开发者也可以选择在主进程其他合适的代码位置设置处理方法
                appletApiManager.setAppletProcessCallHandler(object :
                    IAppletApiManager.AppletProcessCallHandler {
                    override fun onAppletProcessCall(
                        name: String,
                        params: String?,
                        callback: FinCallback<String>?
                    ) {
                        if (callback != null) {
                            /*if (name == LoginApi.API_NAME_LOGIN) {
                                // 从主进程获取登录信息，返回给小程序进程
                                // 这里返回的是虚拟的用户登录信息，开发者请从APP里面自行获取用户登录信息
                                val jsonObject = JSONObject()
                                try {
                                    jsonObject.put("userId", "123")
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                callback.onSuccess(jsonObject.toString())
                            }*/
                        }
                    }
                })
            }

            override fun onError(code: Int, error: String) {
                // SDK初始化失败
                Toast.makeText(this@App, "SDK初始化失败", Toast.LENGTH_SHORT).show()
            }

            override fun onProgress(status: Int, error: String) {
                Log.d("FinAppClient", "SDK初始化进度：$status $error")
            }
        }

        FinAppClient.init(this, config, callback)
    }
}