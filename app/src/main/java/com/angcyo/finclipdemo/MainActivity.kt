package com.angcyo.finclipdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.finogeeks.lib.applet.client.FinAppClient
import com.finogeeks.lib.applet.modules.callback.FinSimpleCallback
import com.finogeeks.lib.applet.sdk.api.request.IFinAppletRequest

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.start).setOnClickListener {
            //FinAppClient.appletApiManager.startApplet(this, "5e0dc1f574193e00010d73c1");
            FinAppClient.appletApiManager.startApplet(
                this,
                IFinAppletRequest.Companion.fromAppId("6445e4ae56ce210001c1ae4c").apply {
                    //setStartParams(mapOf())
                },
                object : FinSimpleCallback<String?>() {

                    override fun onError(code: Int, error: String?) {
                        super.onError(code, error)
                        Log.e("MainActivity", "onError: $code $error")
                    }

                    override fun onProgress(status: Int, info: String?) {
                        super.onProgress(status, info)
                        Log.i("MainActivity", "onProgress: $status $info")
                    }

                    override fun onSuccess(result: String?) {
                        super.onSuccess(result)
                        Log.i("MainActivity", "onSuccess: $result")
                    }
                }
            );
        }
    }
}